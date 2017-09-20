import com.typesafe.sbt.SbtPgp.{PgpKeys, pgpPassphrase}
import sbt.Keys._
import sbt._
import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease.ReleaseStateTransformations._

object Common {

  val oss_user: String = if (sys.env.keySet.contains("OSS_USERNAME")) sys.env("OSS_USERNAME") else ""

  val oss_pass: String = if (sys.env.keySet.contains("OSS_PASSWORD")) sys.env("OSS_PASSWORD") else ""

  val gpg_pass = if (sys.env.keySet.contains("GPG_PASSWORD")) sys.env("GPG_PASSWORD").toCharArray else Array.emptyCharArray

  scalacOptions := Seq(
    "-unchecked",
    "-deprecation",
    "-encoding",
    "utf8",
    "-feature",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-language:reflectiveCalls",
    "-Yrangepos"
  )

  autoScalaLibrary := false

  fork in run := true

  releaseVersionBump := sbtrelease.Version.Bump.Next

  releaseIgnoreUntrackedFiles := true

  releasePublishArtifactsAction := PgpKeys.publishSigned.value

  val tagName = Def.setting {
    s"v${if (releaseUseGlobalVersion.value) (version in ThisBuild).value else version.value}"
  }
  val tagOrHash = Def.setting {
    if (isSnapshot.value)
      sys.process.Process("git rev-parse HEAD").lines_!.head
    else
      tagName.value
  }


  val settings = Seq(
    organization := "com.github.mideo",

    scalacOptions := Seq(
      "-unchecked",
      "-deprecation",
      "-encoding",
      "utf8",
      "-feature",
      "-language:implicitConversions",
      "-language:postfixOps",
      "-language:reflectiveCalls",
      "-Yrangepos"
    ),

    autoScalaLibrary := false,

    fork in run := true,

    pomIncludeRepository := { _ => false },

    publishMavenStyle := true,

    publishArtifact in Test := false,

    licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php")),

    homepage := Some(url("https://github.com/MideO/api-test-kit")),

    scmInfo := Some(
      ScmInfo(
        url("https://github.com/MideO/api-test-kit"),
        "scm:git@github.com/MideO/api-test-kit"
      )
    ),

    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },

    developers := List(
      Developer(
        id = "mideo",
        name = "Mide Ojikutu",
        email = "mide.ojikutu@gmail.com",
        url = url("https://github.com/MideO")
      )
    ),

    credentials += Credentials(
      "Sonatype Nexus Repository Manager",
      "oss.sonatype.org", oss_user, oss_pass),

    pgpPassphrase := Some(gpg_pass),

    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      publishArtifacts,
      setNextVersion,
      commitNextVersion,
      pushChanges
    )

  )

}
