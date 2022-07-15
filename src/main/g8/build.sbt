import Dependencies._
import sbtrelease.Git
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.Utilities.stateW

import java.awt.Desktop
import scala.sys.process.ProcessBuilder

val ReleaseBranch = "dev"
val ProductionBranch = "main"

lazy val root =
  (project in file("."))
    .enablePlugins(BuildInfoPlugin, JavaAppPackaging)
    .settings(
      name := "$name;format="normalize"$",
      organization := "com.ruchij",
      scalaVersion := Dependencies.ScalaVersion,
      maintainer := "me@ruchij.com",
      libraryDependencies ++= rootDependencies ++ rootTestDependencies.map(_ % Test),
      buildInfoKeys := Seq[BuildInfoKey](name, organization, version, scalaVersion, sbtVersion),
      buildInfoPackage := "com.eed3si9n.ruchij",
      topLevelDirectory := None,
      scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xfatal-warnings"),
      Universal / javaOptions ++= Seq("-Dlogback.configurationFile=/opt/data/logback.xml"),
      addCompilerPlugin(kindProjector),
      addCompilerPlugin(betterMonadicFor)
)

lazy val rootDependencies =
  Seq(
    http4sDsl,
    http4sEmberServer,
    http4sCirce,
    circeGeneric,
    circeParser,
    circeLiteral,
    jodaTime,
    pureconfig,
    logbackClassic,
    scalaLogging
  )

lazy val rootTestDependencies =
  Seq(scalaTest, scalaMock, pegdown)

val verifyReleaseBranch = { state: State =>
  val git = Git.mkVcs(state.extract.get(baseDirectory))
  val branch = git.currentBranch

  if (branch != ReleaseBranch) {
    sys.error {
      s"The release branch is \$ReleaseBranch, but the current branch is set to \$branch"
    }
  } else state
}

val mergeReleaseToMaster = { state: State =>
  val git = Git.mkVcs(state.extract.get(baseDirectory))

  val (updatedState, releaseTag) = state.extract.runTask(releaseTagName, state)

  updatedState.log.info(s"Merging \$releaseTag to \$ProductionBranch...")

  val userInput: Option[ProcessBuilder] =
    SimpleReader.readLine("Push changes to the remote master branch (y/n)? [y]")
      .map(_.toLowerCase) match {
      case Some("y") | Some("")  =>
        updatedState.log.info(s"Pushing changes to remote master (\$releaseTag)...")
        Some(git.cmd("push"))

      case _ =>
        updatedState.log.warn("Remember to push changes to remote master")
        None
    }

  val actions: List[ProcessBuilder] =
    List(git.cmd("checkout", ProductionBranch), git.cmd("pull", "--rebase"), git.cmd("merge", releaseTag)) ++
      userInput ++
      List(git.cmd("checkout", ReleaseBranch))

  actions.reduce(_ #&& _) !!

  updatedState.log.info(s"Successfully merged \$releaseTag to \$ProductionBranch")

  updatedState
}
releaseProcess := Seq(
  ReleaseStep(verifyReleaseBranch),
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(mergeReleaseToMaster),
  setNextVersion,
  commitNextVersion,
  pushChanges
)

val viewCoverageResults = taskKey[Unit]("Opens the coverage result in the default browser")

viewCoverageResults := {
  val coverageResults =
    target.value.toPath.resolve(s"scala-\${scalaBinaryVersion.value}/scoverage-report/index.html")

  Desktop.getDesktop.browse(coverageResults.toUri)
}

addCommandAlias("cleanCompile", "; clean; compile")
addCommandAlias("cleanTest", "; clean; test")
addCommandAlias("testWithCoverage", "; clean; coverage; test; coverageAggregate; viewCoverageResults")