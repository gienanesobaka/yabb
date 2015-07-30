package gie.yabb

import biz.enef.angulate.{Controller, Scope}
import gie.yabb.authentication.AuthenticationService
import slogging.{LoggerHolder, LazyLogging}
import scala.collection.mutable.ArrayBuffer
import scala.scalajs.js.Array
import scala.scalajs.js.annotation.{JSExportAll, JSName, JSExport}
import scalajs.js


object AlertsTypes {
  val danger = "danger"
  val success = "success"
  val info = "info"
  val warning = "warning"
}


@JSExport
@JSExportAll
case class AlertInfo(`type`: String, msg: String, timeout: Int=6*60*1000)

@JSExportAll
class AlertsHolder() extends LazyLogging {
  val alerts = new Array[AlertInfo]()

  def all() = alerts

  def isAlerted(): Boolean = {
    alerts.length!=0
  }

  def close(index: Int): Unit ={
    alerts.remove(index)
  }

  def add(alertType: String, msg: String): Unit ={
    alerts += AlertInfo(alertType, msg)
  }

  def replaceAll(alertType: String, msg: String): Unit ={
    clearAlerts()
    add(alertType, msg)
  }

  def clearAlerts(): Unit ={
    alerts.clear()
  }

}
