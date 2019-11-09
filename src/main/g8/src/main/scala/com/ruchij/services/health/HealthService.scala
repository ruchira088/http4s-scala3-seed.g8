package com.ruchij.services.health

import com.ruchij.services.health.models.ServiceInformation

import scala.language.higherKinds

trait HealthService[F[_]] {
  def serviceInformation(): F[ServiceInformation]
}
