package gie.yabb.db.map.bm

import gie.yabb
import yabb.db.SlickProfile.{dbDefaultColumnNames, dbTypeNames}
import yabb.db.SlickProfile.api._


class User(tag: Tag) extends Table[yabb.bm.User](tag, "USER") {
  def id = column[Long](dbDefaultColumnNames.id, O.SqlType(dbTypeNames.identity), O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME", O.SqlType(dbTypeNames.varchar(255)))
  def password = column[String]("PASSWORD", O.SqlType(dbTypeNames.varchar(255)))

  def * = (id, name, password) <> (yabb.bm.User.tupled, yabb.bm.User.unapply)
}

object tableUser extends {
  val q = TableQuery[User]
}