import Dependencies._

lazy val root =
  (project in file("."))
    .enablePlugins(BuildInfoPlugin)
    .settings(
      name := "$name;format="normalize"$",
      organization := "com.ruchij",
      scalaVersion := SCALA_VERSION,
      libraryDependencies ++= rootDependencies ++ rootTestDependencies.map(_ % Test),
      buildInfoKeys := BuildInfoKey.ofN(name, organization, version, scalaVersion, sbtVersion),
      buildInfoPackage := "com.eed3si9n.ruchij",
      assemblyJarName in assembly := "$name;format="normalize"$-assembly.jar",
      testOptions in Test +=
        Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-results")
    )

lazy val rootDependencies =
  Seq()

lazy val rootTestDependencies =
  Seq(scalaTest, pegdown)

addCommandAlias("testWithCoverage", "; coverage; test; coverageReport")
