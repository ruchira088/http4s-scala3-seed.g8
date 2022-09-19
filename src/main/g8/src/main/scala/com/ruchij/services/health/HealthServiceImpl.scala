package com.ruchij.services.health

import cats.effect.Sync
import cats.implicits.*
import com.ruchij.services.health.models.ServiceInformation
import com.ruchij.types.JodaClock

class HealthServiceImpl[F[_]: JodaClock: Sync] extends HealthService[F] {
  override val serviceInformation: F[ServiceInformation] =
    JodaClock[F].timestamp
      .flatMap(ServiceInformation.create[F])
}
