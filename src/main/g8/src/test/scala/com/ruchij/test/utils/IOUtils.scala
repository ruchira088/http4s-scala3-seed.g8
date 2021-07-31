package com.ruchij.test.utils

import cats.effect.IO

object IOUtils {

  def runIO[A](block: => IO[A]): A = block.unsafeRunSync()

  implicit class IOErrorOps(value: IO[_]) {
    val error: IO[Throwable] =
      value.attempt.flatMap {
        case Left(throwable) => IO.pure(throwable)
        case Right(success) => IO.raiseError(new IllegalStateException(s"Expected an exception, but returned \$success"))
      }
  }

}
