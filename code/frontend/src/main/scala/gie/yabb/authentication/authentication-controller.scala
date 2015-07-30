package gie.yabb.authentication

import biz.enef.angulate.core.Location
import biz.enef.angulate.{Scope, Controller}
import gie.yabb.{BusyHolder, AlertsHolder, AlertsTypes}
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
  val busy = new BusyHolder()

  def navigateToRegister(): Unit ={
    route.register.navigate()
  }

  def authenticate(): Unit = {
    busy.incBusy()

    authenticationService.authenticate(login, password).onComplete{ result=>

      busy.decBusy()

      result match {
        case Failure(ex)=>
          alerts.clearAlerts()
          alerts.addError(logger)(s"Error while processing authentication: ${ex.toString}")

        case Success(AuthenticationResponse(false)) =>
          alerts.clearAlerts()
          alerts.addError(logger)(s"Invalid authentication credentials provided.")

        case Success(AuthenticationResponse(true)) =>
          assume(isAuthenticated())
      }

      $scope.$apply()
    }

  }

  def isAuthenticated(): Boolean = {
    authenticationService.isAuthenticated()
  }


}