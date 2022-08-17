package com.ruchij.test

import cats.effect.IO
import io.circe.Json
import org.http4s.{MediaType, Status}

package object matchers {
  val beJsonContentType: ContentTypeMatcher[IO] = ContentTypeMatcher[IO](MediaType.application.json)

  def haveJson(json: Json): JsonResponseMatcherIO = JsonResponseMatcherIO(json)

  def haveStatus(status: Status): ResponseStatusMatcher[IO] = ResponseStatusMatcher[IO](status)
}
