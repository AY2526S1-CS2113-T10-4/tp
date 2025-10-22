package modhero.data;

import modhero.data.modules.Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PrereqGraph}.
 */
public class PrereqGraphTest {

    private List<Module> modules;

    @BeforeEach
    void setUp() {
        // Mock data for testing
        modules = new ArrayList<>();

        // CS1010 has no prereqs
        modules.add(new Module("CS1010", "Programming Methodology", 4, "core", List.of()));

        // CS1231 depends on CS1010
        modules.add(new Module("CS1231", "Discrete Structures", 4, "core", List.of("CS1010")));

        // CS2030 depends on CS1010
        modules.add(new Module("CS2030", "OOP", 4, "core", List.of("CS1010")));

        // CS2040 depends on CS1231 and CS2030
        modules.add(new Module("CS2040", "Data Structures", 4, "core", List.of("CS1231", "CS2030")));
    }

    @Test
    void testGraphBuildsAllModules() {
        PrereqGraph graph = new PrereqGraph(modules);
        HashMap<String, List<String>> adj = graph.getGraph();

        // Should contain all module codes as keys
        assertTrue(adj.containsKey("CS1010"));
        assertTrue(adj.containsKey("CS1231"));
        assertTrue(adj.containsKey("CS2030"));
        assertTrue(adj.containsKey("CS2040"));
    }

    @Test
    void testEdgesAreBuiltCorrectly() {
        PrereqGraph graph = new PrereqGraph(modules);
        HashMap<String, List<String>> adj = graph.getGraph();

        // CS1010 is a prereq of CS1231 and CS2030
        assertEquals(Set.of("CS1231", "CS2030"), new HashSet<>(adj.get("CS1010")));

        // CS1231 and CS2030 are prereqs of CS2040
        assertEquals(List.of("CS2040"), adj.get("CS1231"));
        assertEquals(List.of("CS2040"), adj.get("CS2030"));
    }

    @Test
    void testModuleWithoutDependentsStillExistsInGraph() {
        PrereqGraph graph = new PrereqGraph(modules);
        HashMap<String, List<String>> adj = graph.getGraph();

        // CS2040 has no dependents (it's at the end)
        assertTrue(adj.containsKey("CS2040"));
        assertTrue(adj.get("CS2040").isEmpty());
    }

    @Test
    void testEmptyModuleList() {
        PrereqGraph graph = new PrereqGraph(Collections.emptyList());
        assertTrue(graph.getGraph().isEmpty());
    }
}
