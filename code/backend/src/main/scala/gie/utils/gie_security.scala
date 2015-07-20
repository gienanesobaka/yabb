package gie

import com.typesafe.scalalogging.LazyLogging

package security {


import java.util
import java.util.UUID

class SecurityException extends Exception
  class AccessDeniedException(userUUID: UUID) extends SecurityException
  class PrivilegeNotHeldException(userUUID: UUID, required: UserPrivilege) extends AccessDeniedException(userUUID)

  trait UserPrivilege {
    val id:util.UUID
  }

  trait UserToken {
    def userId: util.UUID
    def privileges: Set[UserPrivilege]
  }
  case class ResourceAccessToken(requiredPrivileges : Set[UserPrivilege]) extends AnyVal

  package privileges {
    object PRIV_ROOT extends UserPrivilege { val id = gie.UUID(Array[Byte](0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,7)) }
  }


}


package object security extends LazyLogging{
  val systemUUID = gie.UUID(Array[Byte](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1))
  val guestUUID = gie.UUID(Array[Byte](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,100))

  def requirePrivileges(user: UserToken, requiredPrivileges: Seq[UserPrivilege]){
    if( user.userId == systemUUID) {
      logger.info(s"SYSTEM UUID, ignoring checks for ${user}, required resource access privileges: ${requiredPrivileges}")
    } else {
      val userPrivs = user.privileges
      requiredPrivileges.foreach{ priv =>
        if(!userPrivs.contains(priv)) throw new PrivilegeNotHeldException(user.userId, priv)
      }
    }
  }
}



