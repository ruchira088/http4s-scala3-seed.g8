package com.ruchij.config

import org.joda.time.DateTime
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert

import scala.util.Try

object ConfigReaders {
  implicit val dateTimeConfigReader: ConfigReader[DateTime] =
    ConfigReader.fromNonEmptyString {
      input =>
        Try(DateTime.parse(input)).toEither.left.map {
          throwable => CannotConvert(input, classOf[DateTime].getSimpleName, throwable.getMessage)
        }
    }
}
