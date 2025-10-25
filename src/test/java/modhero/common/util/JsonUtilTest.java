package modhero.common.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JsonUtilTest {
    private final JsonUtil extractor = new JsonUtil();

    @Test
    void getArgReturnsNullWhenKeyMissing() {
        String json = "{\"a\":1}";
        assertNull(extractor.getArg(json, "noKey"));
    }

    @Test
    void getArgHandleWhitespacesBetweenKeyAndData() {
        String key = "moduleCode";
        String moduleCode = "CS2113";
        String json = String.format("{\"%s\"   :    \"%s\"}", key, moduleCode);
        assertEquals(moduleCode, extractor.getArg(json, key));
    }

    @Test
    void getArgExtractsRawValue() {
        String key = "moduleCode";
        String moduleCode = "CS2113";
        String json = String.format("{\"%s\":%s}, {\"anotherKey\":\"data\"}", key, moduleCode);
        assertNull(extractor.getArg(json, key));
    }

    @Test
    void getArgExtractsString() {
        String key = "title";
        String title = "Software Engineering & Object-Oriented Programming";
        String json = String.format("{\"%s\":\"%s\"}", key, title);
        assertEquals(title, extractor.getArg(json, key));
    }

    @Test
    void getArgExtractsObject() {
        String key = "prereqTree";
        String object = "{\"CS113\":D,\"CS113S\":D}";
        String json = String.format("{\"%s\":%s}", key, object);
        assertEquals(object, extractor.getArg(json, key));
    }

    @Test
    void getArgHandlesNestedObjects() {
        String key = "prereqTree";
        String object = "{\"or\":[\"CS1010:D\",\"CS1010E:D\",\"CS1010X:D\",\"CS1101S:D\",\"CS1010S:D\",\"CS1010J:D\",\"CS1010A:D\",\"UTC2851:D\"]}";
        String json = String.format("{\"%s\":%s}", key, object);
        assertEquals(object, extractor.getArg(json, key));
    }

    @Test
    void getArgReturnsNullOnIncompleteBraces() {
        String key = "prereqTree";
        String json = String.format("\"%s\":{\"key1\":\"data1\", {\"key2\":\"data2\"}", key);
        assertNull(extractor.getArg(json, key));
    }
}
