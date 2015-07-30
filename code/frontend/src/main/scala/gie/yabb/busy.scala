package gie.yabb

import scala.scalajs.js.annotation.JSExport

class BusyHolder {
  var m_busyCounter = 0

  def incBusy(): Unit ={
    m_busyCounter += 1
  }

  def decBusy(): Unit ={
    assume(m_busyCounter>0)
    m_busyCounter -=1
  }

  @JSExport
  def isBusy():Boolean = m_busyCounter!=0

}