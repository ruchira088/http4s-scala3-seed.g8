package com.ruchij.test.matchers

import cats.Show
import org.http4s.headers.`Content-Type`
import org.http4s.{Header, MediaType, ParseResult, Response}
import org.scalatest.matchers.{MatchResult, Matcher}

class ContentTypeMatcher[F[_]](mediaType: MediaType) extends Matcher[Response[F]] {
  override def apply(response: Response[F]): MatchResult = {
    val maybeContentTypeResult: Option[ParseResult[MediaType]] =
      response.headers.get(Header[`Content-Type`].name)
        .map { header => MediaType.parse(header.head.value) }

    MatchResult(
      maybeContentTypeResult.exists(_.contains(mediaType)),
      s"""
         |Response Content-Type header value did NOT match expected media type,
         |   Expected: \${Show[MediaType].show(mediaType)}
         |   Actual: \${maybeContentTypeResult.map(_.fold(_.message, Show[MediaType].show)).getOrElse("Missing Content-Type header")}
         |""".stripMargin,
      s"Content-Type is \${Show[MediaType].show(mediaType)}"
    )
  }
}
