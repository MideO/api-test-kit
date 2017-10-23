name := "api-test-kit"

scalaVersion := "2.11.8"

organization := "com.github.mideo"

testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")

javaSource in Compile := baseDirectory.value / "src/main/java"

javaSource in Test := baseDirectory.value / "src/test/java"

scalaSource in Compile := baseDirectory.value / "src/main/scala"

scalaSource  in Test := baseDirectory.value / "src/test/scala"

resourceDirectory in Compile := baseDirectory.value / "src/main/resources"

resourceDirectory in Test := baseDirectory.value / "src/test/resources"

lazy val apiTestKitCore = (project in file("api-test-kit-core"))
  .settings(Common.settings: _*)
  .settings(libraryDependencies ++= Dependencies.Core ++ Dependencies.RestAssured)

lazy val apiTestKitMonitoring = (project in file("api-test-kit-monitoring"))
  .settings(Common.settings: _*)
  .settings(libraryDependencies ++= Dependencies.Atam)

lazy val apiTestKitApi = (project in file("api-test-kit-api"))
  .settings(Common.settings: _*)
  .dependsOn(apiTestKitCore)

lazy val apiTestKitPerformance = (project in file("api-test-kit-performance"))
  .settings(Common.settings: _*)
  .dependsOn(apiTestKitCore)
  .settings(libraryDependencies ++= Dependencies.Gatling)

lazy val `apitestkit` = (project in file("."))
  .aggregate(apiTestKitApi, apiTestKitCore, apiTestKitMonitoring, apiTestKitPerformance)
  .dependsOn(apiTestKitApi, apiTestKitCore, apiTestKitMonitoring, apiTestKitPerformance)
  .settings(Common.settings: _*)
  .settings(libraryDependencies ++= Dependencies.Atam ++ Dependencies.Gatling ++ Dependencies.Core ++ Dependencies.RestAssured)

