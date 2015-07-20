package gie.yabb.db.mappings

import java.util.UUID

import gie.yabb
import yabb.db.SlickProfile.{dbDefaultColumnNames, dbTypeNames}
import yabb.db.SlickProfile.api._

class RegistrationInfo(tag: Tag) extends Table[yabb.RegistrationInfo](tag, "REGISTRATION_INFO") {
  def id = column[Long](dbDefaultColumnNames.id, O.SqlType(dbTypeNames.identity), O.PrimaryKey, O.AutoInc)
  def userId = column[Long]("USER_ID")

  def activationMagic = column[UUID]("ACTIVATION_MAGIC")

  def * = (id, userId,activationMagic) <> (yabb.RegistrationInfo.tupled, yabb.RegistrationInfo.unapply)

  def fk_user = foreignKey(s"FK_${tableName}_USER", userId, User.q)(_.id)
}

object RegistrationInfo extends {
  val q = TableQuery[RegistrationInfo]
}
