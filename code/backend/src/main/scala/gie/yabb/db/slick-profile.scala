package gie.yabb.db

import com.typesafe.scalalogging.LazyLogging
import slick.dbio.SuccessAction
import slick.driver.JdbcProfile
import slick.jdbc.StaticQuery
import slick.jdbc.meta.MTable

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext


object SlickProfile extends LazyLogging {

  val driver: JdbcProfile = slick.driver.H2Driver
  val api = driver.api

  def setSerializableForTransaction = {
    val dbAction = api.SimpleDBIO[Unit]{ctx=>
      val q = StaticQuery.updateNA("SET LOCK_MODE 1;")
      q.execute(ctx.session)
    }

    dbAction
  }

  def createIfNotExists(tablesDefs: api.TableQuery[_ <: api.Table[_]]*)(implicit executor: ExecutionContext) = {
    import api._

    val dbAction = api.SimpleDBIO[Option[driver.SchemaDescription]]{ctx=>

      val md = ctx.connection.getMetaData()
      val rs = md.getTables(null, null, "%", null)

      val avaliableTables = ArrayBuffer[String]()

      while (rs.next()) {
        avaliableTables += rs.getString("TABLE_NAME")
      }

      tablesDefs.filter({tableDef=>
        if (avaliableTables.contains(tableDef.baseTableRow.tableName)) {
          logger.info(s"Table '${tableDef.baseTableRow.tableName}' already exists.")
          false
        } else {
          logger.info(s"Will creating table '${tableDef.baseTableRow.tableName}'.")
          true
        }
      }).map( _.schema).reduceLeftOption(_ ++ _)
    }

    dbAction.flatMap{schemaOpt=>

      if(schemaOpt.isDefined){
        DBIO.seq(schemaOpt.get.create)
      } else {
        DBIO.successful(())
      }

    }

  }



  object dbTypeNames {
    val identity = "IDENTITY"
    def varchar(length: Int) = {
      assume(length>0)
      s"VARCHAR(${length})"
    }
  }

  object dbDefaultColumnNames {
    val id = "ID"
    val uuid = "UUID"
  }

}

