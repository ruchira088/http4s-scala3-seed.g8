package com.ruchij.test.mixins.io

import cats.effect.IO
import cats.effect.kernel.Sync
import com.ruchij.test.mixins.MockedRoutes

trait MockedRoutesIO extends MockedRoutes[IO] {
  override val sync: Sync[IO] = IO.asyncForIO
}
