import Dependencies._

lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "com.ruchij",
      scalaVersion := "2.12.5"
    )),
    name := "$name$",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      pegdown % Test
    ),
    buildInfoKeys := BuildInfoKey.ofN(name, scalaVersion, sbtVersion),
    buildInfoPackage := "com.eed3si9n.ruchij"
  )

enablePlugins(BuildInfoPlugin)

coverageEnabled := true

testOptions in Test +=
  Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-results")

addCommandAlias("testWithCoverage", "; clean; test; coverageReport")