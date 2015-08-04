package gie.yabb

import java.util.UUID

case class Category(id: Long=0, parentId: Option[Long], name: String)


object CategoryMagic {

  object command {
    val noop   = 0
    val create = 1
    val rename = 2
    val delete = 3
  }

}




case class CategoryCommand(command: Int, nodeId: Option[Long] = None, parentId:Option[Either[UUID, Long]] = None, name: Option[String] = None)



