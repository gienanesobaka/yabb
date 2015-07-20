package gie.yabb.bm

import java.util.UUID

case class User(id: Long, uuid:UUID, name: String, password: String, email:String, active:Boolean=false)

case class UserPrivilege(id: UUID, userID: Long)

