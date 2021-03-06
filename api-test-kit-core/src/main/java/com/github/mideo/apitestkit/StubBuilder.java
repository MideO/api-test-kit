package com.github.mideo.apitestkit;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class StubBuilder {

    private WireMockConfiguration options;
    private static WireMockServer wireMockServer;

    public StubBuilder() {
        ApiTestKitProperties properties = ApiTestKitProperties.create();
        int WIREMOCK_PORT = properties.getInt("wiremock.port", 9999);
        String WIREMOCK_HOST = properties.getString("wiremock.host", "localhost");
        String WIREMOCK_ROOT_DIRECTORY = properties.getString("wiremock.root.directory", "src/test/resources");
        options = wireMockConfig().port(WIREMOCK_PORT).bindAddress(WIREMOCK_HOST).withRootDirectory(WIREMOCK_ROOT_DIRECTORY);
    }


    public WireMockServer getWireMockServer() {
        return wireMockServer;
    }

    public StubBuilder disableRequestJournal() {
        options.disableRequestJournal();
        return this;
    }

    public StubBuilder enableResponseTemplateTransformerForAllResponse() {
        options.extensions(new ResponseTemplateTransformer(true));
        return this;
    }

    public StubBuilder enableResponseTemplateTransformer() {
        options.extensions(new ResponseTemplateTransformer(false));
        return this;
    }

    public WireMockConfiguration getOptions(){
        return options;
    }

    public StubBuilder startWireMock() {
      return startWireMock(options);
    }

    public StubBuilder startWireMock(WireMockConfiguration opt) {
        if (wireMockIsRunning()) return this;
        wireMockServer = new WireMockServer(opt);
        wireMockServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(wireMockServer::shutdownServer));
        return this;
    }

    public StubBuilder shutdownWireMock(WireMockConfiguration opt) {
        if (wireMockIsRunning()) wireMockServer.shutdownServer();
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

    public StubBuilder givenWiremockServerResponse(MappingBuilder mapping, ResponseDefinitionBuilder responseDefinitionBuilder) {
        wireMockServer.stubFor(mapping.willReturn(responseDefinitionBuilder));
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
