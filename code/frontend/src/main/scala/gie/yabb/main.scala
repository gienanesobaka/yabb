package gie.yabb

import biz.enef.angulate._
import biz.enef.angulate.{Scope, Controller}


import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport


object app extends JSApp {

  @JSExport
  def main(): Unit = {
    println("gie.yabb.app.main()")

    val module = angular.createModule("yabb", Nil)
    module.controllerOf[TestController]

  }


  @JSExport
  def clickedButton(): Unit ={
    println("Hello world!")
  }
}


@JSExport
class TestController extends Controller {
  var msg:String = "NoName"
  def printMsg(): Unit ={
    println(msg)
  }
}