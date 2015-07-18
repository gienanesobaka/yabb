package gie.yabb.authentication

import biz.enef.angulate.{Scope, Controller}
import gie.yabb.messages.AuthenticationResponse

import scala.concurrent.Future
import scala.scalajs.js.annotation.JSExport
import scala.util.{Success, Failure}


class MainAuthenticationController(authenticationService: AuthenticationService, $scope:Scope) extends Controller {

  import gie.yabb.app.executionContext

  var login:String = ""
  var password:String = ""

  @JSExport
  var authError: String = ""

  def authenticate(): Unit = {
    authenticationService.authenticate(login, password).onComplete{
      case Failure(ex)=>
        authError = s"Error while processing authentication: ${ex.toString}"
        $scope.$apply()

      case Success(AuthenticationResponse(false)) =>
        authError = s"Invalid authentication credentials provided."
        $scope.$apply()

      case Success(AuthenticationResponse(true)) =>
        assume(authenticationService.isAuthenticated())
        $scope.$apply()
    }
  }

  def isAuthenticated(): Boolean = {
    authenticationService.isAuthenticated()
  }


}