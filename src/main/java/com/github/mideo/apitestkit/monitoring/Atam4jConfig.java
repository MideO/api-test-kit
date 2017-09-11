package com.github.mideo.apitestkit.monitoring;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class Atam4jConfig extends Configuration {

    @JsonProperty
    private String periodInSeconds;

    public String getPeriodInSeconds() {
        return periodInSeconds;
    }
}
