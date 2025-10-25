// NusmodsApiClientTest.java
package modhero.data.nusmods;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NusmodsAPIClientTest {
    private final NusmodsAPIClient client = new NusmodsAPIClient();

    @Test
    void testFetchModuleDataSafelyValidModule() {
        String moduleCode = "CS2113";
        String acadYear = "2024-2025";
        String result = client.fetchModuleDataSafely(acadYear, moduleCode);
        assertTrue(result.contains(moduleCode));
        assertTrue(result.contains(acadYear.replace("-","/")));
    }

    @Test
    void fetchModuleDataSafelyReturnsNullOnInvalidAddress() {
        assertNull(client.fetchModuleDataSafely("invalid", "XXXX"));
    }
}
