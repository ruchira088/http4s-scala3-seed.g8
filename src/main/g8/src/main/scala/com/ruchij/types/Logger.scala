package com.ruchij.types

import cats.effect.kernel.Sync
import com.typesafe.scalalogging.{Logger => TypesafeLogger}

import scala.reflect.ClassTag

case class Logger[A: ClassTag](typesafeLogger: TypesafeLogger) {

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

}

object Logger {
  def apply[A: ClassTag]: Logger[A] = Logger { TypesafeLogger[A] }
}
