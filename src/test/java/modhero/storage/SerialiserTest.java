package modhero.storage;

import modhero.exception.CorruptedDataFileException; // Must import the exception
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Serialiser}.
 */
public class SerialiserTest {

    private Serialiser serialiser;

    @BeforeEach
    void setUp() {
        serialiser = new Serialiser();
    }

    // --- serialiseMessage() ---
    @Test
    void testSerialiseMessage_basic() {
        String result = serialiser.serialiseMessage("CS1010");
        assertEquals("6#CS1010|", result);
    }

    @Test
    void testSerialiseMessage_emptyString() {
        String result = serialiser.serialiseMessage("");
        assertEquals("0#|", result);
    }

    // --- serialiseList() ---
    @Test
    void testSerialiseList_basic() {
        List<String> list = List.of("CS1010", "CS2040");
        String result = serialiser.serialiseList(list);

        // This test was correct:
        // "6#CS1010|" + "7#CS2040|" = "6#CS1010|7#CS2040|" (Length 14)
        // serialiseMessage("6#CS1010|7#CS2040|") = "14#6#CS1010|7#CS2040||"
        assertEquals("14#6#CS1010|7#CS2040||", result);
    }

    @Test
    void testSerialiseList_emptyList() {
        String result = serialiser.serialiseList(List.of());
        // serialiseList(empty) calls serialiseMessage("") which returns "0#|"
        assertEquals("0#|", result, "Empty list should serialise to what an empty string serialises to");
    }

    // --- deserialiseMessage() ---
    @Test
    void testDeserialiseMessage_basic() {
        String serialised = "6#CS1010|7#CS2040|";
        List<String> result = serialiser.deserialiseMessage(serialised);
        assertEquals(List.of("CS1010", "CS2040"), result);
    }

    @Test
    void testDeserialiseMessage_handlesCorruptedDelimiter() {
        String corrupted = "6CS1010|"; // missing #
        // deserialiseMessage returns null on failure
        List<String> result = serialiser.deserialiseMessage(corrupted);
        assertNull(result, "Should return null when missing delimiter");
    }

    @Test
    void testDeserialiseMessage_handlesInvalidLength() {
        String corrupted = "X#CS1010|"; // X is not a number
        // deserialiseMessage returns null on failure
        List<String> result = serialiser.deserialiseMessage(corrupted);
        assertNull(result, "Should return null when length is invalid");
    }

    @Test
    void testDeserialiseMessage_handlesTruncatedString() {
        String incomplete = "6#CS10"; // incomplete message (missing characters)
        // deserialiseMessage returns null on failure
        List<String> result = serialiser.deserialiseMessage(incomplete);
        assertNull(result, "Should return null when message truncated");
    }

    // --- deserialiseList() ---

    @Test
    void testDeserialiseList_success() {
        List<String> serialisedList = List.of("6#CS1010|", "7#CS2040|");

        // Use assertDoesNotThrow to handle the checked exception in a success case
        List<List<String>> result = assertDoesNotThrow(() -> {
            return serialiser.deserialiseList(serialisedList);
        });

        assertEquals(2, result.size());
        assertEquals(List.of("CS1010"), result.get(0));
        assertEquals(List.of("CS2040"), result.get(1));
    }

    @Test
    void testDeserialiseList_emptyInput() {
        // Use assertDoesNotThrow for this success case as well
        List<List<String>> result = assertDoesNotThrow(() -> {
            return serialiser.deserialiseList(List.of());
        });

        assertTrue(result.isEmpty(), "Empty input should produce empty output");
    }

    @Test
    void testDeserialiseList_throwsCorruptedDataFileException() {
        // This is the new test for the failure case.
        // We provide a list where one of the items is corrupted.
        List<String> corruptedList = List.of("6#CS1010|", "7#CS2040"); // Missing final '|'

        // `deserialiseMessage("7#CS2040")` will return null.
        // `deserialiseList` will catch this null and throw the exception.

        // Use assertThrows to verify that the *correct* exception is thrown.
        assertThrows(CorruptedDataFileException.class, () -> {
            serialiser.deserialiseList(corruptedList);
        });
    }
}
