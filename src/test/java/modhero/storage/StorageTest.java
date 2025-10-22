package modhero.storage;

import modhero.data.major.Major;
import modhero.data.modules.Module;
import modhero.exception.CorruptedDataFileException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Storage}.
 */
public class StorageTest {

    // @TempDir creates a temporary directory for tests and cleans it up afterward.
    // This is great for testing directory creation.
    @TempDir
    Path tempDir;

    private File tempFile;
    private Storage storage;
    private Serialiser serialiser;

    @BeforeEach
    void setUp() throws IOException {
        // We still use a specific temp file for most save/load tests
        tempFile = File.createTempFile("modhero_test", ".txt");
        storage = new Storage(tempFile.getAbsolutePath());
        serialiser = new Serialiser();
    }

    @AfterEach
    void tearDown() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    // --- Tests for save() and load() ---

    @Test
    void saveAndLoad_roundTrip_handlesMultipleLines() {
        String testContent = "This is line 1\nThis is line 2\nThis is line 3";
        storage.save(testContent);

        List<String> expected = List.of("This is line 1", "This is line 2", "This is line 3");
        List<String> loaded = storage.load();

        assertEquals(expected, loaded);
    }

    @Test
    void save_overwritesExistingFile() {
        storage.save("First content");
        storage.save("Second content"); // This should overwrite

        List<String> loaded = storage.load();

        assertEquals(1, loaded.size(), "File should only have one line");
        assertEquals("Second content", loaded.get(0));
    }

    @Test
    void save_createsParentDirectories() {
        // Use the @TempDir path to create a nested, non-existent directory
        Path nestedFilePath = tempDir.resolve("new/nested/dir/testfile.txt");
        Storage nestedStorage = new Storage(nestedFilePath.toString());

        // We are testing the public `save` method,
        // which should *cause* the private `ensureFileDirectoryExist` to run.
        nestedStorage.save("test data");

        assertTrue(Files.exists(nestedFilePath.getParent()), "Parent directories should be created");
        assertTrue(Files.exists(nestedFilePath), "File should be created");
    }

    @Test
    void load_nonExistentFile_returnsEmptyList() {
        // Create a storage object pointing to a file that will be deleted
        File nonExistentFile = new File(tempDir.resolve("nonexistent.txt").toString());
        Storage nonExistentStorage = new Storage(nonExistentFile.getAbsolutePath());

        // Ensure it doesn't exist
        if (nonExistentFile.exists()) {
            nonExistentFile.delete();
        }

        // load() should call ensureFileExist(), creating an empty file,
        // and then read from it, returning an empty list.
        List<String> result = nonExistentStorage.load();

        assertNotNull(result, "Load should never return null");
        assertTrue(result.isEmpty(), "Loading a non-existent file should result in an empty list");
    }

    @Test
    void load_unreadableFile_returnsEmptyList() {
        // This tests the catch(IOException) block in load()
        storage.save("test data");

        // Make the file unreadable. This will cause new Scanner(file) to throw FileNotFoundException
        if (tempFile.setReadable(false)) {
            List<String> result = storage.load();
            assertNotNull(result, "Load should never return null");
            assertTrue(result.isEmpty(), "Should return empty list if file is unreadable");

            // Clean up
            tempFile.setReadable(true);
        } else {
            System.err.println("Warning: Could not set file to unreadable. Skipping test load_unreadableFile_returnsEmptyList.");
        }
    }

    // --- Tests for loadAllModulesData() ---

    @Test
    void loadAllModulesData_success() throws IOException {
        // A. Create the mock file content
        // Line 1: CS2040, prereq CS1010
        String prereq1 = serialiser.serialiseMessage("CS1010");
        List<String> module1Args = List.of("CS2040", "Data Structures", "4", "core", prereq1);
        String line1 = serialiser.serialiseList(module1Args);

        // Line 2: CS1010, no prereq
        String noPrereq = serialiser.serialiseMessage("");
        List<String> module2Args = List.of("CS1010", "Programming", "4", "core", noPrereq);
        String line2 = serialiser.serialiseList(module2Args);

        // B. Save the content to the file
        storage.save(line1 + "\n" + line2);

        // C. Run the method and assert
        Map<String, Module> allModulesData = new HashMap<>();
        assertDoesNotThrow(() -> storage.loadAllModulesData(allModulesData));

        // Should have 4 entries: 2 modules added by code and by name
        assertEquals(4, allModulesData.size());

        Module cs2040 = allModulesData.get("CS2040");
        assertNotNull(cs2040);
        assertEquals("Data Structures", cs2040.getName());
        assertEquals(4, cs2040.getMc());
        assertEquals(List.of("CS1010"), cs2040.getPrerequisites());

        // Check that it was also added by name
        assertEquals(cs2040, allModulesData.get("Data Structures"));
    }

    @Test
    void loadAllModulesData_throwsCorruptedDataFileException() {
        storage.save("This is not valid serialised data");

        Map<String, Module> map = new HashMap<>();
        // This test correctly asserts that the *expected* exception is thrown
        assertThrows(CorruptedDataFileException.class, () -> {
            storage.loadAllModulesData(map);
        });
    }

