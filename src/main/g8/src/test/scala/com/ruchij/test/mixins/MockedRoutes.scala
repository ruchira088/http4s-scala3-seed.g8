package com.ruchij.test.mixins

import cats.effect.kernel.Sync
import com.ruchij.services.health.HealthService
import com.ruchij.web.Routes
import org.http4s.HttpApp
import org.scalamock.scalatest.MockFactory
import org.scalatest.OneInstancePerTest

trait MockedRoutes[F[_]] extends MockFactory with OneInstancePerTest {

  val healthService: HealthService[F] = mock[HealthService[F]]

  val sync: Sync[F]

  def createRoutes(): HttpApp[F] =
    Routes[F](healthService)(sync)

}
