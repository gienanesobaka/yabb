package gie.yabb.messages

import java.util.UUID

case class RegistrationRequest( authentication:AuthenticationRequest,
                                firstName: String,
                                lastName: String,
                                email:String)


object RegistrationStatusCodes {
  type Type = Int

  val ok = 1
  val unknownFailure = 2
  val invalidLogin = 3

}

case class RegistrationResponseSuccess(confirmationCookie:UUID)
case class RegistrationResponseFailure(status:RegistrationStatusCodes.Type, additionalInformation: String)

case class RegistrationResponse(status: Either[RegistrationResponseFailure, RegistrationResponseSuccess])