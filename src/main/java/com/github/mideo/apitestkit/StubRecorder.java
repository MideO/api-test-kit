package com.github.mideo.apitestkit;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
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
                        String fileName = mapping.getName() + "-" + mapping.getId() + ".json";
                        byte[] data = mapping.toString().getBytes();

                        Files.write(Paths.get(mappingsDirectory + fileName), data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        return this;
    }


    private void startRecording() {
        String root = options.proxyVia(options.proxyVia()).filesRoot().getPath();
        try {
            Files.createDirectories(Paths.get(root));
            Files.createDirectories(Paths.get(root + "/mappings"));
            Files.createDirectories(Paths.get(root + "/__files"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        wireMockServer.startRecording(options.proxyVia().toString());

    }

    private void stopRecording() {
        recording.addAll(wireMockServer.stopRecording().getStubMappings());
    }
}
