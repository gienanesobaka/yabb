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
  Await.result( db.run(DBIO.seq(SlickProfile.setSerializableForTransaction, SlickProfile.createIfNotExists(map.bm.tableUser.q))), 30.seconds)
  //

  def close(): Unit ={
    db.close()
  }

  def DDL() =  {
    val appSchema = map.bm.tableUser.q

    //DBIO.seq(appSchema.create)

    appSchema
  }

}