package gie.yabb

import com.typesafe.scalalogging.StrictLogging
import gie.yabb.app._
import gie.yabb.bm.{UserPrivilege, User}
import gie.yabb.messages.{AuthenticationResponse, AuthenticationRequest}

trait AuthenticationLogic {

  class AuthenticationImpl extends StrictLogging {

    def authenticate(request: AuthenticationRequest): AuthenticationResponse = {

      if(request.login.isEmpty || request.password.isEmpty) {
        AuthenticationResponse(false)
      } else {
        dao.selectUserWithPrivileges(request.login).filter{ case (u,p) => u.password==request.password}.map{ case (u,p) =>
          logger.info(s"Successfully logged in: ${request}")
          buildSessionUserToken(u,p)
        }.fold{
          logger.warn(s"Failed to log in: ${request}")
          AuthenticationResponse(false)
        }{token=>
          currentUser(token); AuthenticationResponse(true)
        }
      }

    }

    private def buildSessionUserToken(user: User, privileges: Seq[UserPrivilege]) ={
      SessionUserToken(user, privileges.map(dbPriv=>UserPrivilegeImpl(dbPriv.id)).toSet)
    }

  }

  val authentication = new AuthenticationImpl()

}
