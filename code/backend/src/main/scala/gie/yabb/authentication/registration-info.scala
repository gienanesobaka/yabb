package gie.yabb

import java.util.UUID


case class RegistrationInfo(id: Long = 0, userId: Long, activationMagic: UUID)