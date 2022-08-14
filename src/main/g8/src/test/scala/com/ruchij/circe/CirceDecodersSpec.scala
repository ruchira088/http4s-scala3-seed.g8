package com.ruchij.circe

import com.ruchij.circe.Decoders.given_Decoder_DateTime
import io.circe.{Decoder, Json}
import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

class CirceDecodersSpec extends AnyFlatSpec with Matchers {

  "dateTimeDecoder" should "decode the date time string" in {
    Decoder[DateTime].decodeJson(Json.fromString("2021-09-12T08:41:21.288Z")) mustBe
      Right(new DateTime(2021, 9, 12, 8, 41, 21, 288, DateTimeZone.UTC))
  }

  it should "return a failure when invalid input" in {
    Decoder[DateTime].decodeJson(Json.fromString("invalid")) mustBe a[Left[_, _]]
  }

}
