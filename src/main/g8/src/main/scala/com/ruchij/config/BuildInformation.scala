package com.ruchij.config

import org.joda.time.DateTime

final case class BuildInformation(gitBranch: Option[String], gitCommit: Option[String], buildTimestamp: Option[DateTime])
