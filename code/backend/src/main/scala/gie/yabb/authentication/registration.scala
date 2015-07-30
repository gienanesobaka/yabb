package gie.yabb

import com.typesafe.scalalogging.StrictLogging
import gie.yabb.messages.{RegistrationStatusCodes, RegistrationResponseFailure, RegistrationResponse, RegistrationRequest}


trait RegistrationLogic {

  class RegistrationImpl extends StrictLogging {
    
    def register(request: RegistrationRequest): RegistrationResponse ={
      logger.debug(s"GOT REGISTRATION REQUEST: ${request}")


      RegistrationResponse(Left(RegistrationResponseFailure(RegistrationStatusCodes.unknownFailure, "FAILED!")))
    }

  }


  val registration = new RegistrationImpl()


}