package gie.yabb

import biz.enef.angulate.{Controller, Scope}
import gie.yabb.authentication.AuthenticationService
import gie.yabb.messages._
import slogging.LazyLogging

import scala.scalajs.js.annotation.JSExportAll
import scala.util.{Success, Failure}
import scalajs.js

@JSExportAll
case class RegistrationFormScope(
                   var login: js.UndefOr[String]=js.undefined,
                   var firstName: js.UndefOr[String]=js.undefined,
                   var lastName: js.UndefOr[String]=js.undefined,
                   var email: js.UndefOr[String]=js.undefined,
                   var password: js.UndefOr[String]=js.undefined,
                   var password2: js.UndefOr[String]=js.undefined)


class RegistrationController(authenticationService: AuthenticationService, $scope:Scope) extends Controller with LazyLogging {

  import gie.yabb.app.executionContext

  $scope.asInstanceOf[js.Dynamic].data = new RegistrationFormScope().asInstanceOf[js.Object]
  $scope.asInstanceOf[js.Dynamic].minPassword=6

  val alerts = new AlertsHolder()
  val busy = new BusyHolder()

  def register(formData: RegistrationFormScope): Unit ={
    val firstName = formData.firstName.toOption
    val lastName  = formData.lastName.toOption

    (for{
      login       <-formData.login
      email       <-formData.email
      password    <-formData.password
      password2   <-formData.password2
    } yield ( RegistrationRequest(AuthenticationRequest(login, password), firstName, lastName, email), password2)).toOption.fold{
      alerts.addError(logger)( s"Internal decoding of form data have failed: ${formData}" )
    }{ case(request, password2)=>

      logger.debug( s"SERVER READY REQUEST: ${request}" )

      assume(request.authentication.password==password2)

      busy.incBusy()

      authenticationService.register(request).onComplete{ result=>
        busy.decBusy()

        result match {
          case Failure(ex)=>
            alerts.addError(logger)(s"Error while processing registration: ${ex.toString}")

          case Success(RegistrationResponse(Left(failureInfo))) =>
            alerts.addError(logger)(s"Registration failure: ${failureInfo}")

          case Success(RegistrationResponse(Right(registrationInfo))) =>
            alerts.addInfo(logger)(s"Successfully enqueued for registration, confirmation cookie is: ${registrationInfo.confirmationCookie}")
        }

        $scope.$apply()
      }
    }


  }


}