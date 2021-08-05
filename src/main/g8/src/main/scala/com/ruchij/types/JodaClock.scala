package com.ruchij.types

import cats.Applicative
import cats.effect.{Clock, Sync}
import cats.implicits._
import org.joda.time.DateTime

trait JodaClock[F[_]] {
  val timestamp: F[DateTime]
}

object JodaClock {
  def apply[F[_]](implicit jodaClock: JodaClock[F]): JodaClock[F] = jodaClock

  implicit def fromClock[F[_]: Applicative: Clock]: JodaClock[F] =
    new JodaClock[F] {
      override val timestamp: F[DateTime] =
        Clock[F].realTime.map(duration => new DateTime(duration.toMillis))
    }

  def create[F[_]: Sync]: JodaClock[F] = JodaClock.fromClock[F]
}
