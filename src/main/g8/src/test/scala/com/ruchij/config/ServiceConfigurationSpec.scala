package com.ruchij.config

import cats.effect.IO
import com.comcast.ip4s.IpLiteralSyntax
import com.ruchij.test.utils.IOUtils.{IOErrorOps, runIO}
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

          build-information {
            git-branch = "my-branch"

            git-commit = \$\${?GIT_COMMIT}

            build-timestamp = "2021-07-31T10:10:00.000Z"
          }
        """
      }

    ServiceConfiguration.parse[IO](configObjectSource).flatMap {
      serviceConfiguration =>
        IO.delay {
          serviceConfiguration.httpConfiguration mustBe HttpConfiguration(ipv4"127.0.0.1", port"80")
          serviceConfiguration.buildInformation mustBe
            BuildInformation(Some("my-branch"), None, Some(new DateTime(2021, 7, 31, 10, 10, 0, 0, DateTimeZone.UTC)))
        }
    }
  }

  it should "return an error if ConfigObjectSource is not parsable" in runIO {
    val configObjectSource =
      ConfigSource.string {
        s"""
          http-configuration {
            host = "0.0.0.0"

            port = 8000
          }

          build-information {
            git-branch = "my-branch"

            build-timestamp = "invalid-date"
          }
        """
      }

    ServiceConfiguration.parse[IO](configObjectSource).error
      .flatMap { throwable =>
        IO.delay {
          throwable.getMessage must include("Cannot convert 'invalid-date' to DateTime: Invalid format: \"invalid-date\"")
        }
      }
  }

}
