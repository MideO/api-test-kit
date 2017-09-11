package tests

import com.github.mideo.apitestkit.AsciiArt
import com.github.mideo.apitestkit.gatling.{GatlingTestRunner, LocalPerformanceTest, TestScenario}
import io.gatling.core.Predef.{atOnceUsers, scenario, _}
import io.gatling.core.controller.inject.InjectionStep
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocol



object FakeScenario extends TestScenario {
  override def scenarioBuilder(business: String, userName: String): ScenarioBuilder = {
    scenario(s"Fake Scenario $business")
      .exec(http("fake request").get("/"))
  }
}

object AnotherFakeScenario extends TestScenario {
  override def scenarioBuilder(business: String, userName: String): ScenarioBuilder = {
    scenario(s"Another Fake Scenario $business")
      .exec(http("Another fake request").get("/"))
  }
}

class FakePerformanceTest
  extends LocalPerformanceTest{
  AsciiArt.draw("ApiTestKit")

  override val httpProtocol: HttpProtocol = http.baseURL("http://localhost").disableWarmUp.build
  override def injection: InjectionStep = atOnceUsers(1)

  override val testScenarios: List[TestScenario] = List(FakeScenario, AnotherFakeScenario)
  override val simulations: List[PopulationBuilder] = testScenarios map {
    _.scenarioBuilder("fakeScenario", "fakehttpRequest")
      .inject(injection).protocols(httpProtocol)
  }


  override def startApplication(): Unit = {}

  override def stopApplication(): Unit = {}

  doSetUp()

}


class GatlingTestRunnerSpec extends TestLibSpec {

  test("GatlingTestRunner should run") {
    GatlingTestRunner.forSimulationClass(classOf[FakePerformanceTest].getName).fromSourceDirectory("src/test").saveResultDirectoryTo("build/reports/performanceTest").run should be(2)
  }
}
