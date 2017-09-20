package tests.monitoring;

import com.github.mideo.apitestkit.monitoring.Atam4jMainApplication;
import org.junit.Test;
import org.scalatest.junit.JUnitSuite;

import static org.junit.Assert.assertEquals;

public class Atam4jMainApplicationTest extends JUnitSuite {
    @Test
    public void setAtam4JConfigFile() throws Exception {
        //When
        Atam4jMainApplication.setAtam4JConfigFile("abcd");

        //Then
        assertEquals("abcd", Atam4jMainApplication.getAtam4JConfigFile());
    }

    @Test
    public void setSystemProperty() throws Exception {
        //When
        Atam4jMainApplication.setSystemProperty("foo", "bar");

        //Then
        assertEquals("bar", System.getProperty("foo"));
    }

}