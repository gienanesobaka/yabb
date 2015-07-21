package gie.yabb.messages

object magic {
  val magicValue = 41
}

case class WireHeader(magic:Int)
case class WireServerHeader()