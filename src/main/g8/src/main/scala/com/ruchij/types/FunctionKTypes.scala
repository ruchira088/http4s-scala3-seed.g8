package com.ruchij.types

import cats.{Applicative, ApplicativeError, Functor, ~>}

object FunctionKTypes {

  extension [F[_, _], A, B] (value: F[B, A]) {
    def toType[G[_], C >: B](using functionK: ~>[[X] =>> F[C, X], G], functor: Functor[[X] =>> F[X, A]]): G[A] =
      functionK(functor.map(value)(identity[C]))
  }

  given [R]: Functor[[X] =>> Either[X, R]] =
    new Functor[[X] =>> Either[X, R]] {
      override def map[A, B](fa: Either[A, R])(f: A => B): Either[B, R] = fa.left.map(f)
    }

  given [L, F[_]: [G[_]] =>> ApplicativeError[G, L]]: ~>[[A] =>> Either[L, A], F] =
    new ~>[[A] =>> Either[L, A], F] {
      override def apply[A](either: Either[L, A]): F[A] =
        either.fold(ApplicativeError[F, L].raiseError, Applicative[F].pure)
    }

}
