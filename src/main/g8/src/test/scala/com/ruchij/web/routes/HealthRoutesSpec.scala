package com.ruchij.web.routes

import cats.effect.{Clock, IO}
import com.eed3si9n.ruchij.BuildInfo
import com.ruchij.circe.Encoders.dateTimeEncoder
import com.ruchij.test.HttpTestApp
import com.ruchij.test.utils.Providers.stubClock
import com.ruchij.test.utils.IOUtils.runIO
import com.ruchij.test.matchers._
import io.circe.literal._
import org.http4s.Method.GET
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.{Request, Status}
import org.joda.time.DateTime
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

import java.util.concurrent.TimeUnit
import scala.util.Properties

class HealthRoutesSpec extends AnyFlatSpec with Matchers {
  "GET /service/info" should "return a successful response containing service information" in runIO {
    for {
      timestamp <-
        Clock.create[IO].realTime(TimeUnit.MILLISECONDS)
          .map(milliseconds => new DateTime(milliseconds))

      httpApplication <- {
        implicit val clock: Clock[IO] = stubClock[IO](timestamp)

        HttpTestApp.create[IO]
      }

      request = Request[IO](GET, uri"/service/info")

      response <- httpApplication.run(request)

      _ = {
        val expectedJsonResponse =
          json"""{
            "serviceName": "my-http4s-project",
            "serviceVersion": \${BuildInfo.version},
            "organization": "com.ruchij",
            "scalaVersion": \${BuildInfo.scalaVersion},
            "sbtVersion": \${BuildInfo.sbtVersion},
            "javaVersion": \${Properties.javaVersion},
            "gitBranch" : "test-branch",
            "gitCommit" : "my-commit",
            "buildTimestamp" : null,
            "timestamp": \$timestamp
          }"""

        response must beJsonContentType
        response must haveJson(expectedJsonResponse)
        response must haveStatus(Status.Ok)
      }
    }
    yield (): Unit
  }
}