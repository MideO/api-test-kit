package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.mideo.apitestkit.*;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.google.common.collect.ImmutableMap;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.scalatest.junit.JUnitSuite;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class WireMockBaseApiTestSpec extends JUnitSuite {

    private FugazziWireMockBasedApiTest fugazziWireMockBasedApiTest;
    private static ApiTestKitProperties properties = ApiTestKitProperties.create();
    private static int WIREMOCK_PORT = properties.getInt("wiremock.port");
    private static String WIREMOCK_HOST = properties.getString("wiremock.host");
    private String wiremockUrl = String.format("http://%s:%s", WIREMOCK_HOST, WIREMOCK_PORT);

    private RequestSpecification requestSpecification = RestAssuredSpecFactory.givenARequestSpecification().baseUri(wiremockUrl);
    private String payload = JsonParser.serialize(ImmutableMap.of("name", "testApi"));

    public WireMockBaseApiTestSpec() throws JsonProcessingException {
        fugazziWireMockBasedApiTest = new FugazziWireMockBasedApiTest();
        fugazziWireMockBasedApiTest.setupWireMock();
    }

    @Test
    public void properties_ShouldBeInitialised() throws Exception {
        //expect
        assertEquals("localhost", properties.getString("wiremock.host"));
        assertEquals(9999, properties.getInt("wiremock.port"));
        assertTrue(fugazziWireMockBasedApiTest.isApplicationStarted());
    }


    @Test
    public void itShouldRecordProxyMappings() throws Exception {
        //Given
        StubRecorder recorder = fugazziWireMockBasedApiTest.getStubBuilder().recorder("https://google.com/bin", 443).record(
                () -> requestSpecification
                        .when()
                        .get("/blueRed")
                        .then()
                        .statusCode(404)
        );

        //When
        List<StubMapping> mappings = recorder
                .saveRecording()
                .then()
                .getRecording();

        //Then
        assertEquals(1, mappings.size());
        assertEquals("/blueRed", mappings.get(0).getRequest().getUrl());
        assertEquals(RequestMethod.fromString("GET"), mappings.get(0).getRequest().getMethod());

    }


    @Test
    public void givenWiremockServerResponseWithPathAndRequestAndBodyAndStatusCode_ShouldSetWireMockMapping() throws Exception {
        //When
        fugazziWireMockBasedApiTest.getStubBuilder().givenWiremockServerResponse(get("/api"), payload, 500);

        //Then
        requestSpecification.when()
                .get("/api").then()
                .statusCode(500)
                .body(Matchers.is(payload));
    }


    @Test
    public void givenWiremockServerResponseWithPathAndRequestAndBody_ShouldSetWireMockMapping() throws Exception {
        //When
        fugazziWireMockBasedApiTest.getStubBuilder().givenWiremockServerResponse(get("/api"), payload);

        //Then
        requestSpecification.when()
                .get("/api").then()
                .statusCode(200)
                .body(Matchers.is(payload));
    }


    @Test
    public void givenWiremockServerResponseWithPathAndRequest_ShouldSetWireMockMapping() throws Exception {
        //When
        fugazziWireMockBasedApiTest.getStubBuilder().givenWiremockServerResponse(get("/api"));

        //Then
        requestSpecification.when()
                .get("/api").then()
                .statusCode(200);
    }


    @Test
    public void givenWiremockWillReturnCode_ShouldSetWireMockMapping() throws Exception {
        //When
        fugazziWireMockBasedApiTest.getStubBuilder().givenWiremockWillReturnCode(202);

        //Then
        requestSpecification.when()
                .put("/bar").then()
                .statusCode(202);

        requestSpecification.when()
                .get("/foo").then()
                .statusCode(202);

        requestSpecification.when()
                .delete("/foo").then()
                .statusCode(202);
    }

    @Test
    public void isShouldSupportChainedRequestMocks() throws Exception {
        //When
        fugazziWireMockBasedApiTest.getStubBuilder().givenWiremockWillReturnCode(202)
                .givenWiremockServerResponse(post("/api"), payload)
                .givenWiremockServerResponse(put("/api"), "Internal Server Error", 500);


        //Then
        requestSpecification.when()
                .put("/bar").then()
                .statusCode(202);

        requestSpecification.when()
                .get("/foo").then()
                .statusCode(202);

        requestSpecification.when()
                .delete("/foo").then()
                .statusCode(202);

        requestSpecification.when()
                .post("/api").then()
                .statusCode(200)
                .body(Matchers.is(payload));

        requestSpecification.when()
                .put("/api").then()
                .statusCode(500)
                .body(Matchers.is("Internal Server Error"));

    }

    static class FugazziWireMockBasedApiTest extends WireMockBasedApiTest {
        StubBuilder getStubBuilder() {
            return stubBuilder;
        }
        boolean isApplicationStarted(){
            return applicationStarted;
        }

        @Override
        protected void startApplication() {

        }
        @Override
        protected void stopApplication() {

        }

        @Override
        protected void loadTestApplicationProperties() {

        }

    }
}