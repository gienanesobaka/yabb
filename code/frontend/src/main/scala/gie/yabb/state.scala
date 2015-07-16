package gie.yabb


//trait StateDefinition {
//  val parentState:StateDefinition
//  val name:String
//  val partsDirectory:String
//}

object StateHelpers {
  def getStateName(parentName: String, myName:String) = {
    assume(!myName.isEmpty)
    if( (parentName eq null) || parentName.isEmpty) myName else s"${parentName}.${myName}"
  }

  def getPartsDirectory(parent:String, child:String) = {
    assume(!child.isEmpty)
    if( (parent eq null) || parent.isEmpty) child else s"${parent}/${child}"

  }
}