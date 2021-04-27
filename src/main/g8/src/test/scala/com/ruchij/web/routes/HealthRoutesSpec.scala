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

import scala.util.Properties

class HealthRoutesSpec extends AnyFlatSpec with Matchers {
  "GET /service/info" should "return a successful response containing service information" in runIO {
    val dateTime = DateTime.now()
    implicit val clock: Clock[IO] = stubClock[IO](dateTime)

    val expectedJsonResponse =
      json"""{
        "serviceName": "$name;format="normalize"$",
        "serviceVersion": \${BuildInfo.version},
        "organization": "com.ruchij",
        "scalaVersion": "2.13.5",
        "sbtVersion": "1.5.1",
        "javaVersion": \${Properties.javaVersion},
        "gitBranch" : "test-branch",
        "gitCommit" : "my-commit",
        "buildTimestamp" : null,
        "timestamp": \$dateTime
      }"""

    for {
      httpApplication <- HttpTestApp.create[IO]

      request = Request[IO](GET, uri"/service/info")

      response <- httpApplication.run(request)

      _ = {
        response must beJsonContentType
        response must haveJson(expectedJsonResponse)
        response must haveStatus(Status.Ok)
      }
    }
    yield (): Unit
  }
}