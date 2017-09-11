package com.github.mideo.apitestkit;
import org.junit.Before;



public abstract class WireMockBasedApiTest extends ApiTest {

    protected StubBuilder stubServer = new StubBuilder();
    @Before
    public void setupWireMock() {
        setupApi();
        stubServer.startWireMock();
        StubBuilder.wireMockServer.resetMappings();
        StubBuilder.wireMockServer.resetRequests();
        StubBuilder.wireMockServer.resetScenarios();
    }
}
