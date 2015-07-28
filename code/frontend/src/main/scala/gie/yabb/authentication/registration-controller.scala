package gie.yabb

import biz.enef.angulate.{Controller, Scope}
import gie.yabb.authentication.AuthenticationService
import gie.yabb.messages._

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


class RegistrationController(authenticationService: AuthenticationService, $scope:Scope) extends Controller {

  $scope.asInstanceOf[js.Dynamic].data = new RegistrationFormScope().asInstanceOf[js.Object]
  $scope.asInstanceOf[js.Dynamic].minPassword=6


  def register(formData: RegistrationFormScope): Unit ={
    println(s"REQUEST: "+formData)
  }


}