package gie.yabb

import java.util.UUID


case class RegistrationInfo(id: Long, userId: Long, activationMagic: UUID)