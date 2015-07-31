package gie.yabb.db

import java.util.UUID

import com.typesafe.scalalogging.StrictLogging
import com.zaxxer.hikari.{HikariDataSource, HikariConfig}
import gie.security.privileges.PRIV_ROOT
import gie.yabb.{RegistrationInfo, LoginExistsException}
import gie.yabb.bm.{User, UserPrivilege}
import slick.dbio.{NoStream}

import scala.concurrent.{Future, ExecutionContext, Await}
import scala.concurrent.duration._
import slick.dbio


class DbException() extends Exception

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


  def insertUser(user: User) = {
    (mappings.User.q.returning{ mappings.User.q.map(_.id) } += user).map{dbId => user.copy(id=dbId) }
  }

  def insertRegistrationInfo(userId: Long) = {
    val ri = RegistrationInfo(userId=userId, activationMagic=gie.UUID())
    (mappings.RegistrationInfo.q.returning{ mappings.RegistrationInfo.q.map(_.id) } += ri).map{dbId => ri.copy(id=dbId) }
  }

  def enqueueUserRegistration(userP: User) = {
    val user = userP.copy(uuid = gie.UUID(), active=false)

    mappings.User.q.filter(_.name===user.name).exists.result.flatMap {

      case true   =>
        logger.debug(s"trying to register already defined login: ${user.name}")
        DBIO.failed(new LoginExistsException())

      case false  =>
        insertUser(user).flatMap{dbUser=>
          insertRegistrationInfo(dbUser.id).flatMap{ dbRegistrationInfo=>
            DBIO.successful((dbUser, dbRegistrationInfo))
          }
        }

    }

  }


  def getRegistrationInfo(cookie: UUID) = {
    mappings.RegistrationInfo.q.filter(_.activationMagic===cookie).result.headOption
  }

  def setActiveForUser(id:Long, state: Boolean) = {
    mappings.User.q.filter(_.id===id).map(_.active).update(state)
  }

  def deleteRegistrationInfo(id: Long) = {
    mappings.RegistrationInfo.q.filter(_.id===id).delete
  }

  def activateUser(cookie: UUID) = {
    type TTT = DBIOAction[Boolean, NoStream, Effect.Write]
    getRegistrationInfo(cookie).flatMap{ _.fold[TTT]{DBIO.successful(false)}{ activationInfo=>
      setActiveForUser(activationInfo.userId, true).flatMap{c=>assume(c==1)
        deleteRegistrationInfo(activationInfo.id).flatMap{c=>assume(c==1);
          logger.info(s"Activated user with id: ${activationInfo.userId}")
          DBIO.successful(true)}
      }
    }}
  }


}