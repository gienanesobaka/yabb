package gie.yabb

import biz.enef.angulate.{Controller, Scope}
import gie.yabb.authentication.AuthenticationService
import slogging.{Logger, LoggerHolder, LazyLogging}
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
case class AlertInfo(`type`: String, msg: String, timeout: Int)

@JSExportAll
class AlertsHolder() extends LazyLogging {
  val defaultTimeout = 6*60*1000
  val errorTimeOut = defaultTimeout

  val alerts = new Array[AlertInfo]()

  def all() = alerts

  def isAlerted(): Boolean = {
    alerts.length!=0
  }

  def close(index: Int): Unit ={
    alerts.remove(index)
  }

  def add(alertType: String, msg: String, timeout: Int = defaultTimeout): Unit ={
    alerts += AlertInfo(alertType, msg, timeout)
  }


  def clearAlerts(): Unit ={
    alerts.clear()
  }

  def addDanger(msg: String, timeout: Int): Unit ={
    add(AlertsTypes.danger, msg, timeout)
  }

  def addError(logger: => Logger)(msg: String, timeout: Int = errorTimeOut): Unit = {
    logger.error(msg)
    addDanger(msg, timeout)
  }

}
