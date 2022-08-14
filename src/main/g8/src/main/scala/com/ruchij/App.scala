package com.ruchij

import cats.effect.{ExitCode, IO, IOApp}
import com.ruchij.config.ServiceConfiguration
import com.ruchij.services.health.HealthServiceImpl
import com.ruchij.web.Routes
import org.http4s.ember.server.EmberServerBuilder
import pureconfig.ConfigSource

object App extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      configObjectSource <- IO.delay(ConfigSource.defaultApplication)
      serviceConfiguration <- ServiceConfiguration.parse[IO](configObjectSource)

      healthService = new HealthServiceImpl[IO](serviceConfiguration.buildInformation)

      exitCode <- EmberServerBuilder
        .default[IO]
        .withHttpApp(Routes(healthService))
        .withHost(serviceConfiguration.httpConfiguration.host)
        .withPort(serviceConfiguration.httpConfiguration.port)
        .build
        .use(_ => IO.never)
        .as(ExitCode.Success)
    } yield exitCode
}
