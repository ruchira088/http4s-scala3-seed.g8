package com.ruchij.types

import cats.Applicative
import cats.effect.{Clock, Sync}
import cats.implicits.*
import org.joda.time.DateTime

trait JodaClock[F[_]] {
  val timestamp: F[DateTime]
}

object JodaClock {
  def apply[F[_]](using jodaClock: JodaClock[F]): JodaClock[F] = jodaClock

  given fromClock[F[_]: Applicative: Clock]: JodaClock[F] =
    new JodaClock[F] {
      override val timestamp: F[DateTime] =
        Clock[F].realTime.map(duration => DateTime(duration.toMillis))
    }

  def create[F[_]: Sync]: JodaClock[F] = JodaClock.fromClock[F]
}
