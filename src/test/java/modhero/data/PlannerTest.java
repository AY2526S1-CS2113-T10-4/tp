package modhero.data;

import modhero.data.modules.Module;
import modhero.data.modules.ModuleList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Planner}.
 */
public class PlannerTest {

    private Timetable timetable;
    private ModuleList coreList;
    private ModuleList electiveList;

    @BeforeEach
    void setUp() {
        timetable = new Timetable();

        coreList = new ModuleList();
        electiveList = new ModuleList();

        // Level 2 dependency
        coreList.add(new Module("CS2040", "Data Structures", 4, "core", List.of("CS1231")));

        // Level 0 dependencies (no prereqs)
        coreList.add(new Module("CS1010", "Programming Methodology", 4, "core", List.of()));
        electiveList.add(new Module("CS2100", "Computer Organisation", 4, "elective", List.of()));

        // Level 1 dependencies
        coreList.add(new Module("CS1231", "Discrete Structures", 4, "core", List.of("CS1010")));
        electiveList.add(new Module("CS3243", "AI", 4, "elective", List.of("CS2100")));

        // The internal moduleList (insertion order) will be:
        // [CS2040, CS1010, CS1231, CS2100, CS3243]
    }

    @Test
    void testAddToTimetablePlacesModulesCorrectly() {
        Planner planner = new Planner(timetable, coreList, electiveList);
        planner.planTimeTable(); // Runs topological sort + adds to timetable

        int totalModules = timetable.getAllModules().size();
        assertEquals(5, totalModules, "All modules should be placed in timetable");

        // Level 0 modules
        assertEquals("CS1010", timetable.getModules(0, 0).get(0).getCode());
        assertEquals("CS2100", timetable.getModules(0, 1).get(0).getCode());

        // Level 1 modules
        assertEquals("CS1231", timetable.getModules(0, 2).get(0).getCode());
        assertEquals("CS3243", timetable.getModules(0, 3).get(0).getCode());

        // Level 2 module
        // (i=4) -> year = (4/4) % 4 = 1, term = 4 % 4 = 0
        assertEquals("CS2040", timetable.getModules(1, 0).get(0).getCode());
    }

    @Test
    void testEmptyListsHandledGracefully() {
        // This test was logically OK, but had weak assertions.
        timetable = new Timetable(); // Use a fresh timetable
        Planner planner = new Planner(timetable, new ModuleList(), new ModuleList());

        // We can assert that it doesn't throw an exception
        assertDoesNotThrow(() -> {
            planner.planTimeTable();
        });

        // FIX 4: Add a more meaningful assertion
        assertEquals(0, timetable.getAllModules().size(), "Timetable should be empty");
    }

    @Test
    void testDoesNotExceedTimetableCapacity() {
        // This test is fine. It checks that the planner doesn't crash
        // if module count > slot count.
        // The planner's logic `(i / NUM_TERMS) % NUM_YEARS` will
        // just wrap around and add modules to a slot that already has one.

        timetable = new Timetable(); // Use a fresh 4x4 timetable
        ModuleList largeCoreList = new ModuleList();
        for (int i = 0; i < 20; i++) { // 20 modules > 16 slots
            largeCoreList.add(new Module("CS" + (1000 + i), "Dummy", 4, "core", List.of()));
        }

        Planner planner = new Planner(timetable, largeCoreList, new ModuleList());
        assertDoesNotThrow(planner::planTimeTable,
                "Planner should handle more modules than slots without error");

        // Optional: Check that all modules were added (in overlapping slots)
        assertEquals(20, timetable.getAllModules().size());
    }
}
