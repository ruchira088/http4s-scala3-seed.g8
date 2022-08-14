package com.ruchij.config

import com.comcast.ip4s.{Host, Port}
import org.joda.time.DateTime
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert

import scala.util.Try

object ConfigReaders {
  given ConfigReader[DateTime] =
    ConfigReader.fromNonEmptyString {
      input =>
        Try(DateTime.parse(input)).toEither.left.map {
          throwable => CannotConvert(input, classOf[DateTime].getSimpleName, throwable.getMessage)
        }
    }

  given ConfigReader[Host] = ConfigReader.fromNonEmptyStringOpt(Host.fromString)

  given ConfigReader[Port] = ConfigReader.fromNonEmptyStringOpt(Port.fromString)
}