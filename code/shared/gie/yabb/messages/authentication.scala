package gie.yabb.messages


case class AuthenticationRequest(login:String, password:String)
case class AuthenticationResponse(isAuthenticated:Boolean)