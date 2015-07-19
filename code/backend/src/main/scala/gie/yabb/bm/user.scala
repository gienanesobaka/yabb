package gie.yabb.bm

import java.util.UUID

case class User(id: Long, uuid:UUID, name: String, password: String)

case class UserPrivilege(id: UUID, userID: Long)

