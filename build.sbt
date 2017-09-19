name := "api-test-kit"

scalaVersion := "2.11.8"

organization := "com.github.mideo"


resolvers ++= Seq(
  "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  "Sonatypes" at "https://oss.sonatype.org/content/repositories/releases",
  "Maven Repo" at "https://mvnrepository.com/maven2"
)


lazy val `apktestkit` = (project in file("."))
   .settings(Common.settings: _*)
   .settings(libraryDependencies ++= Dependencies.Atam ++ Dependencies.Gatling ++ Dependencies.Core ++ Dependencies.RestAssured)

