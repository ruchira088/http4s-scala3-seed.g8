package com.ruchij.web.middleware

import cats.effect.IO
import com.ruchij.exceptions.ResourceNotFoundException
import com.ruchij.test.utils.IOUtils.runIO
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.{HttpApp, HttpRoutes, Request}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

class NotFoundHandlerSpec extends AnyFlatSpec with Matchers {

  "NotFoundHandler" should "return a ResourceNotFoundException for unmatched routes" in runIO {
    val httpApp: HttpApp[IO] =
      NotFoundHandler[IO] {
        HttpRoutes.empty
      }

    httpApp.run(Request(uri = uri"/not-found")).attempt
      .flatMap { result =>
        IO.delay {
          result mustBe Left(ResourceNotFoundException("Endpoint not found: GET /not-found"))
        }
      }
  }

}
