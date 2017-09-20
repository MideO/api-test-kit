package tests;


import com.github.mideo.apitestkit.JsonParser;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.scalatest.junit.JUnitSuite;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class JsonParserTest extends JUnitSuite {
    private static String givenString = "{\"abc\":\"123\"}";
    private static Map<String, String> expected = ImmutableMap.of("abc", "123");
    private Dummy d = new Dummy();

    static class Dummy {


        public String getName() {
            return name;
        }

        private String name;

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Dummy dummy = (Dummy) o;

            return name != null ? name.equals(dummy.name) : dummy.name == null;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "{\"name\":\"Dummy\"}";
        }
    }

    @Test
    public void createJsonMap_shouldCreateJsonMap() throws Exception {
        //When
        Map result = JsonParser.deserialize(givenString);

        //Then
        assertEquals(expected, result);
    }

    @Test
    public void toJsonString_ShouldConvertMapToString() throws Exception {
        //When
        String result = JsonParser.serialize(expected);

        //Then
        assertEquals(givenString, result);

    }

    @Test
    public void toJsonString_ShouldConvertClassToString() throws Exception {
        // Given
        d.setName("Dummy");
        // When
        String result = JsonParser.serialize(d);

        //Then
        assertEquals(d.toString(), result);

    }

    @Test
    public void deserialise_ShouldConvertClassToString() throws Exception {
        // Given
        d.setName("Dummy");

        //When
        Dummy result = JsonParser.deserialize(d.toString(), Dummy.class);

        //Then
        assertTrue(d.equals(result));
        assertEquals(d.getName(), result.getName());


    }

}