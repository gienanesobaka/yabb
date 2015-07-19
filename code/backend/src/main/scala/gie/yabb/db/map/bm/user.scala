package gie.yabb.db.mappings

import java.util.UUID

import gie.yabb
import yabb.db.SlickProfile.{dbDefaultColumnNames, dbTypeNames}
import yabb.db.SlickProfile.api._


class User(tag: Tag) extends Table[yabb.bm.User](tag, "USER") {
  def id = column[Long](dbDefaultColumnNames.id, O.SqlType(dbTypeNames.identity), O.PrimaryKey, O.AutoInc)
  def uuid = column[UUID](dbDefaultColumnNames.uuid)
  def name = column[String]("NAME", O.SqlType(dbTypeNames.varchar(255)))
  def password = column[String]("PASSWORD", O.SqlType(dbTypeNames.varchar(255)))

  def idx_uuid = index("IDX_UUID", uuid, unique = true)
  def idx_name = index("IDX_NAME", name, unique = true)

  def * = (id, uuid, name, password) <> (yabb.bm.User.tupled, yabb.bm.User.unapply)
}

object User extends {
  val q = TableQuery[User]
}


class UserPrivilege(tag: Tag) extends Table[yabb.bm.UserPrivilege](tag, "USER_PRIVILEGE") {
  def id = column[UUID](dbDefaultColumnNames.id)
  def userId = column[Long]("USER_ID")

  def idx_id = index("IDX_ID", id, unique = false)
  def idx_userId = index("IDX_USERID", userId, unique = false)

  def fk_user = foreignKey("FK_USER", userId, User.q)(_.id)

  def * = (id, userId) <> (yabb.bm.UserPrivilege.tupled, yabb.bm.UserPrivilege.unapply)
}

object UserPrivilege extends {
  val q = TableQuery[UserPrivilege]
}
