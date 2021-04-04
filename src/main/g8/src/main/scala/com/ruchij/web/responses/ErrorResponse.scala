package com.ruchij.web.responses

import cats.data.NonEmptyList

case class ErrorResponse(errorMessages: NonEmptyList[String])