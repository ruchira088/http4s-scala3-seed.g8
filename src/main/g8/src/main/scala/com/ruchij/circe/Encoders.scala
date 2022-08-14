package com.ruchij.circe

import io.circe.Encoder
import org.joda.time.DateTime

object Encoders {
  given Encoder[DateTime] = Encoder.encodeString.contramap[DateTime](_.toString)
}
