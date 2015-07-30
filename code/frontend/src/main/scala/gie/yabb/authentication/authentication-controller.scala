package gie.yabb.authentication

import biz.enef.angulate.core.Location
import biz.enef.angulate.{Scope, Controller}
import gie.yabb.{AlertsHolder, AlertsTypes}
import gie.yabb.messages.AuthenticationResponse
import gie.yabb.app.route
import slogging.StrictLogging

import scala.concurrent.Future
import scala.scalajs.js.annotation.{JSExportAll, JSExport}
import scala.util.{Success, Failure}


class MainAuthenticationController(authenticationService: AuthenticationService, $scope:Scope, $location: Location) extends Controller with StrictLogging {

  import gie.yabb.app.executionContext

  logger.debug(s"MainAuthenticationController.ctor()")

  implicit def implicitLocation = $location

  var login:String = ""
  var password:String = ""

  val alerts = new AlertsHolder()

  private var m_busy = false

  def isBusy():Boolean ={
    m_busy
  }

  def navigateToRegister(): Unit ={
    route.register.navigate()
  }

  def authenticate(): Unit = {

    m_busy = true

    def reevaluateOnResult(): Unit ={
        m_busy = false
        $scope.$apply()
    }

    authenticationService.authenticate(login, password).onComplete{
      case Failure(ex)=>
        alerts.replaceAll(AlertsTypes.danger, s"Error while processing authentication: ${ex.toString}")
        reevaluateOnResult()

      case Success(AuthenticationResponse(false)) =>
        alerts.replaceAll(AlertsTypes.danger, s"Invalid authentication credentials provided.")
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