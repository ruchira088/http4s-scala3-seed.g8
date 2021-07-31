package com.ruchij.web.middleware

import cats.effect.IO
import com.ruchij.exceptions.ResourceNotFoundException
import com.ruchij.test.matchers._
import com.ruchij.test.utils.IOUtils.runIO
import io.circe.literal.JsonStringContext
import org.http4s.{HttpApp, Request, Response, Status}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

class ExceptionHandlerSpec extends AnyFlatSpec with Matchers {
  "ExceptionHandler" should s"return 404 error for ResourceNotFoundException" in runIO {
    val httpApp: HttpApp[IO] =
      ExceptionHandler {
        HttpApp { _ =>
          IO.raiseError[Response[IO]](ResourceNotFoundException("Unable to find test resource"))
        }
      }

    val expectedJson =
      json"""{
        "errorMessages": [ "Unable to find test resource" ]
      }"""

    httpApp.run(Request())
      .flatMap { response =>
        IO.delay {
          response must beJsonContentType
          response must haveStatus(Status.NotFound)
          response must haveJson(expectedJson)
        }
      }
  }

  it should "return 500 error for unknown exceptions" in runIO {
    val httpApp: HttpApp[IO] =
      ExceptionHandler {
        HttpApp { _ =>
          IO.raiseError[Response[IO]](new Exception("Unexpected exception occurred"))
        }
      }

    val expectedJson =
      json"""{
        "errorMessages": [ "Unexpected exception occurred" ]
      }"""

    httpApp.run(Request())
      .flatMap { response =>
        IO.delay {
          response must beJsonContentType
          response must haveStatus(Status.InternalServerError)
          response must haveJson(expectedJson)
        }
      }
  }

}
