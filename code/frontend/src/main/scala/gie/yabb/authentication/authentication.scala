package gie.yabb.authentication

import biz.enef.angulate.Service
import biz.enef.angulate.ext.CookiesService


class AuthenticationService($cookies: CookiesService) extends Service {

  println("AuthenticationService.ctor()")

  private val m_reAuthKeyName = s"${this.getClass.getName}-REAUTH"

  private var m_isAuthenticated = false

  def isAuthenticated: Boolean = m_isAuthenticated

  def authenticate(login: String, password: String): Unit ={
    println("LOGIN: "+login)
    m_isAuthenticated = true

    $cookies.put(m_reAuthKeyName, login)
  }


}

