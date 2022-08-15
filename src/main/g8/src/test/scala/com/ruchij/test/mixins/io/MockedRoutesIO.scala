package com.ruchij.test.mixins.io

import cats.effect.IO
import cats.effect.kernel.Sync
import com.ruchij.test.mixins.MockedRoutes
import org.scalatest.Suite

trait MockedRoutesIO extends MockedRoutes[IO] { self: Suite =>
  override val sync: Sync[IO] = IO.asyncForIO
}
