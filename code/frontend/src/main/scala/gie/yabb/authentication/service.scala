package gie.yabb.authentication

import biz.enef.angulate.Service
import biz.enef.angulate.core.impl.HttpPromiseWrapper
import biz.enef.angulate.core.{HttpPromise, HttpService}
import biz.enef.angulate.ext.CookiesService

import gie.yabb.app.executionContext
import slogging.StrictLogging
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

import gie.yabb.{MarshallingService, serverApi}
import gie.yabb.messages.{AuthenticationResponse, AuthenticationRequest}


class AuthenticationService(marshallingService: MarshallingService) extends Service with StrictLogging { self=>

  import gie.yabb.YHelpers._

  logger.debug("AuthenticationService.ctor()")

  private var m_authenticatedLogin:Option[String] = None

  def isAuthenticated(): Boolean = synchronized{ m_authenticatedLogin.isDefined }

  def authenticate(login: String, password: String): Future[AuthenticationResponse] = synchronized {


    marshallingService
      .postRequest[AuthenticationRequest, AuthenticationResponse](serverApi.api.authentication.authenticate, AuthenticationRequest(login, password))
      .map{v=>self.synchronized{ if(v.isAuthenticated) m_authenticatedLogin = Some(login) else m_authenticatedLogin=None}; v}

  }


}

