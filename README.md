## api-test-kit 

[![Build Status](https://travis-ci.org/MideO/api-test-kit.svg?branch=master)](https://travis-ci.org/MideO/api-test-kit)

### Modules
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mideo/api-test-kit_2.11/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.mideo%22%20a%3A%22api-test-kit_2.11%22) api-test-kit

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mideo/api-test-kit-api_2.11/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.mideo%22%20a%3A%22api-test-kit-api_2.11%22) api-test-kit-api

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mideo/api-test-kit-performance_2.11/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.mideo%22%20a%3A%22api-test-kit-performance_2.11%22) api-test-kit-performance 	  

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mideo/api-test-kit-core_2.11/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.mideo%22%20a%3A%22api-test-kit-core_2.11%22) api-test-kit-core 	  

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mideo/api-test-kit-monitoring_2.11/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.mideo%22%20a%3A%22api-test-kit-monitoring_2.11%22) api-test-kit-monitoring 

Test library for quick bootstrapping API Acceptance/Integration and Performance tests base classes backed with JUnit, Wiremock, Atam4J, and Gatling. 

For more information on test tools see:
 
* [JUnit](http://junit.org)
* [Rest-Assured](http://rest-assured.io/)
* [Wiremock](http://wiremock.org)
* [Atam4J](https://github.com/atam4j/atam4j)
* [Gatling](http://gatling.io)


#### Java Utilities
* RestAssuredSpecFactory - Create rest-assured request spec with all wapi required headers
 ```java
 public class Example {
         public static void main(String[] args) throws Exception {
             RestAssuredSpecFactory.givenARequestSpecificationWithAllRequiredHeaders()
                    .when()
                    .post("http://exmaple.com")
                    .then().statusCode(200);
        
     }
 }
 
 ```

* AtamApplication - Acceptance Test as Monitors bootstrap
```java
public class ExampleAtamApplication extends Atam4jMainApplication {
        public static void main(String[] args) throws Exception {
        if (args == null || args.length == 0) {
            args = new String[]{"server", getAtam4JConfigFile()};
        }
        new ExampleAtamApplication().run(args);
    }
}


```
* ApiTest - Bootstrap api tests by starting and shutting down the test application 
```java
class ExampleApiTest extends ApiTest {

    @Override
    void startApplication() {
        Spark.start();
    }

    @Override
    void stopApplication() {
        Spark.stop();
    }

    @Override
    void loadTestApplicationProperties() {
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("test.properties"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

```
* WiremockBasedApiTest - Bootstrap api tests by starting and shutting down the test application and wiremock
```java
class ExampleWiremockBasedApiTest extends WireMockBasedApiTest {

    @Override
    void startApplication() {
        Spark.start();
    }

    @Override
    void stopApplication() {
        Spark.stop();
    }

    @Override
    void loadTestApplicationProperties() {
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("test.properties"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

```
* JsonParser - quickly serialize/deserialize json backed by jackson
```java
    public class DummyExample {
           
        static class Dummy {
    
    
            public String getName() {
                return name;
            }
    
            private String name;
    
            public void setName(String name) {
                this.name = name;
            }
    
        }

        public static void main(String[] args) {
        
            Dummy d = JsonParser.deserialize("{\"name\":\"Dummy\"}", Dummy.class);
            String payload = JsonParser.serialize(ImmutableMap.of("abc", "123"));
        }
    }
```
  
 * StubBuilder - wiremock backed stubbing
 ```java
 public class Example {
          public static void main(String[] args) throws Exception {
              StubBuilder stubServer = new StubBuilder().startWireMock();
              
              //stubbing
              stubServer.givenWiremockWillReturnCode(200);
              
              //set wiremock as proxy and record conversation with a third party service for future use
              StubRecorder recorder = stubBuilder.recorder("https://example.com", 443).record(
                              () -> requestSpecification
                                      .when()
                                      .get("/blueRed")
                                      .then()
                                      .statusCode(404)
                      );
              
                      
              List<StubMapping> mappings = recorder.saveRecording().then().getRecording();
              
      }
  }

```

#### Scala Utilities
* PerformanceTest - Base Performance Test Class
* LocalPerformanceTest - Performance Test Class bootstrapping of start and shutdown local test application
* MockedLocalPerformanceTest - Performance Test Class bootstrapping of start and shutdown local test application and Wiremock
* PerformanceTestConfig - Performance Test Config

```scala
import com.github.mideo.apitestkit.AsciiArt
import io.gatling.core.Predef.{atOnceUsers, scenario}
import io.gatling.core.controller.inject.InjectionStep
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.core.Predef._
import org.junit.runner.RunWith




object FakeScenario extends TestScenario {
  override def scenarioBuilder(business: String, userName: String): ScenarioBuilder = {
    scenario(s"Fake Scenario $business")
      .exec(http("fake request").get("/"))
  }
}

class FakeLocalPerformanceTest
  extends LocalPerformanceTest{
  AsciiArt.draw("TestLibs")
  override val testScenarios: List[TestScenario] = List(FakeScenario)
  override val simulations: List[PopulationBuilder] = testScenarios map { _.scenarioBuilder("test", "simulation").inject(injection) }

  override def injection: InjectionStep = atOnceUsers(1)
  doSetUp()

  override def startApplication(): Unit = {}

  override def stopApplication(): Unit = {}
}

class FakeMockedPerformanceTest
  extends LocalPerformanceTest{
  AsciiArt.draw("TestLibs")
  override val testScenarios: List[TestScenario] = List(FakeScenario)
  override val simulations: List[PopulationBuilder] = testScenarios map { _.scenarioBuilder("test", "simulation").inject(injection) }
  
  ScalaStubBuilder.givenWiremockWillReturnCode(200)
  
  override def injection: InjectionStep = atOnceUsers(1)
  doSetUp()

  override def startApplication(): Unit = {}

  override def stopApplication(): Unit = {}
}

class FakePerformanceTest
  extends PerformanceTest{
  AsciiArt.draw("TestLibs")
  override val testScenarios: List[TestScenario] = List(FakeScenario)
  override val simulations: List[PopulationBuilder] = testScenarios map { _.scenarioBuilder("test", "simulation").inject(injection) }

  override def injection: InjectionStep = atOnceUsers(1)
  doSetUp()
}


object FakeRunner {
  def main(args:Array[String]): Unit ={
    
    PerformanceTestConfig.forUrl("performanceTest.url")
        .withSuccessfulRequestPercentage(99)
      .withRequestPerSecond(5)
    
    GatlingTestRunner.forSimulationClass(classOf[FakePerformanceTest].getName)
      .fromSourceDirectory("src/test")
      .saveResultDirectoryTo("build/reports/performanceTest")
      .run
    
  } 
}
```
