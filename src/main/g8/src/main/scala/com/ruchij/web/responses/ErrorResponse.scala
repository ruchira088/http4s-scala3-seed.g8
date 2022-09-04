package com.ruchij.web.responses

import cats.data.NonEmptyList

final case class ErrorResponse(errorMessages: NonEmptyList[String])
