name := "api-test-kit"

scalaVersion := "2.11.8"

organization := "com.github.mideo"

lazy val `apktestkit` = (project in file("."))
  .settings(
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

    testOptions += Tests.Argument(TestFrameworks.JUnit),

    javaSource in Compile := baseDirectory.value / "src/main/java",

    javaSource in Test := baseDirectory.value / "src/test/java",

    resourceDirectory in Compile := baseDirectory.value / "src/main/resources",

    resourceDirectory in Test := baseDirectory.value / "src/test/resources"
  )



resolvers ++= Seq(
  "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  "Sonatypes" at "https://oss.sonatype.org/content/repositories/releases",
  "Maven Repo" at "https://mvnrepository.com/maven2"
)

lazy val GatlingVersion = "2.2.2"

lazy val RestAssuredVersion = "3.0.0"

libraryDependencies ++= Seq(
  // Atam
  "me.atam" % "atam4j" % "0.58",
  "io.dropwizard" % "dropwizard-core" % "1.1.1",

  // Gatling
  "io.gatling.highcharts" % "gatling-charts-highcharts" % GatlingVersion,
  "io.gatling" % "gatling-test-framework" % GatlingVersion,
  "io.gatling" % "gatling-recorder" % GatlingVersion,

  // RestAssured
  "io.rest-assured" % "rest-assured" % RestAssuredVersion,
  "io.rest-assured" % "json-schema-validator" % RestAssuredVersion,

  // Wiremock
  "com.github.tomakehurst" % "wiremock" % "2.7.1",

  "com.novocode" % "junit-interface" % "0.10" % "test",
  "junit" % "junit" % "4.12",
  "org.hamcrest" % "hamcrest-junit" % "2.0.0.0",
  "org.scalatest" % "scalatest_2.11" % "3.0.3" % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % "test"
)

pomIncludeRepository := { _ => false }

publishMavenStyle := true

publishArtifact in Test := false

val oss_user = if (sys.env.keySet.contains("OSS_USERNAME")) sys.env("OSS_USERNAME") else ""
val oss_pass = if (sys.env.keySet.contains("OSS_PASSWORD")) sys.env("OSS_PASSWORD") else ""
val gpg_pass = if (sys.env.keySet.contains("GPG_PASSWORD")) sys.env("GPG_PASSWORD").toCharArray else Array.emptyCharArray

credentials += Credentials(
  "Sonatype Nexus Repository Manager",
  "oss.sonatype.org", oss_user, oss_pass)

pgpPassphrase := Some(gpg_pass)

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php"))

homepage := Some(url("https://github.com/MideO/api-test-kit"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/MideO/api-test-kit"),
    "scm:git@github.com/MideO/api-test-kit"
  )
)

developers := List(
  Developer(
    id = "mideo",
    name = "Mide Ojikutu",
    email = "mide.ojikutu@gmail.com",
    url = url("https://github.com/MideO")
  )
)

val tagName = Def.setting {
  s"v${if (releaseUseGlobalVersion.value) (version in ThisBuild).value else version.value}"
}
val tagOrHash = Def.setting {
  if (isSnapshot.value)
    sys.process.Process("git rev-parse HEAD").lines_!.head
  else
    tagName.value
}


// Release
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

releaseVersionBump := sbtrelease.Version.Bump.Next

releaseIgnoreUntrackedFiles := true

releasePublishArtifactsAction := PgpKeys.publishSigned.value

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