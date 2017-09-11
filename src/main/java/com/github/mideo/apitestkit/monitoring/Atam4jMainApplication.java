package com.github.mideo.apitestkit.monitoring;


import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import me.atam.atam4j.Atam4j;

import java.util.concurrent.TimeUnit;


public abstract class Atam4jMainApplication extends Application<Atam4jConfig> {

    public static String getAtam4JConfigFile() {
        return atam4JConfigFile;
    }

    public static void setAtam4JConfigFile(String fileName) {
        atam4JConfigFile = fileName;
    }

    public static void setSystemProperty(String property, String value) {
        System.setProperty(property, value);
    }

    private static String atam4JConfigFile = "atam4j-config.yml";


    @Override
    public void initialize(Bootstrap<Atam4jConfig> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
    }

    @Override
    public void run(Atam4jConfig configuration, Environment environment) throws Exception {

        Atam4j atam4j = new Atam4j.Atam4jBuilder(environment.jersey())
                .withUnit(TimeUnit.SECONDS)
                .withInitialDelay(1)
                .withPeriod(Long.valueOf(configuration.getPeriodInSeconds()))
                .build();
        atam4j.initialise();

    }
}
