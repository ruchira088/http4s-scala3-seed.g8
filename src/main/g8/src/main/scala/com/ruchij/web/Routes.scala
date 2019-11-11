package com.ruchij.web

import cats.data.Kleisli
import cats.effect.{Clock, Sync}
import com.ruchij.exceptions.ResourceNotFoundException
import com.ruchij.services.health.HealthService
import com.ruchij.web.middleware.ExceptionHandler
import com.ruchij.web.routes.ServiceRoutes
import org.http4s.{HttpApp, HttpRoutes, Request, Response}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

import scala.language.higherKinds

object Routes {
  def apply[F[_]: Sync: Clock](healthService: HealthService[F]): HttpApp[F] = {
    implicit val dsl: Http4sDsl[F] = new Http4sDsl[F] {}

    val routes: HttpRoutes[F] =
      Router(
        "/service" -> ServiceRoutes(healthService)
      )

    ExceptionHandler {
      Kleisli[F, Request[F], Response[F]] {
        request =>
          routes.run(request).getOrElseF {
            Sync[F].raiseError(ResourceNotFoundException(s"Endpoint not found: request.method{request.uri}"))
          }
      }
    }
  }
}
