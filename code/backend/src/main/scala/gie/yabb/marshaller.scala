package gie.yabb

import com.typesafe.scalalogging.Logger
import net.liftweb.common.{Empty, Failure, Full, Box}
import net.liftweb.http.{BadResponse, JsonResponse, LiftResponse}
import net.liftweb.util.Helpers._
import upickle.default._


object YRestHelper {

  def requestToResponse[T : Reader, R : Writer](body: Box[Array[Byte]], maxRequestBodySize: Int=1024)(logger : =>Logger)(handler: T=>R): LiftResponse = {

    val request:Box[String] = body.flatMap{ v=>
      if (v.size<=maxRequestBodySize)
        Full(v)
      else {
        val msg = s"Violation of (size<=maxRequestBodySize), request body size is ${v.size} but maxRequestBodySize size is ${maxRequestBodySize}."
        Failure(msg, Full(new IllegalArgumentException(msg)), Empty)
      }
    }.map(new String(_))

    requestToResponse[T,R](request)(logger)(handler)

  }

  def requestToResponse[T: Reader, R: Writer](body: Box[String])(logger : =>Logger)(handler: T=>R): LiftResponse = {

    val response = body.flatMap{v=>tryo( read[T](v) )}.flatMap( v=>tryo(handler(v)) )

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

trait MarshallerServiceTrait {
  import YRestHelper._

  def handleRequest[T: Reader, R: Writer](messageData:Box[Array[Byte]])
                                         (logger : =>Logger)
                                         (handler: T=>R) :LiftResponse = {
    requestToResponse[T, R](messageData)(logger){ handler }
  }

}