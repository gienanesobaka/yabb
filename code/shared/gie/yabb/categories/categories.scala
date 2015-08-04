package gie.yabb

import java.util.UUID

case class Category(id: Long=0, parentId: Option[Long], name: String)


object CategoryMagic {
  val catIdDirty = -1
  val catIdNew = -2

  object command {
    val noop   = 0
    val create = 1
    val rename = 2
    val delete = 3
  }

}




case class CategoryCommand(command: Int, nodeId: Option[Long] = None, name: Option[String] = None)



