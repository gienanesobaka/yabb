package gie.yabb.db

import com.typesafe.scalalogging.StrictLogging
import com.zaxxer.hikari.{HikariDataSource, HikariConfig}

import scala.concurrent.{ExecutionContext, Await}
import scala.concurrent.duration._

class Database(connectionURL: String)(implicit executor: ExecutionContext) extends StrictLogging {

  logger.debug(s"DataSource: ${connectionURL}")

  import SlickProfile.api._

  val db = {
    val hConf = new HikariConfig()
    hConf.setJdbcUrl(connectionURL)
    val dataSource = new HikariDataSource(hConf)

    Database.forDataSource(dataSource)
  }

  // ctor
  ctor()
  //

  def close(): Unit ={
    db.close()
  }

  def ctor(): Unit ={
    val createTablesAction = SlickProfile.createIfNotExists(map.bm.User.q)
    Await.result( db.run(DBIO.seq(SlickProfile.setSerializableForTransaction, createTablesAction).transactionally), 30.seconds)
  }


}