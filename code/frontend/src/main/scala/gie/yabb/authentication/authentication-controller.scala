package gie.yabb.authentication

import biz.enef.angulate.core.Location
import biz.enef.angulate.{Scope, Controller}
import gie.yabb.messages.AuthenticationResponse

import scala.concurrent.Future
import scala.scalajs.js.annotation.JSExport
import scala.util.{Success, Failure}


class MainAuthenticationController(authenticationService: AuthenticationService, $scope:Scope, $location: Location) extends Controller {

  import gie.yabb.app.executionContext

  var login:String = ""
  var password:String = ""

  @JSExport
  var authError: String = ""

  private var m_busy = false

  def isBusy():Boolean ={
    m_busy
  }

  def navigateToRegister(): Unit ={
    $location.path("register")
  }

  def authenticate(): Unit = {

    m_busy = true

    def reevaluateOnResult(): Unit ={
        m_busy = false
        $scope.$apply()
    }

    authenticationService.authenticate(login, password).onComplete{
      case Failure(ex)=>
        authError = s"Error while processing authentication: ${ex.toString}"
        reevaluateOnResult()

      case Success(AuthenticationResponse(false)) =>
        authError = s"Invalid authentication credentials provided."
        reevaluateOnResult()

      case Success(AuthenticationResponse(true)) =>
        assume(isAuthenticated())
        reevaluateOnResult()
    }
  }

  def isAuthenticated(): Boolean = {
    authenticationService.isAuthenticated()
  }


}