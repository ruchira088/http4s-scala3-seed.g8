package com.ruchij.web

import cats.MonadThrow
import cats.implicits._
import com.ruchij.web.validate.Validator
import org.http4s.{EntityDecoder, Request}

package object requests {

  implicit class RequestOps[F[_]](request: Request[F]) {
    def to[A](implicit validator: Validator[F, A], entityDecoder: EntityDecoder[F, A], monadThrow: MonadThrow[F]): F[A] =
      request.as[A].flatMap(validator.validate)
  }

}
