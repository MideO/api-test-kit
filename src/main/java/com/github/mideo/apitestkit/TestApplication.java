package com.github.mideo.apitestkit;

import java.util.Properties;

public abstract class TestApplication {
    protected static Properties properties = new Properties();
    protected abstract void startApplication();
    protected abstract void stopApplication();
    protected abstract void loadTestApplicationProperties();

    static boolean applicationStarted = false;
}
