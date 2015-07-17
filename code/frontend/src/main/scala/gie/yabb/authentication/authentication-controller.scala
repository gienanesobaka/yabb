package gie.yabb.authentication

import biz.enef.angulate.Controller

import scala.scalajs.js.annotation.JSExport


class MainAuthenticationController(authenticationService: AuthenticationService) extends Controller {

  var login:String = ""
  var password:String = ""

  def authenticate(): Boolean = {
    println(s"login: ${login}, password: ${password}")
    authenticationService.authenticate(login, password)
  }

  def isAuthenticated(): Boolean = {
    authenticationService.isAuthenticated()
  }


}