package com.ruchij.config

import cats.ApplicativeError
import com.ruchij.config.ConfigReaders.given
import com.ruchij.types.FunctionKTypes.{toType, given}
import pureconfig.error.ConfigReaderException
import pureconfig.{ConfigObjectSource, ConfigReader}

final case class ServiceConfiguration(httpConfiguration: HttpConfiguration) derives ConfigReader

object ServiceConfiguration {
  def parse[F[_]: [G[_]] =>> ApplicativeError[G, Throwable]](configObjectSource: ConfigObjectSource): F[ServiceConfiguration] =
    configObjectSource.load[ServiceConfiguration].left.map(ConfigReaderException.apply[ServiceConfiguration]).toType[F, Throwable]
}
