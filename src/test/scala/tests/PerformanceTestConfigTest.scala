package tests

import com.github.mideo.apitestkit.gatling.PerformanceTestConfig

class PerformanceTestConfigTest extends TestLibSpec {

  test("testWithRequestPerSecond") {
    //When
    PerformanceTestConfig.withRequestPerSecond(1)

    //Then
    PerformanceTestConfig.RequestPerSecond should be(1)

  }

  test("testWithPropertyFile") {
    //When
    PerformanceTestConfig.withPropertyFile("test.properties")

    //Then
    PerformanceTestConfig.PropertyFile should be("test.properties")

  }

  test("testWithSuccessfulRequestPercentage") {
    //When
    PerformanceTestConfig.withSuccessfulRequestPercentage(98)

    //Then
    PerformanceTestConfig.SuccessfulRequestPercentage should be(98)

  }

  test("testWithNumberOfUniqueUsers") {
    //When
    PerformanceTestConfig.withNumberOfUniqueUsers(5)

    //Then
    PerformanceTestConfig.NumberOfUniqueUsers should be(5)
  }

  test("testForDurationInMinutes") {
    //When
    PerformanceTestConfig.forDurationInMinutes(10)

    //Then
    PerformanceTestConfig.Duration should be(10)

  }


  test("testForUrl") {
    //When
    PerformanceTestConfig.forUrl("http://localhost")

    //Then
    PerformanceTestConfig.Url should be("http://localhost")

  }

}
