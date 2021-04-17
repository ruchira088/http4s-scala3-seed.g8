package com.ruchij.services.health

import java.util.concurrent.TimeUnit

import cats.effect.{Clock, Sync}
import cats.implicits._
import com.ruchij.config.BuildInformation
import com.ruchij.services.health.models.ServiceInformation
import org.joda.time.DateTime

class HealthServiceImpl[F[_]: Clock: Sync](buildInformation: BuildInformation) extends HealthService[F] {
  override val serviceInformation: F[ServiceInformation] =
    Clock[F].realTime(TimeUnit.MILLISECONDS)
      .flatMap(timestamp => ServiceInformation.create(new DateTime(timestamp), buildInformation))
}

