package com.ruchij.web.routes

import cats.effect.Sync
import cats.implicits.toFlatMapOps
import com.ruchij.circe.Encoders.dateTimeEncoder
import com.ruchij.services.health.HealthService
import io.circe.generic.auto.exportEncoder
import org.http4s.circe.CirceEntityEncoder.circeEntityEncoder
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object HealthRoutes {
  def apply[F[_]: Sync](healthService: HealthService[F])(implicit dsl: Http4sDsl[F]): HttpRoutes[F] = {
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "info" =>
        healthService.serviceInformation
          .flatMap(serviceInformation => Ok(serviceInformation))
    }
  }
}
