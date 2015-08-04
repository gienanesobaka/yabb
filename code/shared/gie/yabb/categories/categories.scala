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

case class CategoryCommand(command: Int, nodeId: Either[UUID, Long], parentId:Option[Either[UUID, Long]], name: Option[String] = None)



