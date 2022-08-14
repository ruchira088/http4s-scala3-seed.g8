package com.ruchij.types

import cats.{Applicative, ApplicativeError, ~>}

object FunctionKTypes {

  implicit class FunctionK2TypeOps[F[+_, +_], A, B](value: F[B, A]) {
    def toType[G[_], C >: B](using functionK: ~>[[X] =>> F[C, X], G]): G[A] = functionK(value)
  }

  implicit def eitherToF[L, F[_]: [G[_]] =>> ApplicativeError[G, L]]: ~>[[A] =>> Either[L, A], F] =
    new ~>[[A] =>> Either[L, A], F] {
      override def apply[A](either: Either[L, A]): F[A] =
        either.fold(ApplicativeError[F, L].raiseError, Applicative[F].pure)
    }

}
