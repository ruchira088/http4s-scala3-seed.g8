package com.ruchij.config

import cats.ApplicativeError
import com.ruchij.config.BuildInformation
import com.ruchij.config.ConfigReaders.dateTimeConfigReader
import com.ruchij.types.FunctionKTypes.eitherToF
import pureconfig.ConfigObjectSource
import pureconfig.error.ConfigReaderException
import pureconfig.generic.auto._

case class ServiceConfiguration(httpConfiguration: HttpConfiguration, buildInformation: BuildInformation)

object ServiceConfiguration {
  def parse[F[_]: ApplicativeError[*[_], Throwable]](configObjectSource: ConfigObjectSource): F[ServiceConfiguration] =
    eitherToF.apply {
      configObjectSource.load[ServiceConfiguration].left.map(ConfigReaderException.apply)
    }
}
