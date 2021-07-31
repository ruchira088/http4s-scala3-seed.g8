package com.ruchij.services.health

import cats.effect.{Clock, Sync}
import cats.implicits._
import com.ruchij.config.BuildInformation
import com.ruchij.services.health.models.ServiceInformation
import com.ruchij.types.JodaClock

class HealthServiceImpl[F[_]: Clock: Sync](buildInformation: BuildInformation) extends HealthService[F] {
  override val serviceInformation: F[ServiceInformation] =
    JodaClock[F].timestamp
      .flatMap(timestamp => ServiceInformation.create(timestamp, buildInformation))
}