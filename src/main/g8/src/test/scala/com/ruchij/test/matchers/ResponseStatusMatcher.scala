package com.ruchij.test.matchers

import cats.effect.Effect
import org.http4s.{Response, Status}
import org.scalatest.matchers.{MatchResult, Matcher}

class ResponseStatusMatcher[F[_] : Effect](status: Status) extends Matcher[Response[F]] {
  override def apply(response: Response[F]): MatchResult = {
    if (status == response.status)
      MatchResult(matches = true, "N/A", s"Expected and actual statuses are equal \${response.status}")
    else {
      val body = Effect[F].toIO(response.bodyText.compile[F, F, String].string).unsafeRunSync()

      MatchResult(matches = false,
        s"""
           |Expected: \$status
           |
           |Actual: \${response.status}
           |
           |Body: \$body
           |""".stripMargin, "N/A")
    }
  }
}
