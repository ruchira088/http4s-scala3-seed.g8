package com.ruchij.config

import cats.ApplicativeError
import com.ruchij.config.BuildInformation
import com.ruchij.config.ConfigReaders.given
import com.ruchij.types.FunctionKTypes.*
import pureconfig.error.ConfigReaderException
import pureconfig.generic.derivation.default.*
import pureconfig.{ConfigObjectSource, ConfigReader}

case class ServiceConfiguration(httpConfiguration: HttpConfiguration, buildInformation: BuildInformation) derives ConfigReader

object ServiceConfiguration {
  def parse[F[_]: [G[_]] =>> ApplicativeError[G, Throwable]](configObjectSource: ConfigObjectSource): F[ServiceConfiguration] =
    configObjectSource.load[ServiceConfiguration].left.map(ConfigReaderException.apply[ServiceConfiguration]).toType[F, Throwable]
}
