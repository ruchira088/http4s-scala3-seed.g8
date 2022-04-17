package com.ruchij.types

import cats.effect.kernel.Sync
import com.typesafe.scalalogging.{Logger => TypesafeLogger}

import scala.reflect.ClassTag

case class Logger(typesafeLogger: TypesafeLogger) {

  def info[F[_]: Sync](infoMessage: String): F[Unit] =
    Sync[F].blocking {
      typesafeLogger.info(infoMessage)
    }

  def warn[F[_]: Sync](warnMessage: String): F[Unit] =
    Sync[F].blocking {
      typesafeLogger.warn(warnMessage)
    }

  def error[F[_]: Sync](errorMessage: String): F[Unit] =
    Sync[F].blocking {
      typesafeLogger.error(errorMessage)
    }

  def error[F[_]: Sync](errorMessage: String, throwable: Throwable): F[Unit] =
    Sync[F].blocking {
      typesafeLogger.error(errorMessage, throwable)
    }

  def debug[F[_]: Sync](debugMessage: String): F[Unit] =
    Sync[F].blocking {
      typesafeLogger.debug(debugMessage)
    }

  def trace[F[_]: Sync](traceMessage: String): F[Unit] =
    Sync[F].blocking {
      typesafeLogger.trace(traceMessage)
    }

}

object Logger {
  def apply[A: ClassTag]: Logger = Logger { TypesafeLogger[A] }
}
