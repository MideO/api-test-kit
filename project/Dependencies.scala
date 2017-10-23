import sbt._


object Dependencies {

  private val GatlingVersion = "2.2.2"

  private val RestAssuredVersion = "3.0.0"

  val Atam = Seq(
    "me.atam" % "atam4j" % "0.58",
    "io.dropwizard" % "dropwizard-core" % "1.1.1"
  )

  val Gatling = Seq(
    "io.gatling.highcharts" % "gatling-charts-highcharts" % GatlingVersion,
    "io.gatling" % "gatling-test-framework" % GatlingVersion,
    "io.gatling" % "gatling-recorder" % GatlingVersion
  )
  val RestAssured = Seq(
    "io.rest-assured" % "rest-assured" % RestAssuredVersion,
    "io.rest-assured" % "json-schema-validator" % RestAssuredVersion
  )

  val Core = Seq(
    "com.github.tomakehurst" % "wiremock" % "2.8.0",
    "com.novocode" % "junit-interface" % "0.10" % "test",
    "junit" % "junit" % "4.12",
    "org.hamcrest" % "hamcrest-junit" % "2.0.0.0"  exclude ("org.hamcrest", "java-hamcrest"),
    "org.scalatest" % "scalatest_2.11" % "3.0.3" % "test",
    "org.pegdown" % "pegdown" % "1.6.0" % "test",
    "org.mockito" % "mockito-all" % "1.9.5" % "test"
  )

}
