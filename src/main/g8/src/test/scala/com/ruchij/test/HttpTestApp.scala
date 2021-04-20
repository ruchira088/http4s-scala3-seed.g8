package com.ruchij.test

import cats.Applicative
import cats.effect.{Clock, Sync}
import com.ruchij.config.BuildInformation
import com.ruchij.services.health.HealthServiceImpl
import com.ruchij.web.Routes
import org.http4s.HttpApp

object HttpTestApp {
  val BuildInfo: BuildInformation = BuildInformation(Some("test-branch"), Some("my-commit"), None)

  def create[F[_]: Sync: Clock]: F[HttpApp[F]] =
    Applicative[F].pure {
      Routes(new HealthServiceImpl[F](BuildInfo))
    }
}
