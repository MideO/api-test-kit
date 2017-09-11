package com.github.mideo.apitestkit;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;


import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class StubBuilder {
    private ApiTestKitProperties properties = ApiTestKitProperties.create();
    private int WIREMOCK_PORT = properties.getInt("wiremock.port");
    private String WIREMOCK_HOST = properties.getString("wiremock.host");
    private WireMockConfiguration options = new WireMockConfiguration().port(WIREMOCK_PORT).bindAddress(WIREMOCK_HOST);

    static WireMockServer wireMockServer;

    public void disableRequestJournal() {
        options.disableRequestJournal();
        wireMockServer = new WireMockServer(options);
    }

    public void startWireMock() {
        startWireMock(WIREMOCK_PORT);
    }

    public void startWireMock(int port) {
        if (wireMockServer!= null && wireMockServer.isRunning()) return;

        options.port(port);
        wireMockServer = new WireMockServer(options);
        wireMockServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(wireMockServer::shutdownServer));
    }

    public StubBuilder givenWiremockServerResponse(MappingBuilder mapping, String jsonResponse, int code) {
        wireMockServer.stubFor(
                mapping.willReturn(aResponse()
                .withStatus(code)
                .withBody(jsonResponse))
        );
        return this;
    }

    public StubBuilder givenWiremockServerResponse(MappingBuilder mapping, String jsonResponse) {
        givenWiremockServerResponse(mapping, jsonResponse, 200);
        return this;
    }


    public StubBuilder givenWiremockServerResponse(MappingBuilder mapping) {
        wireMockServer.stubFor(
                mapping.willReturn(aResponse()
                        .withStatus(200)
                )
        );
        return this;
    }

    public StubBuilder givenWiremockWillReturnCode(int code) {
        wireMockServer.stubFor(any(urlMatching(".*"))
                .willReturn(aResponse().withStatus(code)));
        return this;
    }


}
