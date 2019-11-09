package com.ruchij.web.middleware

import cats.data.Kleisli
import cats.effect.Sync
import com.ruchij.exceptions.ResourceNotFoundException
import com.ruchij.web.responses.ErrorResponse
import org.http4s.dsl.impl.EntityResponseGenerator
import org.http4s.{HttpApp, Request, Response, Status}

import scala.language.higherKinds

object ExceptionHandler {
  def apply[F[_]: Sync](httpApp: HttpApp[F]): HttpApp[F] =
    Kleisli[F, Request[F], Response[F]] {
      request =>
        Sync[F].handleErrorWith(httpApp.run(request)) { throwable =>
          entityResponseGenerator(throwable)(errorResponseBody(throwable))
        }
    }

  def entityResponseGenerator[F[_]](throwable: Throwable): EntityResponseGenerator[F, F] =
    new EntityResponseGenerator[F, F] {
      override def status: Status =
        throwable match {
          case _: ResourceNotFoundException => Status.NotFound

          case _ => Status.InternalServerError
        }
    }

  def errorResponseBody(throwable: Throwable): ErrorResponse =
    throwable match {
      case _ => ErrorResponse(List(throwable))
    }
}
