package com.github.mideo.apitestkit;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class StubBuilder {

    private int WIREMOCK_PORT;
    private String WIREMOCK_HOST;
    private String WIREMOCK_ROOT_DIRECTORY;
    private WireMockConfiguration options;
    private static WireMockServer wireMockServer;

    public StubBuilder() {
        ApiTestKitProperties properties = ApiTestKitProperties.create();
        WIREMOCK_PORT = properties.getInt("wiremock.port");
        WIREMOCK_HOST = properties.getString("wiremock.host");
        WIREMOCK_ROOT_DIRECTORY = properties.getString("wiremock.root.directory", "src/test/resources");
        options = new WireMockConfiguration().port(WIREMOCK_PORT).bindAddress(WIREMOCK_HOST).withRootDirectory(WIREMOCK_ROOT_DIRECTORY);
    }

    public WireMockServer getWireMockServer() {
        return wireMockServer;
    }

    public StubBuilder disableRequestJournal() {
        options.disableRequestJournal();
        wireMockServer = new WireMockServer(options);
        return this;
    }

    public StubBuilder startWireMock() {
        if (wireMockIsRunning()) return this;
        wireMockServer = new WireMockServer(options);
        wireMockServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(wireMockServer::shutdownServer));
        return this;
    }


    boolean wireMockIsRunning(){
        return wireMockServer != null && wireMockServer.isRunning();
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

    public StubRecorder recorder(String host, int port) {
        return new StubRecorder(wireMockServer, options.proxyVia(host, port));
    }

}
