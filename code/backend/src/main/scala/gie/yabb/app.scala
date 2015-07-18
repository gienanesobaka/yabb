package gie.yabb

import gie.utils.prop.{WithMemo, PropsFromClassLoaderBundle, Configuration}
import gie.yabb.db.Database
import gie.yabb.messages.{AuthenticationResponse, AuthenticationRequest}

import scala.concurrent.ExecutionContext.Implicits.global

object app {

  implicit object config extends Configuration( new PropsFromClassLoaderBundle("application.properties") with WithMemo )

  val db = new Database(s"jdbc:h2:${implicitly[Configuration].get('db_path)};AUTO_SERVER=TRUE;TRACE_LEVEL_FILE=2")

  def boot(): Unit ={

  }

  def close(): Unit = {
    db.close()
  }

  def authenticate(request: AuthenticationRequest): AuthenticationResponse = {

    AuthenticationResponse(true)
  }

}