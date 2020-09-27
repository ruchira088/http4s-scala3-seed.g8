package com.ruchij.web.middleware

import cats.arrow.FunctionK
import cats.data.Kleisli
import cats.effect.Sync
import cats.implicits._
import com.ruchij.exceptions.ResourceNotFoundException
import com.ruchij.types.FunctionKTypes
import com.ruchij.web.responses.ErrorResponse
import org.http4s.dsl.impl.EntityResponseGenerator
import org.http4s.{HttpApp, Request, Response, Status}

object ExceptionHandler {
  def apply[F[_]: Sync](httpApp: HttpApp[F]): HttpApp[F] =
    Kleisli[F, Request[F], Response[F]] { request =>
      Sync[F].handleErrorWith(httpApp.run(request)) { throwable =>
        entityResponseGenerator[F](throwable)(throwableResponseBody(throwable))
          .map(errorResponseMapper(throwable))
      }
    }

  val throwableStatusMapper: Throwable => Status = {
    case _: ResourceNotFoundException => Status.NotFound

    case _ => Status.InternalServerError
  }

  val throwableResponseBody: Throwable => ErrorResponse = throwable => ErrorResponse(List(throwable))

  def errorResponseMapper[F[_]](throwable: Throwable)(response: Response[F]): Response[F] =
    throwable match {
      case _ => response
    }

  def entityResponseGenerator[F[_]](throwable: Throwable): EntityResponseGenerator[F, F] =
    new EntityResponseGenerator[F, F] {
      override def status: Status = throwableStatusMapper(throwable)

      override def liftG: FunctionK[F, F] = FunctionKTypes.identityFunctionK[F]
    }
}
