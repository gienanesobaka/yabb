package gie.yabb

import biz.enef.angulate.Service
import biz.enef.angulate.core.{HttpPromise, HttpService}
import biz.enef.angulate.ext.CookiesService
import gie.yabb.YHelpers._

import upickle.default._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class MarshallingService($http:HttpService) extends Service {

  def postRequest[T: Writer, R: Reader](url: String, message: T)(implicit ec: ExecutionContext): Future[R] = {
    val wireMessage = write[T](message)

    val p:HttpPromise[String] = $http.post(url, wireMessage)

    futureFromJs(p).flatMap{v=> tryToFuture(Try{ read[R](v) }) }
  }

}
