package gie.yabb.db

import java.util.UUID

import com.typesafe.scalalogging.StrictLogging
import com.zaxxer.hikari.{HikariDataSource, HikariConfig}
import gie.security.privileges.PRIV_ROOT
import gie.yabb.bm.{User, UserPrivilege}
import slick.dbio.{NoStream}

import scala.concurrent.{Future, ExecutionContext, Await}
import scala.concurrent.duration._
import slick.dbio

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

  def run[R](a: DBIOAction[R, NoStream, Nothing]): Future[R] = db.run(a)
  
  private def createAdminAccount() = {

    (for( priv <- mappings.UserPrivilege.q  if priv.id === PRIV_ROOT.id;
          user <- mappings.User.q           if user.id === priv.userId) yield user).result.headOption.flatMap { vOpt =>
      vOpt.fold {
        logger.info("No admin account found. will create new")
        val user = User(0, gie.UUID(), "admin", "admin", "admin@admin.local", active = true)

        (mappings.User.q.returning{ mappings.User.q.map(_.id) } += user)
          .flatMap{ insertedUserId:Long => (mappings.UserPrivilege.q += UserPrivilege(PRIV_ROOT.id, insertedUserId)).flatMap{ _:Int=> DBIO.successful(insertedUserId)} }
          .flatMap{ insertedUserId:Long => DBIO.successful(user.copy(id=insertedUserId))}

      } { v =>
        logger.info(s"Already have account with admin rights: ${v}")
        DBIO.successful(v)
      }
    }

  }

  def ctor(): Unit ={
    val createTablesAction = SlickProfile.createIfNotExists(mappings.User.q, mappings.UserPrivilege.q, mappings.RegistrationInfo.q)
    Await.result( db.run(DBIO.seq(SlickProfile.setSerializableForTransaction, createTablesAction, createAdminAccount()).transactionally), 30.seconds)
  }


  def selectUserWithPrivileges(name: String) = {

    mappings.User.q.filter(u=> (u.name === name) && (u.active===true)).result.headOption.flatMap{ userOpt=>

      userOpt.map(user=> mappings.UserPrivilege.q.filter(_.userId === user.id).result.map( (user,_)  ) ) match {
        case Some( userInfoAction ) => userInfoAction.map( Some(_) )
        case None                   => DBIO.successful(None)
      }
    }
 }


}