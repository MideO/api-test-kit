package com.github.mideo.apitestkit;


import org.junit.Before;

public abstract class ApiTest extends TestApplication {

    @Before
    public void setupApi()  {
        if (applicationStarted) return;
        try {
            loadTestApplicationProperties();
            startApplication();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::stopApplication));
        applicationStarted = true;
    }

}