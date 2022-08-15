package com.ruchij.web.routes

import cats.effect.IO
import com.ruchij.services.health.models.ServiceInformation
import com.ruchij.test.matchers.*
import com.ruchij.test.mixins.io.MockedRoutesIO
import com.ruchij.test.utils.IOUtils.runIO
import io.circe.literal.*
import org.http4s.Method.GET
import org.http4s.syntax.all.uri
import org.http4s.{Request, Status}
import org.joda.time.{DateTime, DateTimeZone}
import org.mockito.Mockito.when
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

class HealthRoutesSpec extends AnyFlatSpec with Matchers with MockedRoutesIO {

  "GET /service/info" should "return a successful response containing service information" in runIO {

    val expectedJsonResponse =
      json"""{
        "serviceName": "$name;format="normalize"$",
        "serviceVersion": "1.0.0",
        "organization": "com.ruchij",
        "scalaVersion": "3.1.3",
        "sbtVersion": "1.7.1",
        "javaVersion": "17.0.4",
        "gitBranch" : "my-branch",
        "gitCommit" : "my-commit",
        "buildTimestamp" : null,
        "timestamp": "2021-08-04T10:10:00.000Z"
      }"""

    for {
      _ <- IO.delay {
        when(healthService.serviceInformation)
          .thenReturn {
            IO.pure {
              ServiceInformation(
                "$name;format="normalize"$",
                "1.0.0",
                "com.ruchij",
                "3.1.3",
                "1.7.1",
                "17.0.4",
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