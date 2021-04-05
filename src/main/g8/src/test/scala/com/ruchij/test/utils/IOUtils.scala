package com.ruchij.test.utils

import cats.effect.IO

object IOUtils {

  def runIO[A](block: => IO[A]): A = block.unsafeRunSync()

}
