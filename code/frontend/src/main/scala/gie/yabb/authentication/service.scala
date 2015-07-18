package gie.yabb.authentication

import biz.enef.angulate.Service
import biz.enef.angulate.core.impl.HttpPromiseWrapper
import biz.enef.angulate.core.{HttpPromise, HttpService}
import biz.enef.angulate.ext.CookiesService
import upickle.default._

import gie.yabb.app.executionContext
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

import gie.yabb.serverApi
import gie.yabb.messages.{AuthenticationResponse, AuthenticationRequest}


class AuthenticationService($cookies: CookiesService, $http:HttpService) extends Service { self=>

  import gie.yabb.YHelpers._

  println("AuthenticationService.ctor()")

  private var m_authenticatedLogin:Option[String] = None

  def isAuthenticated(): Boolean = synchronized{ m_authenticatedLogin.isDefined }

  def authenticate(login: String, password: String): Future[AuthenticationResponse] = synchronized {

    val wireMsg = write( AuthenticationRequest(login, password) )

    val p:HttpPromise[String] = $http.post(serverApi.api.authentication.authenticate, wireMsg)

    futureFromJs(p)
      .flatMap{v=> tryToFuture(Try{ read[AuthenticationResponse](v) }) }
      .map{v=>self.synchronized{ if(v.isAuthenticated) m_authenticatedLogin = Some(login) else m_authenticatedLogin=None}; v}

  }


}

