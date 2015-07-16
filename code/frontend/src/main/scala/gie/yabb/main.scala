package gie.yabb

import biz.enef.angulate._
import biz.enef.angulate.{Scope, Controller}


import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport


object app extends JSApp {

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



class TestController extends Controller {
  var msg:String = "NoName"
  def printMsg(): Unit ={
    println(msg)
  }
}