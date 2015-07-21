package gie.yabb.rest

import com.typesafe.scalalogging.{StrictLogging, Logger}
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.rest._
import net.liftweb.json.JsonAST.JString
import net.liftweb.util.Helpers.tryo

import upickle.default._

import gie.yabb.app
import gie.yabb.messages.{AuthenticationResponse, AuthenticationRequest}


object authentication extends RestHelper with StrictLogging{
  import gie.yabb.YRestHelper._

  serve {
    case req@Req("app" :: "api" :: authentication :: authenticate :: Nil, _, PostRequest)=>
      app.handleRequest[AuthenticationRequest, AuthenticationResponse](req.body)(logger)(app.authenticate(_))
      //requestToResponse[AuthenticationRequest, AuthenticationResponse](req.body)(logger)(app.authenticate(_))
  }

}
