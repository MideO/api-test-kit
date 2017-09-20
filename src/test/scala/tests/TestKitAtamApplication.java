package tests;

import com.github.mideo.apitestkit.monitoring.Atam4jMainApplication;

public class TestKitAtamApplication extends Atam4jMainApplication {
    public static void main(String[] args) throws Exception {
        args = new String[]{"server", "atam4j-config.yml"};

        new TestKitAtamApplication().run(args);
    }
}
