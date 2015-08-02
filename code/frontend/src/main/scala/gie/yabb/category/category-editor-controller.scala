package gie.yabb

import biz.enef.angulate.{Scope, Controller}
import slogging.LazyLogging
import scala.scalajs.js.{Array}
import scala.util.Random
import scalajs.js
import js.JSConverters._
import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
class Item[T](var p_label:String,
              p_children:Seq[Item[T]] = Seq(),
              p_data:Option[T]=None,
              p_onSelect:Option[Item[T]=>Unit] = None,
              p_classes:Option[String]=None){

  var label = p_label
  var children:js.Array[Item[T]] = p_children.toJSArray
  var data:js.UndefOr[T] = p_data.orUndefined
  var onSelect:js.UndefOr[js.Function1[Item[T], Unit]] = p_onSelect.map( js.Any.fromFunction1(_) ).orUndefined
  var classes:js.UndefOr[String] = p_classes.orUndefined

}

class CategoryEditorController($scope:Scope) extends Controller with LazyLogging {

  type Node = Item[Unit]

  logger.debug("CategoryEditorController.ctor()")

  def onSelect(p: Item[Unit]) = {
    selected = Some(p).orUndefined
  }

  val rootNode = new Node("[ROOT]")

  var data:js.Array[Node] = Array(rootNode).toJSArray
  var selected:js.UndefOr[Node] = None.orUndefined

  def add(): Unit ={
    selected.toOption.orElse(Some(rootNode)).foreach{node=>
      node.children.push( new Node(s"Lalalo ${Random.nextInt()}") )
    }
  }

  def rename(): Unit ={
    selected.toOption.filter(_!=rootNode).fold{
      logger.debug("nothing to rename")
    }{node=>

    }
  }

  def delete(): Unit ={
    selected.toOption.filter(_!=rootNode).fold{
      logger.debug("nothing to delete")
    }{node=>

    }
  }

}