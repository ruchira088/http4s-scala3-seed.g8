package com.ruchij.types

import cats.{Applicative, ApplicativeError, ~>}

object FunctionKTypes {

  implicit class FunctionK2TypeOps[F[+_, +_], A, B](value: F[B, A]) {
    def toType[G[_], C >: B](implicit functionK: F[C, *] ~> G): G[A] = functionK(value)
  }

  implicit def eitherToF[L, F[_]: ApplicativeError[*[_], L]]: Either[L, *] ~> F =
    new ~>[Either[L, *], F] {
      override def apply[A](either: Either[L, A]): F[A] =
        either.fold(ApplicativeError[F, L].raiseError, Applicative[F].pure)
    }

}
