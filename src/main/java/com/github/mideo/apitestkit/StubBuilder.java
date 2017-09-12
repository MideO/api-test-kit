package com.github.mideo.apitestkit;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.recording.SnapshotRecordResult;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class StubBuilder {

    private ApiTestKitProperties properties = ApiTestKitProperties.create();
    private int WIREMOCK_PORT = properties.getInt("wiremock.port");
    private String WIREMOCK_HOST = properties.getString("wiremock.host");
    private WireMockConfiguration options = new WireMockConfiguration().port(WIREMOCK_PORT).bindAddress(WIREMOCK_HOST);
    private static WireMockServer wireMockServer;

    public WireMockServer getWireMockServer() {
        return wireMockServer;
    }

    public StubBuilder disableRequestJournal() {
        options.disableRequestJournal();
        wireMockServer = new WireMockServer(options);
        return this;
    }

    public StubBuilder proxyVia(String host, int port) {
        options.proxyVia(host, port);


        if (!wireMockIsRunning()) {
            wireMockServer = new WireMockServer(options);
        }else{

            wireMockServer.startRecording(options.proxyVia().toString());

        }
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

    public StubBuilder startRecording() {
        wireMockServer.startRecording(options.proxyVia().toString());
        return this;
    }


    public SnapshotRecordResult stopRecording() {
        options.filesRoot().child("mappings").createIfNecessary();
        return wireMockServer.stopRecording();
    }


}
