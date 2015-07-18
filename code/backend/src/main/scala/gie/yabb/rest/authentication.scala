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

object YRestHelper {

  def requestToResponse[T: Reader, R: Writer](body: Box[Array[Byte]], maxRequestBodySize: Int=1024)(logger : =>Logger)(handler: T=>R): LiftResponse = {

    val response:Box[R] = body.flatMap{ v=>
      if (v.size<=maxRequestBodySize)
        Full(v)
      else {
        val msg = s"Violation of (size<=maxRequestBodySize), request body size is ${v.size} but maxRequestBodySize size is ${maxRequestBodySize}."
        Failure(msg, Full(new IllegalArgumentException(msg)), Empty)
      }
    }.map(new String(_)).flatMap{v=>tryo(read[T](v))}.flatMap( v=>tryo(handler(v)) )


    response match {

      case Full(v) =>
        JsonResponse(write(v))

      case Empty =>
        BadResponse()

      case Failure(msg,exception,chain) =>
        logger.warn(s"REST FAILED: msg:${msg}, exception: ${exception}, chain: ${chain}")

        BadResponse()
    }

  }

}


object authentication extends RestHelper with StrictLogging{
  import YRestHelper._

  serve {
    case req@Req("app" :: "api" :: authentication :: authenticate :: Nil, _, PostRequest)=>
      requestToResponse[AuthenticationRequest, AuthenticationResponse](req.body)(logger)(app.authenticate(_))
  }

}
