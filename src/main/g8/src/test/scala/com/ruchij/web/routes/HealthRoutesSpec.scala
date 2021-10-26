package com.ruchij.web.routes

import cats.effect.IO
import com.ruchij.services.health.models.ServiceInformation
import com.ruchij.test.matchers._
import com.ruchij.test.mixins.io.MockedRoutesIO
import com.ruchij.test.utils.IOUtils.runIO
import io.circe.literal._
import org.http4s.Method.GET
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.{Request, Status}
import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

class HealthRoutesSpec extends AnyFlatSpec with Matchers with MockedRoutesIO {

  "GET /service/info" should "return a successful response containing service information" in runIO {

    val expectedJsonResponse =
      json"""{
        "serviceName": "my-http4s-project",
        "serviceVersion": "1.0.0",
        "organization": "com.ruchij",
        "scalaVersion": "2.13.6",
        "sbtVersion": "1.5.5",
        "javaVersion": "11.0.12",
        "gitBranch" : "my-branch",
        "gitCommit" : "my-commit",
        "buildTimestamp" : null,
        "timestamp": "2021-08-04T10:10:00.000Z"
      }"""

    for {
      _ <- IO.delay {
        (() => healthService.serviceInformation).expects()
          .returns {
            IO.pure {
              ServiceInformation(
                "my-http4s-project",
                "1.0.0",
                "com.ruchij",
                "2.13.6",
                "1.5.5",
                "11.0.12",
                Some("my-branch"),
                Some("my-commit"),
                None,
                new DateTime(2021, 8, 4, 10, 10, 0, 0, DateTimeZone.UTC)
              )
            }
          }
      }

      request = Request[IO](GET, uri"/service/info")

      routes = createRoutes()

      response <- routes.run(request)

      _ <- IO.delay {
        response must beJsonContentType
        response must haveJson(expectedJsonResponse)
        response must haveStatus(Status.Ok)
      }
    }
    yield (): Unit
  }
}