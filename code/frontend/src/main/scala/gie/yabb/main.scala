package gie.yabb


import biz.enef.angulate.ext.{Route, RouteProvider}

import biz.enef.angulate._
import biz.enef.angulate.{Scope, Controller}
import gie.yabb.authentication.{AuthenticationService, MainAuthenticationController}


import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport


object serverApi {
  val prefix = "/app/api/"
  object api {
    object authentication {
      val authenticate = s"${prefix}authentication/authenticate"
    }
  }
}

object app extends JSApp {

  implicit val executionContext = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  private val parts = "parts"

  def main(): Unit = {
    println("gie.yabb.app.main()")

    val module = angular.createModule("gie.yabb", Seq("ngCookies", "ngRoute", "ui.validate"))

    module
      .serviceOf[MarshallingService]
      .serviceOf[AuthenticationService]

    module
      .controllerOf[TestController]
      .controllerOf[RegistrationController]
      .controllerOf[MainAuthenticationController]


    module.config{ ($routeProvider: RouteProvider)=>
      $routeProvider
        .when("/authenticate",
          Route(templateUrl = s"${parts}/authentication.html", controllerAs = "controller", controller = classOf[MainAuthenticationController].getName))
        .when("/register",
          Route(templateUrl = s"${parts}/register.html", controllerAs = "controller", controller = classOf[RegistrationController].getName))
        .otherwise(
          Route(redirectTo = "/"))
    }

    module.run{ ()=>

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

