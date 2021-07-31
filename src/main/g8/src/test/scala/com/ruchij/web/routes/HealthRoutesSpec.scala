package com.ruchij.web.routes

import cats.effect.{Clock, IO, Sync}
import com.eed3si9n.ruchij.BuildInfo
import com.ruchij.circe.Encoders.dateTimeEncoder
import com.ruchij.config.BuildInformation
import com.ruchij.services.health.HealthServiceImpl
import com.ruchij.test.utils.IOUtils.runIO
import com.ruchij.test.matchers._
import com.ruchij.types.JodaClock
import com.ruchij.web.Routes
import io.circe.literal._
import org.http4s.Method.GET
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.{Request, Status}
import org.scalamock.scalatest.MockFactory
import org.scalatest.OptionValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

import java.util.concurrent.TimeUnit
import scala.util.Properties

class HealthRoutesSpec extends AnyFlatSpec with Matchers with MockFactory with OptionValues {

  "GET /service/info" should "return a successful response containing service information" in runIO {
    for {
      dateTime <- JodaClock.create[IO].timestamp

      clock = mock[Clock[IO]]
      _ <- IO.delay((clock.realTime _).expects(TimeUnit.MILLISECONDS).returns(IO.apply(dateTime.getMillis)))

      healthService = new HealthServiceImpl[IO](HealthRoutesSpec.BuildInfo)(clock, Sync[IO])
      routes = Routes(healthService)

      request = Request[IO](GET, uri"/service/info")

      response <- routes(request)

      expectedJsonResponse =
        json"""{
            "serviceName": "$name;format="normalize"$",
            "serviceVersion": \${BuildInfo.version},
            "organization": "com.ruchij",
            "scalaVersion": \${BuildInfo.scalaVersion},
            "sbtVersion": \${BuildInfo.sbtVersion},
            "javaVersion": \${Properties.javaVersion},
            "gitBranch" : "test-branch",
            "gitCommit" : "my-commit",
            "buildTimestamp" : null,
            "timestamp": \$dateTime
          }"""

      _ = {
        response must beJsonContentType
        response must haveJson(expectedJsonResponse)
        response must haveStatus(Status.Ok)
      }
    }
    yield (): Unit
  }
}

object HealthRoutesSpec {
  private val BuildInfo: BuildInformation = BuildInformation(Some("test-branch"), Some("my-commit"), None)
}