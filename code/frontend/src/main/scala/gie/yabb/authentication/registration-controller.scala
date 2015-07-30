package gie.yabb

import biz.enef.angulate.{Controller, Scope}
import gie.yabb.authentication.AuthenticationService
import gie.yabb.messages._
import slogging.LazyLogging

import scala.scalajs.js.annotation.JSExportAll
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

  $scope.asInstanceOf[js.Dynamic].data = new RegistrationFormScope().asInstanceOf[js.Object]
  $scope.asInstanceOf[js.Dynamic].minPassword=6

  def register(formData: RegistrationFormScope): Unit ={
    val firstName = formData.firstName.toOption
    val lastName  = formData.lastName.toOption

    (for{
      login       <-formData.login
      email       <-formData.email
      password    <-formData.password
      password2   <-formData.password2
    } yield ( RegistrationRequest(AuthenticationRequest(login, password), firstName, lastName, email), password2)).toOption.fold{
      logger.error(s"Internal decoding of form data have failed: ${formData}")
    }{ case(request, password2)=>
        logger.debug( s"SERVER READY REQUEST: ${request}" )

    }
  }


}