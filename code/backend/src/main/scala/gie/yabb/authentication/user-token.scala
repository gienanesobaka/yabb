package gie.yabb

import java.util

import gie.security.{UserPrivilege, UserToken}
import gie.yabb.bm.User

case class UserPrivilegeImpl(val id:util.UUID) extends UserPrivilege


case class SessionUserToken(user: User, privileges: Set[UserPrivilege]) extends UserToken {
  def userId: util.UUID = user.uuid
}