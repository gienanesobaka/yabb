package gie.yabb.states.login

import angulate.uirouter.{StateService, View, State, StateProvider}
import biz.enef.angulate.Module.RichModule
import biz.enef.angulate.{Scope, Controller, Service}
import gie.yabb.{helpers, StateHelpers}
import gie.yabb.authentication.AuthenticationService
import gie.yabb.states
import scala.scalajs.js.annotation.JSExport
import scalajs.js


object state {


  def build(module: RichModule, parentFullStateName:String, placeAtView:String): String ={

    val fullStateName = StateHelpers.getFullStateName(parentFullStateName, "login")

    val stateControllerName = (s"${fullStateName}--AuthenticationController")

    val notAuthenticatedStateName = states.not_authenticated.state.build(module,fullStateName,"main-view")


    println("GEN NEW CONTROLLER: "+stateControllerName)
    module.controller(
      stateControllerName,
      (authenticationService: AuthenticationService, $state: StateService, $scope:Scope)=>{
        helpers.controllerAs($scope, "authController"){ new AuthenticationController(authenticationService,$state, notAuthenticatedStateName) }
      }
    )

    module.config( ($stateProvider:StateProvider) => {
      $stateProvider.state(
        fullStateName,
        State(
          url="/login",
          views=Map(
            placeAtView -> View("parts/part-login.html", stateControllerName))))
    })




    fullStateName
  }
}



class AuthenticationController(
            authenticationService: AuthenticationService,
            $state: StateService,
            notAuthenticatedStateName:String) extends Controller {

  @JSExport
  def isAuthenticated() = authenticationService.isAuthenticated


  if(authenticationService.isAuthenticated)
    $state.go("state.states.names.notAuthenticated")
  else
    $state.go(notAuthenticatedStateName)

}