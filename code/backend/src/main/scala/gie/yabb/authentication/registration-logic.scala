package gie.yabb

import com.typesafe.scalalogging.StrictLogging
import gie.yabb.bm.User
import gie.yabb.db.DbException
import gie.yabb.messages._


class LoginExistsException() extends DbException


trait RegistrationLogic {

  class RegistrationImpl extends StrictLogging {
    
    def register(request: RegistrationRequest): RegistrationResponse ={
      logger.debug(s"GOT REGISTRATION REQUEST: ${request}")

      val user = User(
        name = request.authentication.login,
        password = request.authentication.password,
        email = request.email)

      try {
        app.dao.enqueueUserRegistration(user) match {
          case (user, registrationInfo)=>
            logger.debug(s"Enqueued new registration: ${user}, info: ${registrationInfo}")
            RegistrationResponse(Right(RegistrationResponseSuccess(registrationInfo.activationMagic)))
        }
      } catch {
        case e:DbException=>
          logger.info(s"failure while processing db request: ${e.toString}")
          RegistrationResponse(Left(RegistrationResponseFailure(RegistrationStatusCodes.invalidLogin, "Invalid login")))
        case e:Exception=>
          logger.error(s"unknown failure while processing db request: ${e.toString}")
          RegistrationResponse(Left(RegistrationResponseFailure(RegistrationStatusCodes.unknownFailure, "FAILED!")))
      }

    }

  }


  val registration = new RegistrationImpl()


}