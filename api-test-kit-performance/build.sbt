name := "api-test-kit-performance"

scalaVersion := "2.11.8"

organization := "com.github.mideo"

javaSource in Compile := baseDirectory.value / "src/main/scala"

resourceDirectory in Compile := baseDirectory.value / "src/main/resources"
