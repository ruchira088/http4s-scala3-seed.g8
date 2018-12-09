import sbt._

object Dependencies
{
  val SCALA_VERSION = "2.12.8"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"

  lazy val pegdown = "org.pegdown" % "pegdown" % "1.6.0"
}
