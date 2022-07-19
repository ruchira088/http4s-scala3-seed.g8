package com.ruchij.config

import com.comcast.ip4s.{Host, Port}
import org.joda.time.DateTime
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert

import scala.reflect.{ClassTag, classTag}
import scala.util.Try

object ConfigReaders {
  implicit val dateTimeConfigReader: ConfigReader[DateTime] =
    ConfigReader.fromNonEmptyString {
      input =>
        Try(DateTime.parse(input)).toEither.left.map {
          throwable => CannotConvert(input, classOf[DateTime].getSimpleName, throwable.getMessage)
        }
    }

  implicit val hostConfigReader: ConfigReader[Host] = ConfigReader.fromNonEmptyStringOpt(Host.fromString)

  implicit val portConfigReader: ConfigReader[Port] = ConfigReader.fromNonEmptyStringOpt(Port.fromString)
}