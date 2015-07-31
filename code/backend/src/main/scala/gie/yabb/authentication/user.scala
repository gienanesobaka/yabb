package gie.yabb.bm

import java.util.UUID

case class User(id: Long = 0, uuid:UUID=gie.UUID.zero, name: String, password: String, email:String, active:Boolean=false)

case class UserPrivilege(id: UUID, userID: Long)

