package com.ruchij.test

import cats.effect.{Clock, Sync}
import com.ruchij.services.health.HealthServiceImpl
import com.ruchij.web.Routes
import org.http4s.HttpApp

import scala.language.higherKinds

object HttpTestApp {
  def apply[F[_]: Sync: Clock](): HttpApp[F] =
    Routes(new HealthServiceImpl[F])
}
