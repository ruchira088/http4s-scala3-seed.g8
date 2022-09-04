package com.ruchij.exceptions

final case class ResourceNotFoundException(errorMessage: String) extends Exception(errorMessage)
