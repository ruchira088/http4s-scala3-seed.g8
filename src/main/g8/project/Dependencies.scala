import sbt._

object Dependencies
{
  val ScalaVersion = "3.2.1"
  val Http4sVersion = "0.23.18"
  val CirceVersion = "0.14.5"

  lazy val http4sDsl = "org.http4s" %% "http4s-dsl" % Http4sVersion

  lazy val http4sEmberServer = "org.http4s" %% "http4s-ember-server" % Http4sVersion

  lazy val http4sCirce = "org.http4s" %% "http4s-circe" % Http4sVersion

  lazy val circeGeneric = "io.circe" %% "circe-generic" % CirceVersion

  lazy val circeParser = "io.circe" %% "circe-parser" % CirceVersion

  lazy val circeLiteral = "io.circe" %% "circe-literal" % CirceVersion

  lazy val jodaTime = "joda-time" % "joda-time" % "2.12.2"

  lazy val pureconfig = "com.github.pureconfig" %% "pureconfig-core" % "0.17.2"

  lazy val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.4.6"

  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.15"

  lazy val scalaTestPlusMockito = "org.scalatestplus" %% "mockito-4-6" % "3.2.15.0"

  lazy val pegdown = "org.pegdown" % "pegdown" % "1.6.0"
}
