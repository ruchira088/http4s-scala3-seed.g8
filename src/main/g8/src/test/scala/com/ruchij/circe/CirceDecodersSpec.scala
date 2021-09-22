package com.ruchij.circe

import io.circe.Json
import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

class CirceDecodersSpec extends AnyFlatSpec with Matchers {

  "dateTimeDecoder" should "decode the date time string" in {
    Decoders.dateTimeDecoder.decodeJson(Json.fromString("2021-09-12T08:41:21.288Z")) mustBe
      Right(new DateTime(2021, 9, 12, 8, 41, 21, 288, DateTimeZone.UTC))
  }

  it should "return a failure when invalid input" in {
    Decoders.dateTimeDecoder.decodeJson(Json.fromString("invalid")) mustBe a [Left[_, _]]
  }

}
