package com.ruchij.types

import cats.effect.kernel.Sync
import org.slf4j.{LoggerFactory, Logger => Slf4jLogger}

import scala.reflect.ClassTag

case class Logger[A: ClassTag](slf4jLogger: Slf4jLogger) {

  def info[F[_]: Sync](infoMessage: String): F[Unit] =
    Sync[F].blocking {
      slf4jLogger.info(infoMessage)
    }

  def warn[F[_]: Sync](warnMessage: String): F[Unit] =
    Sync[F].blocking {
      slf4jLogger.warn(warnMessage)
    }

  def error[F[_]: Sync](errorMessage: String): F[Unit] =
    Sync[F].blocking {
      slf4jLogger.error(errorMessage)
    }

}

object Logger {
  def apply[A](implicit classTag: ClassTag[A]): Logger[A] =
    Logger {
      LoggerFactory.getLogger(classTag.runtimeClass)
    }
}
