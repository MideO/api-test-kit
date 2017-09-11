package tests;

import com.github.mideo.apitestkit.RestAssuredSpecFactory;
import io.restassured.RestAssured;
import io.restassured.internal.RequestSpecificationImpl;
import me.atam.atam4j.Monitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Monitor
public class RestAssuredSpecFactoryTest {
    @Test
    public void useRelaxedHTTPSValidation_ShouldSetRestAssuredToUseRelaxedHTTPSValidation() throws Exception {
        //When
        RestAssuredSpecFactory.useRelaxedHTTPSValidation();

        //Then
        assertEquals("ALLOW_ALL", RestAssured.config().getSSLConfig().getSSLSocketFactory().getHostnameVerifier().toString());
    }

    @Test
    public void port_shouldSetPort() throws Exception {
        //When
        RestAssuredSpecFactory.port(8080);

        //Then
        assertEquals(8080, RestAssured.port);
    }

    @Test
    public void givenARequestSpecification_shoutCreateRequestSpecification() throws Exception {
        //When
        assertTrue(RestAssuredSpecFactory.givenARequestSpecification().getClass().equals(RequestSpecificationImpl.class));
    }
}