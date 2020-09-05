package com.ruchij.test

import cats.effect.{Clock, Sync}
import com.ruchij.config.BuildInformation
import com.ruchij.services.health.HealthServiceImpl
import com.ruchij.web.Routes
import org.http4s.HttpApp

object HttpTestApp {
  def apply[F[_]: Sync: Clock](): HttpApp[F] = {
    val buildInformation =
      BuildInformation(Some("test-branch"), Some("my-commit"), None)

    Routes(new HealthServiceImpl[F](buildInformation))
  }
}
