package com.ruchij.config

import cats.effect.IO
import com.comcast.ip4s.{ipv4, port}
import com.ruchij.test.utils.IOUtils.{error, runIO}
import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import pureconfig.ConfigSource

class ServiceConfigurationSpec extends AnyFlatSpec with Matchers {

  "ServiceConfiguration" should "parse the ConfigObjectSource" in runIO {
    val configObjectSource =
      ConfigSource.string {
        s"""
          http-configuration {
            host = "127.0.0.1"
            host = \$\${?HTTP_HOST}

            port = 80
            port = \$\${?HTTP_PORT}
          }
        """
      }

    ServiceConfiguration.parse[IO](configObjectSource).flatMap {
      serviceConfiguration =>
        IO.delay {
          serviceConfiguration.httpConfiguration mustBe HttpConfiguration(ipv4"127.0.0.1", port"80")
        }
    }
  }

  it should "return an error if ConfigObjectSource is not parsable" in runIO {
    val configObjectSource =
      ConfigSource.string {
        s"""
          http-configuration {
            host = "0.0.0.0"

            port = my-invalid-port
          }
        """
      }

    ServiceConfiguration.parse[IO](configObjectSource).error
      .flatMap { throwable =>
        IO.delay {
          throwable.getMessage must include("Cannot convert 'my-invalid-port' to com.comcast.ip4s.Port")
        }
      }
  }

}
