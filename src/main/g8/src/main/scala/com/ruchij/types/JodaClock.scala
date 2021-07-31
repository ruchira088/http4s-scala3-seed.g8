package com.ruchij.types

import cats.Applicative
import cats.effect.{Clock, Sync}
import cats.implicits._
import org.joda.time.DateTime

import java.util.concurrent.TimeUnit

trait JodaClock[F[_]] {
  val timestamp: F[DateTime]
}

object JodaClock {
  def apply[F[_]](implicit jodaClock: JodaClock[F]): JodaClock[F] = jodaClock

  implicit def fromClock[F[_]: Applicative: Clock]: JodaClock[F] =
    new JodaClock[F] {
      override val timestamp: F[DateTime] =
        Clock[F].realTime(TimeUnit.MILLISECONDS).map(milliseconds => new DateTime(milliseconds))
    }

  def create[F[_]: Sync]: JodaClock[F] = JodaClock.fromClock[F](Applicative[F], Clock.create[F])
}
