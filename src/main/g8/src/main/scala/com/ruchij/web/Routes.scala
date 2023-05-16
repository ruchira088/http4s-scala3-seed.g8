package com.ruchij.web

import cats.effect.Sync
import com.ruchij.services.health.HealthService
import com.ruchij.web.middleware.{ExceptionHandler, NotFoundHandler}
import com.ruchij.web.routes.ServiceRoutes
import fs2.compression.Compression
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.server.middleware.GZip
import org.http4s.{HttpApp, HttpRoutes}

object Routes {
  def apply[F[_]: Sync: Compression](healthService: HealthService[F]): HttpApp[F] = {
    given Http4sDsl[F] = new Http4sDsl[F] {}

    val routes: HttpRoutes[F] =
      Router(
        "/service" -> ServiceRoutes(healthService)
      )

    GZip {
      ExceptionHandler {
        NotFoundHandler(routes)
      }
    }
  }
}
