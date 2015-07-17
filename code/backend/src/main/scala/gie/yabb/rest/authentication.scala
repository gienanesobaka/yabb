package gie.yabb.rest

import com.typesafe.scalalogging.StrictLogging
import gie.yabb.messages.AuthenticationRequest
import net.liftweb.common.{ParamFailure, Failure, Full, Empty}
import net.liftweb.http._
import net.liftweb.http.rest._
import net.liftweb.json.JsonAST.JString
import upickle.default._

/**
 * Created by gie on 7/18/15.
 */



object authentication extends RestHelper with StrictLogging{
  val maxRequestBodySize=1024

  serve {


    case req@Req("app" :: "api" :: authentication :: authenticate :: Nil, _, PostRequest)=>
      var answer = req.body
        .flatMap(v=>if (v.size<=maxRequestBodySize) Full(v) else Failure("", Full(new IllegalArgumentException(s"size<=maxRequestBodySize, but actual size is ${v.size}")), Empty))

      answer match {
        case Failure(msg,exception,chain) =>
          logger.warn(s"REST FAILED: msg:${msg}, exception: ${exception}, chain: ${chain}")

          BadResponse()

        case _ =>
          JString("Static")
      }

  }

}
