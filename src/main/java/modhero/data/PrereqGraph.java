package modhero.data;

import modhero.data.modules.Module;
import java.util.*;

/**
 * Builds and manages a prerequisite dependency graph between modules.
 * Each module is a node, and edges represent prerequisite relationships.
 * Example: CS1010 → CS2030 means CS1010 must be taken before CS2030.
 */
public class PrereqGraph {
    private List<Module> modules;
    private final HashMap<String, List<String>> graph = new HashMap<>();

    /**
     * Builds a prerequisite graph from the given list of modules.
     * Each module is a node. An edge A → B means A must be taken before B.
     */
    public PrereqGraph(List<Module> modules) {
        this.modules = modules;
        buildGraph(); // now works properly — no args needed
    }

    /**
     * Constructs the adjacency list representation of the prerequisite graph.
     * Each module is initialized as a key, and for every prerequisite combination,
     * edges are added from prerequisite → dependent module.
     */
    private void buildGraph() {
        // Initialize all modules as keys
        for (Module m : modules) {
            graph.putIfAbsent(m.getCode(), new ArrayList<>());
        }

        // For each module, add edges from each prerequisite → this module
        for (Module m : modules) {
            List<List<String>> prereqCombos = m.getPrerequisites();
            if (prereqCombos == null) continue;

            // Each inner list = one valid prerequisite combination (e.g. AND group)
            for (List<String> combo : prereqCombos) {
                for (String prereq : combo) {
                    graph.computeIfAbsent(prereq, k -> new ArrayList<>()).add(m.getCode());
                }
            }
        }
    }

    /** Returns the adjacency list (module → list of dependents). */
    public HashMap<String, List<String>> getGraph() {
        return graph;
    }

    /** Prints the adjacency list for debugging. */
    public void printGraph() {
        System.out.println("=== Prerequisite Graph ===");
        for (Map.Entry<String, List<String>> e : graph.entrySet()) {
            System.out.println(e.getKey() + " → " + e.getValue());
        }
    }
}
