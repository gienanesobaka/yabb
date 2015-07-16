package gie.yabb.states.login

import angulate.uirouter.{StateService, View, State, StateProvider}
import biz.enef.angulate.Module.RichModule
import biz.enef.angulate.{Controller, Service}
import gie.yabb.StateHelpers
import gie.yabb.authentication.AuthenticationService


object state {

  import StateHelpers._

  val name = "login"
  val dirName = name
  val controllerName = classOf[AuthenticationController].getName
  def view(parentPartsDirectory:String) = View(s"${getPartsDirectory(parentPartsDirectory,dirName)}/part-index.html", controllerName)


  def apply(module: RichModule, viewName:String, partsDirectory:String): RichModule= {


    //module.controller()

    module.controllerOf[AuthenticationController]

    module.config{ ($stateProvider:StateProvider)=>

    }


    module
  }


}


class AuthenticationController(authenticationService: AuthenticationService, $state: StateService) extends Controller {
//  if(authenticationService.isAuthenticated)
//    $state.go(state.states.names.notAuthenticated)
//  else
//    $state.go(state.states.names.authenticated)

}