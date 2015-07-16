package gie.yabb.states.not_authenticated

import angulate.uirouter.{View, State, StateProvider, StateService}
import biz.enef.angulate.Module.RichModule
import biz.enef.angulate.{Controller, Scope}
import gie.yabb.{helpers, StateHelpers}
import gie.yabb.authentication.AuthenticationService

import scala.scalajs.js.annotation.JSExport

object state {

  def build(module: RichModule, parentFullStateName:String, placeAtView:String): String ={

    val fullStateName = StateHelpers.getFullStateName(parentFullStateName, "not-authenticated")

    val stateControllerName = (s"${fullStateName}--NotAuthenticatedController")

    println("GEN NEW CONTROLLER: "+stateControllerName)
    module.controller(
      stateControllerName,
      (authenticationService: AuthenticationService, $state: StateService, $scope:Scope)=>{
        helpers.controllerAs($scope, "notAuthController"){ new NotAuthenticatedController(authenticationService,$state) }
      }
    )

    module.config( ($stateProvider:StateProvider) => {
      $stateProvider.state(
        fullStateName,
        State(
          url=null,
          views=Map(
            placeAtView -> View("parts/part-do-login.html", stateControllerName))))
    })

    fullStateName
  }

}



class NotAuthenticatedController(authenticationService: AuthenticationService, $state: StateService) extends Controller {


}