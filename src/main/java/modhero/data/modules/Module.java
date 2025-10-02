package modhero.data.modules;

import modhero.storage.Serialiser;

import java.util.Comparator;
import java.util.List;

/**
 * Represents a module at NUS, including its code, name, modular credits,
 * type, and prerequisites.
 */
public class Module {
    private String code;    // e.g. CS2113
    private String name;    // e.g. Software Engineering
    private int mc;         // e.g. modular credits
    private String type;    // e.g. core, elective, etc.
    private List<String> prerequisites; // e.g. ["CS1010", "CS1231"]
    // TODO: Sort based on the prequisite in the future
    public static Comparator<Module> ModuleCodeComparator = new Comparator<Module>() {
        @Override
        public int compare(Module module1, Module module2) {
            return module1.code.compareToIgnoreCase(module2.code);
        }
    };

    /**
     * Creates a new Module object.
     *
     * @param code the module code
     * @param name the module name
     * @param mc the number of modular credits
     * @param type the module type (e.g., core, elective)
     * @param prerequisites the list of prerequisite module codes
     */
    public Module(String code, String name, int mc, String type, List<String> prerequisites) {
        this.code = code;
        this.name = name;
        this.mc = mc;
        this.type = type;
        this.prerequisites = prerequisites;
    }

    /** Getters */

    /** @return the module code */
    public String getCode() {
        return code;
    }

    /** @return the module name */
    public String getName() {
        return name;
    }

    /** @return the number of modular credits */
    public int getMc() {
        return mc;
    }

    /** @return the module type */
    public String getType() {
        return type;
    }

    /** @return the list of prerequisite module codes */
    public List<String> getPrerequisites() {
        return prerequisites;
    }

    /**
     * Returns a formatted string representation of the module for storage purposes.
     *
     * @return the serialized module string
     */
    public String toFormatedString() {
        Serialiser sm = new Serialiser();
        return sm.serialiseMessage(code) +
                sm.serialiseMessage(name) +
                sm.serialiseMessage(Integer.toString(mc)) +
                sm.serialiseMessage(type) +
                sm.serialiseList(prerequisites);
    }
}
