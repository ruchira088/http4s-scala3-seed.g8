import sbt._

object Dependencies
{
  val SCALA_VERSION = "2.13.1"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8"

  lazy val pegdown = "org.pegdown" % "pegdown" % "1.6.0"
}
