package gie.yabb

import biz.enef.angulate.{Scope, Controller}
import slogging.LazyLogging
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

}

class CategoryNode(var cat:Category, children:Seq[CategoryNode]=Seq()) extends Item[CategoryNode](children){
  override def label:String = cat.name
  override def label_=(name:String): Unit ={
    cat = cat.copy(name = name)
  }

  def isDirty = cat.id==CategoryMagic.catIdDirty
  def isNew = cat.id==CategoryMagic.catIdNew
  def id = {
    assume(cat.id>=0)
    cat.id
  }

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

  private val rootNode = new Node(Category(0, None, "[ROOT]"))

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

      Category(CategoryMagic.catIdNew, None, newNodeLabel) |>
        (d=>new Node(d)) |>
        (node.children.push(_))

    }
  }

  def rename(): Unit ={
    import CategoryMagic._

    selected.filter{ case(node, parent)=>node ne rootNode }.fold{
      logger.debug("nothing to rename")
    }{ case(node, parentNode)=>

        node.cat = node.cat.copy(id = if(node.cat.id==catIdNew) catIdNew else catIdDirty, name = nodeLabel)

        logger.debug(s"${node.cat}")
    }
  }

  def delete(): Unit ={
    selected.filter{ case(node, parent)=>node ne rootNode }.fold{
      logger.debug("nothing to delete")
    }{ case(node, parentNode)=>

      val peers = parentNode.children
      val idx = peers.indexOf(node); assume(idx != -1)
      peers.remove(idx)

      if(node.isNew){
        logger.debug(s"Node '${node.label}' is new, no need to record 'delete' operation.")
      } else {
        parentNode.deleteQueue += node
      }

      selected = None
    }
  }

}