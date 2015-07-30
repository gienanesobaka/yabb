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
import gie.yabb.messages.{RegistrationResponse, RegistrationRequest, AuthenticationResponse, AuthenticationRequest}


class AuthenticationService(marshallingService: MarshallingService) extends Service with StrictLogging { self=>


  logger.debug("AuthenticationService.ctor()")

  private var m_authenticatedLogin:Option[String] = None

  def isAuthenticated(): Boolean = synchronized{ m_authenticatedLogin.isDefined }

  def authenticate(login: String, password: String): Future[AuthenticationResponse] = {

    marshallingService
      .postRequest[AuthenticationRequest, AuthenticationResponse](serverApi.api.authentication.authenticate, AuthenticationRequest(login, password))
      .map{v=>self.synchronized{ if(v.isAuthenticated) m_authenticatedLogin = Some(login) else m_authenticatedLogin=None}; v}

  }


  def register(request: RegistrationRequest): Future[RegistrationResponse] = {
    marshallingService.postRequest[RegistrationRequest, RegistrationResponse](serverApi.api.authentication.register, request)
  }


}

