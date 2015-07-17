package gie.yabb.states.not_authenticated

import angulate.uirouter.{View, State, StateProvider, StateService}
import biz.enef.angulate.Module.RichModule
import biz.enef.angulate.{Controller, Scope}
import gie.yabb.{helpers, StateHelpers}
import gie.yabb.authentication.AuthenticationService

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportAll, JSExport}

object state {

  def build(module: RichModule, parentFullStateName:String, placeAtView:String): String ={

    val fullStateName = StateHelpers.getFullStateName(parentFullStateName, "not-authenticated")

    val stateControllerName = (s"${fullStateName}--NotAuthenticatedController")

    println("GEN NEW CONTROLLER: "+stateControllerName)

    module.controller(
      stateControllerName,
      (authenticationService: AuthenticationService, $state: StateService, $scope:Scope)=>{
        $scope.asInstanceOf[js.Dynamic].login = ""
        $scope.asInstanceOf[js.Dynamic].password = ""

        helpers.controllerAs($scope, "notAuthController"){ new NotAuthenticatedController(authenticationService,$state) }
      }
    )

    module.config( ($stateProvider:StateProvider) => {
      $stateProvider.state(
        fullStateName,
        State(
          url="/authenticate",
          controllerAs = "notAuthController",
          views=Map(
            placeAtView -> View("parts/part-do-login.html", stateControllerName))))
    })

    fullStateName
  }

}



@JSExportAll
class NotAuthenticatedController(authenticationService: AuthenticationService, $state: StateService) extends Controller {

  def authenticate(login: String, password: String): Unit ={
    authenticationService.authenticate(login, password)
    $state.go("login")
  }

}