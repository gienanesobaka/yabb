package gie.yabb.authentication

import biz.enef.angulate.Service
import biz.enef.angulate.ext.CookiesService


class AuthenticationService($cookies: CookiesService) extends Service {

  println("AuthenticationService.ctor()")

  private val m_reAuthKeyName = s"${this.getClass.getName}-REAUTH"

  private var m_authenticatedLogin:Option[String] = None

  def isAuthenticated(): Boolean = m_authenticatedLogin.isDefined

  def authenticate(login: String, password: String): Boolean ={
    println("LOGIN: "+login)
    m_authenticatedLogin = Some(login)

    $cookies.put(m_reAuthKeyName, login)

    isAuthenticated()
  }


}

