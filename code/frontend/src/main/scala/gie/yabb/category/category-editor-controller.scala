package gie.yabb

import java.util.UUID

import biz.enef.angulate.{Scope, Controller}
import slogging.LazyLogging
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.scalajs.js.Dynamic.literal
import scala.scalajs.js.{Array}
import scala.util.Random
import scalajs.js
import js.JSConverters._
import scala.scalajs.js.annotation.{JSExport, JSExportAll}
import gie.utils.ImplicitPipe._

@JSExportAll
abstract class Item[SelfT <: Item[_]](p_children:Seq[Item[SelfT]] = Seq()){

  var label:String
  var children:js.Array[Item[SelfT]] = p_children.toJSArray

  def asSelf = this.asInstanceOf[SelfT]

}

package CategoryNodeState {
  sealed trait CategoryNodeState
  case class Clean() extends CategoryNodeState
  case class Dirty() extends CategoryNodeState
  case class New() extends CategoryNodeState
}

class CategoryNode(var label:String, var state:CategoryNodeState.CategoryNodeState, nodeId:Option[Long] = None, children:Seq[CategoryNode]=Seq()) extends Item[CategoryNode](children){

  val id:Either[UUID, Long] = nodeId.toRight(gie.UUID())
  val deleteQueue = ArrayBuffer[CategoryNode]()
}


class CategoryEditorController($scope:Scope) extends Controller with LazyLogging {

  type Node = CategoryNode

  logger.debug("CategoryEditorController.ctor()")


  $scope.asInstanceOf[js.Dynamic].treeOptions = literal(
    equality = (n1:js.Any, n2:js.Any) => n1 eq n2
  )

  private var selected:Option[Tuple2[Node,Node]] = None

  def onSelect(p: Node, parentNode:Node) = {
    selected = Some(p, parentNode)
    nodeLabel = p.label
  }

  private val rootNode = new Node("[ROOT]", CategoryNodeState.Clean())

  var data:js.Array[Node] = Array(rootNode).toJSArray

  var nodeLabel=""
  var newNodeLabel=""

  def add(): Unit ={

    val newNodeName = newNodeLabel

    selected
      .orElse(Some(rootNode,null))
      .filter(_ => !newNodeLabel.isEmpty)
      .filter{ case(node, parent) => !node.children.find(_.label==newNodeName).isDefined }
      .foreach{ case(node,parent)=>

      node.children.push( new Node(newNodeLabel, CategoryNodeState.New()) )

    }
  }

  def rename(): Unit ={

    selected.filter{ case(node, parent)=>node ne rootNode }.fold{
      logger.debug("nothing to rename")
    }{ case(node, parentNode)=>

      node.label = nodeLabel

      node.state match {
        case CategoryNodeState.Clean() => node.state = CategoryNodeState.Dirty()
        case CategoryNodeState.New() =>
        case CategoryNodeState.Dirty() =>
      }

    }
  }

  def delete(): Unit ={
    selected.filter{ case(node, parent)=>node ne rootNode }.fold{
      logger.debug("nothing to delete")
    }{ case(node, parentNode)=>

      val peers = parentNode.children
      val idx = peers.indexOf(node); assume(idx != -1)
      peers.remove(idx)

      node.state match {
        case CategoryNodeState.New() =>
          logger.debug(s"Node '${node.label}' is a new, no need to record 'delete' operation.")
        case _ =>
          parentNode.deleteQueue += node
      }

      selected = None
    }
  }


  private def prepareCommandBuffer(): Unit ={

    type ComplexNodeId = Option[Either[UUID, Long]]

    val commandBuffer = new ArrayBuffer[CategoryCommand]()

    def processChildrenOf(node: CategoryNode): Unit ={

      val myId: ComplexNodeId = if(node eq rootNode) None else Some(node.id)

      node.children.foreach(n=>processNode(n.asSelf, myId))
    }

    def processNode(node: CategoryNode, parentNodeId: ComplexNodeId): Unit ={

      node.state match {
        case CategoryNodeState.Clean() => logger.debug(s"node '${node.label}' is clean")
        case CategoryNodeState.New()   => processNode_new(node, parentNodeId)
        case CategoryNodeState.Dirty() => processNode_dirty(node, parentNodeId)
      }

    }

    def processNode_new(node: CategoryNode, parentNodeId: ComplexNodeId): Unit ={
      commandBuffer += CategoryCommand(CategoryMagic.command.create, name = Some(node.label) )
    }

    def processNode_dirty(node: CategoryNode, parentNodeId: ComplexNodeId): Unit ={

    }

    processChildrenOf(rootNode)

  }

}