package com.ruchij.config

import com.comcast.ip4s.{Host, Port}
import pureconfig.ConfigReader

import scala.util.Try

object ConfigReaders {
  given ConfigReader[Host] = ConfigReader.fromNonEmptyStringOpt(Host.fromString)

  given ConfigReader[Port] = ConfigReader.fromNonEmptyStringOpt(Port.fromString)
}