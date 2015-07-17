package gie.yabb.authentication

import biz.enef.angulate.Service
import biz.enef.angulate.core.HttpService
import biz.enef.angulate.ext.CookiesService
import gie.yabb.serverApi

import upickle.default._

import gie.yabb.messages.AuthenticationRequest


class AuthenticationService($cookies: CookiesService, $http:HttpService) extends Service {

  println("AuthenticationService.ctor()")

  private val m_reAuthKeyName = s"${this.getClass.getName}-REAUTH"

  private var m_authenticatedLogin:Option[String] = None

  def isAuthenticated(): Boolean = m_authenticatedLogin.isDefined

  def authenticate(login: String, password: String): Boolean ={
    println("LOGIN: "+login)
    
    val wireMsg = write( AuthenticationRequest(login, password) )
    $http.post(serverApi.api.authentication.authenticate, wireMsg)

    m_authenticatedLogin = Some(login)

    $cookies.put(m_reAuthKeyName, login)

    isAuthenticated()
  }


}

