package gie.yabb


import gie.utils.ImplicitPipe._

import biz.enef.angulate._
import biz.enef.angulate.{Scope, Controller}
import angulate.uirouter._
import gie.yabb.authentication.AuthenticationService


import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport



object app extends JSApp {

  def main(): Unit = {
    println("gie.yabb.app.main()")

    val module = angular.createModule("gie.yabb", Seq("ui.router"))

    module.serviceOf[AuthenticationService]

    module.controllerOf[TestController]

    gie.yabb.states.root.state(module, "main-view","")

    module.run{ ($state:StateService) =>
      //$state.go(gie.yabb.main.States.names.default)
    }

  }


  @JSExport
  def clickedButton(): Unit ={
    println("Hello world!")
  }
}



class TestController(authenticationService: AuthenticationService) extends Controller {
  var msg:String = "NoName"
  def printMsg(): Unit ={
    println(s"login status: ${authenticationService.isAuthenticated}")
    println(msg)
  }
}

