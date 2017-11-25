package com.github.mideo.apitestkit.gatling

import com.github.mideo.apitestkit.StubBuilder
import java.util.Properties

import io.gatling.core.Predef._
import io.gatling.core.controller.inject.InjectionStep
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}
import io.gatling.http.protocol.HttpProtocol

import scala.concurrent.duration._
import scala.util.Random


trait TestScenario {
  def scenarioBuilder(scenarioName: String*): ScenarioBuilder
}


trait PerformanceTest extends Simulation {

  def injection: InjectionStep = constantUsersPerSec(PerformanceTestConfig.NumberOfUniqueUsers) during (PerformanceTestConfig.Duration minutes)

  val httpProtocol:HttpProtocol

  val testScenarios: List[TestScenario]

  val simulations: List[PopulationBuilder]

  def doSetUp(): SetUp = {
    setUp(Random.shuffle(simulations): _*)
      .throttle(reachRps(PerformanceTestConfig.RequestPerSecond) in (1 second), holdFor(PerformanceTestConfig.Duration minutes))
      .assertions(global.successfulRequests.percent.greaterThan(PerformanceTestConfig.SuccessfulRequestPercentage))
  }
}

trait LocalPerformanceTest
  extends TestApplication
    with PerformanceTest {
  before {
    if (!applicationStarted) {
      startApplication()
      Runtime.getRuntime.addShutdownHook(new Thread() {
        override def run(): Unit = {
          stopApplication()
        }
      })
      applicationStarted = true
    }
  }


}

object ScalaStubBuilder extends StubBuilder { disableRequestJournal() }

sealed trait TestApplication extends Simulation {
  protected var properties = new Properties

  def startApplication(): Unit

  def stopApplication(): Unit

  var applicationStarted = false
}


trait MockedPerformanceTest
  extends LocalPerformanceTest {
  before {
    ScalaStubBuilder.startWireMock()
  }
}
