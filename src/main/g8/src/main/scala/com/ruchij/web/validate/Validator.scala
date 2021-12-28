package com.ruchij.web.validate

import cats.Applicative

trait Validator[F[_], -A] {
  def validate[B <: A](input: B): F[B]
}

object Validator {
  implicit def baseValidator[F[_]: Applicative, A]: Validator[F, A] =
    new Validator[F, A] {
      override def validate[B <: A](input: B): F[B] = Applicative[F].pure[B](input)
    }
}