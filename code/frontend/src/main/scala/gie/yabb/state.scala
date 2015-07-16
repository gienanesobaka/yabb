package gie.yabb

import biz.enef.angulate.Scope
import scalajs.js

object StateHelpers {
  def getFullStateName(parentFullStateName: String, childStateName:String) = {
    assume(!childStateName.isEmpty)
    if( (parentFullStateName eq null) || parentFullStateName.isEmpty) childStateName else s"${parentFullStateName}.${childStateName}"
  }

  def getPartsDirectory(parent:String, child:String) = {
    assume(!child.isEmpty)
    if( (parent eq null) || parent.isEmpty) child else s"${parent}/${child}"

  }
}

object helpers {

  def controllerAs(scope: Scope, asName: String)( controllerCtor: => AnyRef) = {
    val controller = controllerCtor
    val controllerJS = controller.asInstanceOf[js.Object]
    scope.asInstanceOf[js.Dynamic].updateDynamic(asName)(controllerJS)

    controllerJS
  }

}