package com.ruchij.web.validate

import cats.Applicative

trait Validator[F[_], -A] {
  def validate[B <: A](input: B): F[B]
}

object Validator {
  given [F[_]: Applicative, A]: Validator[F, A] with {
    override def validate[B <: A](input: B): F[B] = Applicative[F].pure[B](input)
  }
}