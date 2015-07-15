package bootstrap.liftweb


import gie.yabb.app

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import sitemap._
import Loc._


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {

    // where to search snippet
    LiftRules.addToPackages("gie.yabb.snippets")
    //LiftRules.addToPackages(gie.lift.menu.SNIPPET_PATH)
    LiftRules.cometRequestTimeout = Full(25)
    LiftRules.maxMimeSize = 2 * 10 * 1024 *1024
    LiftRules.maxMimeFileSize = LiftRules.maxMimeSize - 1024*1024

    // Build SiteMap

//    val homeLoc = Menu(Loc("index" ,
//      new Loc.Link("index"::Nil, false),
//      <span><i class="icon-home icon-white"></i>  Начало </span> ) )


//    val menus =
//      homeLoc :: //Menu.i("Начало") / "index" ::
//      Menu(Loc("contentMenu" ,
//    				new Loc.Link("contentlst"::Nil, false),
//    				<span><i class="icon-file icon-white"></i> Материалы</span>,
//    				Loc.If( ()=> sec.User.checkAccess(sec.Role.teacher :: sec.Role.admin :: Nil),
//    				        ()=> NotFoundResponse() ) ) ) ::
//      Menu(Loc("uploadMenu" ,
//    				new Loc.Link("upload"::Nil, false),
//            <span><i class="icon-upload icon-white"></i> Загрузка материала</span>,
//    				Loc.If( ()=> sec.User.checkAccess(sec.Role.teacher :: sec.Role.admin :: Nil),
//    				        ()=> NotFoundResponse() ) ) ) ::
//      Nil

    //LiftRules.setSiteMapFunc(() => sitemapMutators(sitemap))
    //LiftRules.setSiteMap( SiteMap( (menus ::: secMenu ::: testMenu): _*))
    // Use jQuery 1.4
    LiftRules.jsArtifacts = net.liftweb.http.js.jquery.JQueryArtifacts
    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart = Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd = Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)
    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // What is the function to test if a user is logged in?
    //LiftRules.loggedInTest = Full(() => sec.User.loggedIn_?)

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))

    //net.liftmodules.FoBo.InitParam.ToolKit = net.liftmodules.FoBo.Bootstrap231
    //net.liftmodules.FoBo.init()

    app.boot()
    LiftRules.unloadHooks.append(app.close)
  }
}
