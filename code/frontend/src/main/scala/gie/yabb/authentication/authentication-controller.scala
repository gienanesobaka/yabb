package gie.yabb.authentication

import biz.enef.angulate.Controller

class MainAuthenticationController(authenticationService: AuthenticationService) extends Controller {

  def isAuthenticated(): Boolean = authenticationService.isAuthenticated()

}