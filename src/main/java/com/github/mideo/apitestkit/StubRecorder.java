package com.github.mideo.apitestkit;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;


public class StubRecorder {

    private WireMockServer wireMockServer;



    private List<StubMapping> recording = new ArrayList<>();
    private WireMockConfiguration options;

    StubRecorder(WireMockServer wireMockServer, WireMockConfiguration options) {
        this.wireMockServer = wireMockServer;
        this.options = options;
    }

    public List<StubMapping> getRecording() {
        return recording;
    }

    public StubRecorder record(Runnable runnable) {
        startRecording();
        try {
            runnable.run();
        } finally {
            stopRecording();

        }
        return this;

    }

    public StubRecorder then(){
        return this;
    }

    public StubRecorder saveRecording() {
        recording.forEach(
                mapping -> {
                    try {
                        String mappingsDirectory = options.proxyVia(options.proxyVia()).filesRoot().getPath() + "/mappings/";
                        String filesDirectory = options.proxyVia(options.proxyVia()).filesRoot().getPath() + "/__files/";
                        String fileName = mapping.getName() + "-" + mapping.getId();
                        ResponseDefinition resp = mapping.getResponse();
                        mapping.setResponse(ResponseDefinitionBuilder.responseDefinition().withStatus(resp.getStatus()).withBodyFile(fileName).withHeaders(resp.getHeaders()).build());
                        byte[] mappingBytes = mapping.toString().getBytes();

                        Files.write(Paths.get(filesDirectory + fileName), resp.getByteBody(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                        Files.write(Paths.get(mappingsDirectory + fileName+ ".json"), mappingBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        return this;
    }


    private void startRecording() {
        String root = options.proxyVia(options.proxyVia()).filesRoot().getPath();
        createRecordingDirectoriesIfNotExist(root);
        wireMockServer.startRecording(options.proxyVia().toString());

    }

    private void stopRecording() {
        recording.addAll(wireMockServer.stopRecording().getStubMappings());
    }

    private void createRecordingDirectoriesIfNotExist(String root) {
        try {
            if(Files.notExists(Paths.get(root))) Files.createDirectories(Paths.get(root));
            if(Files.notExists(Paths.get(root+ "/mappings"))) Files.createDirectories(Paths.get(root+ "/mappings"));
            if(Files.notExists(Paths.get(root+ "/__files"))) Files.createDirectories(Paths.get(root+ "/__files"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
