package modhero.data;

import modhero.data.modules.Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Timetable}.
 */
public class TimetableTest {

    private Timetable timetable;
    private Module cs1010;
    private Module cs2040;
    private Module cs2100;

    @BeforeEach
    void setUp() {
        timetable = new Timetable(); // Use the no-argument constructor
        cs1010 = new Module("CS1010", "Programming Methodology", 4, "core", List.of());
        cs2040 = new Module("CS2040", "Data Structures", 4, "core", List.of("CS1010"));
        cs2100 = new Module("CS2100", "Computer Organisation", 4, "core", List.of("CS1010"));
    }

    @Test
    void testAddAndGetModules() {
        timetable.addModule(0, 0, cs1010);
        timetable.addModule(0, 1, cs2040);

        List<Module> term1Modules = timetable.getModules(0, 0);
        List<Module> term2Modules = timetable.getModules(0, 1);

        assertEquals(1, term1Modules.size());
        assertEquals("CS1010", term1Modules.get(0).getCode());
        assertEquals(1, term2Modules.size());
        assertEquals("CS2040", term2Modules.get(0).getCode());
    }

    @Test
    void testRemoveModuleRemovesCorrectly() {
        timetable.addModule(0, 0, cs1010);
        timetable.addModule(0, 1, cs2040);

        // Call removeModule with 0-based indexing
        boolean removed = timetable.removeModule(0, 0, "CS1010");
        assertTrue(removed, "Expected CS1010 to be removed");

        // CS1010 should be gone now
        assertTrue(timetable.getModules(0, 0).isEmpty());
        // CS2040 should still be there
        assertFalse(timetable.getModules(0, 1).isEmpty());
    }

    @Test
    void testRemoveModuleNonExistentReturnsFalse() {
        // Use 0-based indexing
        boolean result = timetable.removeModule(0, 0, "NON_EXISTENT");
        assertFalse(result, "Should return false when module does not exist");
    }

    @Test
    void testGetAllModulesAggregatesCorrectly() {
        timetable.addModule(0, 0, cs1010);
        timetable.addModule(0, 1, cs2040);
        timetable.addModule(1, 0, cs2100);

        List<Module> all = timetable.getAllModules();
        assertEquals(3, all.size());
        assertTrue(all.contains(cs1010));
        assertTrue(all.contains(cs2040));
        assertTrue(all.contains(cs2100));
    }

    @Test
    void testClearTimetableRemovesEverything() {
        timetable.addModule(0, 0, cs1010);
        timetable.addModule(0, 1, cs2040);

        timetable.clearTimetable();

        // Assuming NUM_YEARS and NUM_TERMS are 4
        // A better test would be to check getAllModules
        assertEquals(0, timetable.getAllModules().size(), "Timetable should be empty after clear");
    }

    @Test
    void testEmptyTimetableInitially() {
        // A simpler way to test this
        assertEquals(0, timetable.getAllModules().size(), "Timetable should start empty");
    }
}