package modhero.data.modules;

import modhero.storage.Serialiser;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a module at NUS, including its code, name, modular credits,
 * type, and prerequisites (which can include AND/OR combinations).
 */
public class Module {
    public static final Logger logger = Logger.getLogger(Module.class.getName());

    private String code;    // e.g. CS2113
    private String name;    // e.g. Software Engineering
    private int mc;         // e.g. modular credits
    private String type;    // e.g. core, elective, etc.

    // Each inner list represents one valid combination of prerequisites (for OR/AND logic)
    // e.g. [[CS1010], [CS2030, CS2040]] means:
    // either take CS1010, OR (CS2030 AND CS2040)
    private List<List<String>> prerequisites;

    /**
     * Creates a new Module object.
     *
     * @param code the module code
     * @param name the module name
     * @param mc the number of modular credits
     * @param type the module type (e.g., core, elective)
     * @param prerequisites nested prerequisite combinations
     */
    public Module(String code, String name, int mc, String type, List<List<String>> prerequisites) {
        this.code = code;
        this.name = name;
        this.mc = mc;
        this.type = type;
        this.prerequisites = prerequisites;

        logger.log(Level.FINEST, "Module created: " + name + " (" + code + ")");
    }

    /** Getters */

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getMc() {
        return mc;
    }

    public String getType() {
        return type;
    }

    /**
     * @return the nested list of prerequisite combinations
     */
    public List<List<String>> getPrerequisites() {
        return prerequisites;
    }

    /**
     * Returns a formatted string representation of the module for storage purposes.
     * For simplicity, prerequisites are flattened into a readable string.
     */
    public String toFormattedString() {
        Serialiser sm = new Serialiser();
        StringBuilder prereqBuilder = new StringBuilder();

        if (prerequisites != null) {
            for (List<String> combo : prerequisites) {
                prereqBuilder.append(String.join(" & ", combo)).append(" | ");
            }
        }

        return sm.serialiseMessage(code) +
                sm.serialiseMessage(name) +
                sm.serialiseMessage(Integer.toString(mc)) +
                sm.serialiseMessage(type) +
                sm.serialiseMessage(prereqBuilder.toString());
    }

    /** Comparator for sorting by module code (case-insensitive). */
    public static Comparator<Module> ModuleCodeComparator = new Comparator<Module>() {
        @Override
        public int compare(Module module1, Module module2) {
            return module1.code.compareToIgnoreCase(module2.code);
        }
    };
}
