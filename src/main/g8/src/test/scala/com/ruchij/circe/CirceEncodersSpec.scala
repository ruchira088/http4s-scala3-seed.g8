package com.ruchij.circe

import io.circe.Json
import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

class CirceEncodersSpec extends AnyFlatSpec with Matchers {

  "dateTimeEncoder" should "encode DateTime" in {
    val dateTime = new DateTime(2021, 9, 12, 19, 1, 49, 100, DateTimeZone.UTC)

    Encoders.dateTimeEncoder(dateTime) mustBe Json.fromString("2021-09-12T19:01:49.100Z")
  }

}
