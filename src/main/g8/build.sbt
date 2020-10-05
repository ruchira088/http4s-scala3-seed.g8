import Dependencies._
import sbtrelease.Git
import sbtrelease.ReleaseStateTransformations._

val ReleaseBranch = "dev"

lazy val root =
  (project in file("."))
    .enablePlugins(BuildInfoPlugin, JavaAppPackaging)
    .settings(
      name := "$name;format="normalize"$",
      organization := "com.ruchij",
      scalaVersion := SCALA_VERSION,
      maintainer := "me@ruchij.com",
      libraryDependencies ++= rootDependencies ++ rootTestDependencies.map(_ % Test),
      buildInfoKeys := BuildInfoKey.ofN(name, organization, version, scalaVersion, sbtVersion),
      buildInfoPackage := "com.eed3si9n.ruchij",
      topLevelDirectory := None,
      scalacOptions ++= Seq("-Xlint", "-feature", "-Wconf:cat=lint-byname-implicit:s"),
      addCompilerPlugin(kindProjector),
      addCompilerPlugin(betterMonadicFor),
      addCompilerPlugin(scalaTypedHoles)
)

lazy val rootDependencies =
  Seq(
    http4sDsl,
    http4sBlazeServer,
    http4sCirce,
    circeGeneric,
    circeParser,
    circeLiteral,
    jodaTime,
    pureconfig,
    logbackClassic
  )

lazy val rootTestDependencies =
  Seq(scalaTest, pegdown)

addCommandAlias("testWithCoverage", "; coverage; test; coverageReport")

val verifyReleaseBranch = taskKey[Unit]("Verifies the release git branch")
verifyReleaseBranch := {
  val git = Git.mkVcs(baseDirectory.value)
  val branch = git.currentBranch

  if (branch != ReleaseBranch) sys.error(s"The release branch is \$ReleaseBranch, but the current branch is set to \$branch") else (): Unit
}

releaseProcess := Seq(
  releaseStepTask(verifyReleaseBranch),
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  setNextVersion,
  commitNextVersion,
  pushChanges
)
