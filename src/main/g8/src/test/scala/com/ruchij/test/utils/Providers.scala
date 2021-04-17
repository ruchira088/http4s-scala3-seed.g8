package com.ruchij.test.utils

import java.util.concurrent.TimeUnit

import cats.effect.{Clock, Sync}
import org.joda.time.DateTime

import scala.concurrent.duration.TimeUnit

object Providers {
  implicit def clock[F[_]: Sync]: Clock[F] = stubClock(DateTime.now())

  def stubClock[F[_]: Sync](dateTime: => DateTime): Clock[F] = new Clock[F] {
    override def realTime(unit: TimeUnit): F[Long] =
      Sync[F].delay(unit.convert(dateTime.getMillis, TimeUnit.MILLISECONDS))

    override def monotonic(unit: TimeUnit): F[Long] = realTime(unit)
  }
}
