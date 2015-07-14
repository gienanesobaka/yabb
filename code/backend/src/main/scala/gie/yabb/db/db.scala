package gie.yabb.db

import com.typesafe.scalalogging.StrictLogging
import com.zaxxer.hikari.{HikariDataSource, HikariConfig}
import slick.driver.JdbcProfile
import slick.jdbc.StaticQuery

import scala.concurrent.Await
import scala.concurrent.duration._


class Database(connectionURL: String) extends StrictLogging {

  logger.debug(s"DataSource: ${connectionURL}")

  val driver: JdbcProfile = slick.driver.H2Driver
  val api = driver.api


  val db = {
    val hConf = new HikariConfig()
    hConf.setJdbcUrl(connectionURL)
    val dataSource = new HikariDataSource(hConf)

    api.Database.forDataSource(dataSource)
  }

  // ctor
  setSerializableForTransaction()
  //

  private def setSerializableForTransaction() = {
    val dbAction = api.SimpleDBIO[Unit]{ctx=>
      val q = StaticQuery.updateNA("SET LOCK_MODE 1;")
      q.execute(ctx.session)
    }

    val result = db.run(dbAction)
    Await.result(result, 10.seconds)
  }

  def close(): Unit ={
    db.close()
  }

}