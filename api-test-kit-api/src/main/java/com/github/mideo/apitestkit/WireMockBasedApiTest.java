package com.github.mideo.apitestkit;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.Before;



public abstract class WireMockBasedApiTest extends ApiTest {

    protected StubBuilder stubBuilder = new StubBuilder();
    @Before
    public void setupWireMock() {
        setupApi();
        stubBuilder.startWireMock();
        WireMockServer mockServer = stubBuilder.getWireMockServer();
        mockServer.resetMappings();
        mockServer.resetRequests();
        mockServer.resetScenarios();
    }
}