    @Test
    void loadAllModulesData_skipsOnWrongArgumentCount() {
        // Create a line with only 3 arguments instead of 5
        List<String> moduleArgs = List.of("CS1010", "Programming", "4");
        String line1 = serialiser.serialiseList(moduleArgs);
        storage.save(line1);

        Map<String, Module> map = new HashMap<>();
        // The method should log a warning and 'break', not throw an exception
        assertDoesNotThrow(() -> storage.loadAllModulesData(map));

        assertTrue(map.isEmpty(), "Module with wrong arg count should be skipped");
    }

    @Test
    void loadAllModulesData_skipsOnInvalidModuleCredit() {
        // Create a line where MC is "four", not "4"
        String noPrereq = serialiser.serialiseMessage("");
        List<String> moduleArgs = List.of("CS1010", "Programming", "four", "core", noPrereq);
        String line1 = serialiser.serialiseList(moduleArgs);
        storage.save(line1);

        Map<String, Module> map = new HashMap<>();
        // The method catches NumberFormatException and logs, not throws
        assertDoesNotThrow(() -> storage.loadAllModulesData(map));

        assertTrue(map.isEmpty(), "Module with invalid MC should be skipped");
    }

    // --- Tests for loadAllMajorsData() ---

    @Test
    void loadAllMajorsData_success() {
        // A. Setup: We need a pre-filled allModulesData map
        Map<String, Module> allModulesData = new HashMap<>();
        Module cs1010 = new Module("CS1010", "Programming", 4, "core", List.of());
        Module cs2040 = new Module("CS2040", "Data Structures", 4, "core", List.of("CS1010"));
        allModulesData.put("CS1010", cs1010);
        allModulesData.put("CS2040", cs2040);

        // B. Create the mock file content
        // Line 1: Computer Science, requires CS1010 and CS2040
        String reqModules = serialiser.serialiseMessage("CS1010") + serialiser.serialiseMessage("CS2040");
        List<String> majorArgs = List.of("Computer Science", "CS", reqModules);
        String line1 = serialiser.serialiseList(majorArgs);
        storage.save(line1);

        // C. Run the method and assert
        Map<String, Major> allMajorsData = new HashMap<>();
        assertDoesNotThrow(() -> storage.loadAllMajorsData(allModulesData, allMajorsData));

        // Should have 2 entries: added by full name and by abbreviation
        assertEquals(2, allMajorsData.size());

        Major csMajor = allMajorsData.get("CS");
        assertNotNull(csMajor);
        assertEquals("Computer Science", csMajor.getName());
        assertEquals(csMajor, allMajorsData.get("Computer Science"));

        // Check that the modules were correctly pulled from the allModulesData map
        assertEquals(2, csMajor.getModules().size());
        assertTrue(csMajor.getModules().getList().contains(cs1010));
        assertTrue(csMajor.getModules().getList().contains(cs2040));
    }

    @Test
    void loadAllMajorsData_skipsMissingModules() {
        // A. Setup: allModulesData is EMPTY
        Map<String, Module> allModulesData = new HashMap<>();

        // B. Create file: Major requires "CS1010", but it's not in the map
        String reqModules = serialiser.serialiseMessage("CS1010");
        List<String> majorArgs = List.of("Computer Science", "CS", reqModules);
        String line1 = serialiser.serialiseList(majorArgs);
        storage.save(line1);

        // C. Run and assert
        Map<String, Major> allMajorsData = new HashMap<>();
        assertDoesNotThrow(() -> storage.loadAllMajorsData(allModulesData, allMajorsData));

        // The major is still created
        assertEquals(2, allMajorsData.size());
        Major csMajor = allMajorsData.get("CS");
        assertNotNull(csMajor);

        // But its module list is empty, because CS1010 couldn't be found
        assertTrue(csMajor.getModules().isEmpty(), "Module list should be empty if modules aren't found");
    }

    @Test
    void loadAllMajorsData_throwsCorruptedDataFileException() {
        storage.save("This is not valid serialised data");

        Map<String, Module> modules = new HashMap<>();
        Map<String, Major> majors = new HashMap<>();

        assertThrows(CorruptedDataFileException.class, () -> {
            storage.loadAllMajorsData(modules, majors);
        });
    }

    @Test
    void loadAllMajorsData_skipsOnWrongArgumentCount() {
        // Create a line with only 2 arguments instead of 3
        List<String> majorArgs = List.of("Computer Science", "CS");
        String line1 = serialiser.serialiseList(majorArgs);
        storage.save(line1);

        Map<String, Module> modules = new HashMap<>();
        Map<String, Major> majors = new HashMap<>();

        // The method should log a warning and 'break', not throw an exception
        assertDoesNotThrow(() -> storage.loadAllMajorsData(modules, majors));

        assertTrue(majors.isEmpty(), "Major with wrong arg count should be skipped");
    }
}
