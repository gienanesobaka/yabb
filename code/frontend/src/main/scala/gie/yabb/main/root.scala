package gie.yabb.states.root

import gie.yabb.StateHelpers
import gie.yabb.states

import angulate.uirouter.{StateService, View, State, StateProvider}
import biz.enef.angulate.Controller
import biz.enef.angulate.Module.RichModule

object state {
  import StateHelpers._

  val name = "" //root state
  val dirName = "root"
  val controllerName = classOf[RootController].getName


  def apply(module: RichModule, viewName:String, partsDirectory:String): RichModule= {

    module.controllerOf[RootController]

    module.config{ ($stateProvider:StateProvider)=>
      $stateProvider.state( getStateName(name, states.login.state.name), State(
        url="/login",
        views = Map(viewName -> states.login.state.view(dirName))))
    }

    states.login.state(module, viewName, partsDirectory)

    module
  }


}



class RootController($state: StateService) extends Controller {

}