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

  implicit val hostConfigReader: ConfigReader[Host] = optionParser(Host.fromString)

  implicit val portConfigReader: ConfigReader[Port] = optionParser(Port.fromString)

  private def optionParser[A: ClassTag](parser: String => Option[A]): ConfigReader[A] =
    ConfigReader.fromNonEmptyString { input =>
      parser(input).toRight(CannotConvert(input, classTag[A].runtimeClass.getSimpleName, "Parser failed"))
    }
}