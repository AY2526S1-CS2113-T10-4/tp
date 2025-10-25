package modhero.data.modules;

import modhero.common.util.Serialiser;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a module at NUS, including its code, name, modular credits,
 * type, and prerequisites.
 */
public class Module {
    public static final Logger logger = Logger.getLogger(Module.class.getName());

    private String code;    // e.g. CS2113
    private String name;    // e.g. Software Engineering
    private int mc;         // e.g. modular credits
    private String type;    // e.g. core, elective, etc.
    private Prerequisites prerequisites; // e.g. ["CS1010", "CS1231"]

    /**
     * Creates a new Module object.
     *
     * @param code the module code
     * @param name the module name
     * @param mc the number of modular credits
     * @param type the module type (e.g., core, elective)
     * @param prerequisites the object of prerequisite module codes
     */
    public Module(String code, String name, int mc, String type, Prerequisites prerequisites) {
        assert code != null && !code.isEmpty() : "Module code must not be empty";
        assert name != null && !name.isEmpty() : "Module name must not be empty";
        assert type != null && !type.isEmpty() : "Module type must not be empty";
        assert prerequisites != null : "Prerequisites list must not be null";

        this.code = code;
        this.name = name;
        this.mc = mc;
        this.type = type;
        this.prerequisites = prerequisites;

        logger.log(Level.FINEST, "Module created: " + name + " (" + code + ")");
    }

    public Module(String code, String name, int mc, String type, List<String> prerequisites) {
        assert code != null && !code.isEmpty() : "Module code must not be empty";
        assert name != null && !name.isEmpty() : "Module name must not be empty";
        assert type != null && !type.isEmpty() : "Module type must not be empty";
        assert prerequisites != null : "Prerequisites list must not be null";

        this.code = code;
        this.name = name;
        this.mc = mc;
        this.type = type;
        this.prerequisites = new Prerequisites(List.of(prerequisites));

        logger.log(Level.FINEST, "Module created: " + name + " (" + code + ")");
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
    public Prerequisites getPrerequisites() {
        return prerequisites;
    }

    /**
     * Returns a formatted string representation of the module for storage purposes.
     *
     * @return the serialized module string
     */
    public String toFormatedString() {
        logger.log(Level.FINEST, "Serialising module: " + code);

        Serialiser serialiser = new Serialiser();
        String formattedString = serialiser.serialiseMessage(code)
                + serialiser.serialiseMessage(name)
                + serialiser.serialiseMessage(Integer.toString(mc))
                + serialiser.serialiseMessage(type)
                + prerequisites.toFormatedString();

        logger.log(Level.FINEST, "Successful serialising module: " + code);
        return formattedString;
    }
}
