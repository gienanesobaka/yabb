package gie.yabb

import java.util.UUID

import com.typesafe.scalalogging.StrictLogging
import gie.utils.prop.{WithMemo, PropsFromClassLoaderBundle, Configuration}
import gie.yabb.bm.{User, UserPrivilege}
import gie.yabb.db.Database
import gie.yabb.messages.{AuthenticationResponse, AuthenticationRequest}
import net.liftweb.http.SessionVar

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object app
  extends StrictLogging
  with MarshallerServiceTrait
  with RegistrationLogic
  with AuthenticationLogic{

  implicit object config extends Configuration( new PropsFromClassLoaderBundle("application.properties") with WithMemo )

  object currentUser extends SessionVar[SessionUserToken](null)

  val db = new Database(s"jdbc:h2:${implicitly[Configuration].get('db_path)};AUTO_SERVER=TRUE;TRACE_LEVEL_FILE=2")

  def boot(): Unit ={

  }

  def close(): Unit = {
    db.close()
  }


  object dao {

    import gie.yabb.db.SlickProfile.api._

    val dbAccessTimeout = 30.seconds

    def run[R](a: DBIOAction[R, NoStream, Nothing]): Future[R] = db.run(a)

    def selectUserWithPrivileges(name: String) = {
      Await.result(run{ db.selectUserWithPrivileges(name).transactionally}, dbAccessTimeout)
    }

    def enqueueUserRegistration(user: User) = {
      Await.result(run{ db.enqueueUserRegistration(user).transactionally}, dbAccessTimeout)
    }

    def activateUser(cookie: UUID) = {
      Await.result(run{ db.activateUser(cookie).transactionally}, dbAccessTimeout)
    }

  }

}