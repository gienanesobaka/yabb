package gie.yabb.db.mappings


import gie.yabb
import yabb.db.SlickProfile.{dbDefaultColumnNames, dbTypeNames}
import yabb.db.SlickProfile.api._

class Category(tag: Tag) extends Table[yabb.Category](tag, "CATEGORY") {
  def id = column[Long](dbDefaultColumnNames.id, O.SqlType(dbTypeNames.identity), O.PrimaryKey, O.AutoInc)
  def parentId= column[Option[Long]]("PARENT_ID")
  def name = column[String]("NAME")

  def * = (id, parentId, name) <> (yabb.Category.tupled, yabb.Category.unapply)

  def index_parentId = index(s"INDEX_${tableName}_PARENT_ID", parentId, unique = false)

}

object Category extends {
  val q = TableQuery[Category]
}

