package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Test;

import com.github.mideo.apitestkit.ApiTestKitProperties;
import com.github.mideo.apitestkit.JsonParser;
import com.github.mideo.apitestkit.RestAssuredSpecFactory;
import com.github.mideo.apitestkit.WireMockBasedApiTest;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.junit.Assert.assertTrue;


public class WireMockBaseApiTestTest extends WireMockBasedApiTest {
    private static ApiTestKitProperties properties = ApiTestKitProperties.create();


    private static int WIREMOCK_PORT = properties.getInt("wiremock.port");
    private static String WIREMOCK_HOST = properties.getString("wiremock.host");
    private String wiremockUrl = String.format("http://%s:%s", WIREMOCK_HOST,WIREMOCK_PORT);

    private RequestSpecification requestSpecification = RestAssuredSpecFactory.givenARequestSpecification().baseUri(wiremockUrl);
    private String payload = JsonParser.serialize(ImmutableMap.of("name", "testApi"));

    private boolean propertiesLoaded;
    private boolean appStarted;

    public WireMockBaseApiTestTest() throws JsonProcessingException {
    }

    @Test
    public void properties_ShouldBeInitialised() throws Exception {
        //expect
        assertTrue(appStarted);
        assertTrue(propertiesLoaded);
    }


    @Test
    public void givenWiremockServerResponseWithPathAndRequestAndBodyAndStatusCode_ShouldSetWireMockMapping() throws Exception {
        //When
        stubServer.givenWiremockServerResponse(get("/api"), payload, 500);

        //Then
        requestSpecification.when()
                .get("/api").then()
                .statusCode(500)
                .body(Matchers.is(payload));
    }


    @Test
    public void givenWiremockServerResponseWithPathAndRequestAndBody_ShouldSetWireMockMapping() throws Exception {
        //When
        stubServer.givenWiremockServerResponse(get("/api"), payload);

        //Then
        requestSpecification.when()
                .get("/api").then()
                .statusCode(200)
                .body(Matchers.is(payload));
    }


    @Test
    public void givenWiremockServerResponseWithPathAndRequest_ShouldSetWireMockMapping() throws Exception {
        //When
        stubServer.givenWiremockServerResponse(get("/api"));

        //Then
        requestSpecification.when()
                .get("/api").then()
                .statusCode(200);
    }




    @Test
    public void givenWiremockWillReturnCode_ShouldSetWireMockMapping() throws Exception {
        //When
        stubServer.givenWiremockWillReturnCode(202);

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


    @Override
    protected void startApplication() {
        appStarted = true;
    }

    @Override
    protected void stopApplication() {

    }

    @Override
    protected void loadTestApplicationProperties() {
        propertiesLoaded = true;
    }
}