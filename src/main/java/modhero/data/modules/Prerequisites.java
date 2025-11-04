package modhero.data.modules;

import modhero.common.util.SerialisationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents module prerequisites as a list of OR-groups.
 * Each inner list represents a set of modules where any one satisfies the requirement.
 */
public class Prerequisites {
    public static final Logger logger = Logger.getLogger(Prerequisites.class.getName());

    /** OR-grouped prerequisite modules. */
    private List<List<String>> prereq;

    /** Creates an empty prerequisites object. */
    public Prerequisites() {
        this.prereq = new ArrayList<>();
    }

    /**
     * Creates a prerequisites object with predefined OR-groups.
     *
     * @param prereq list of OR-groups (each inner list contains module codes)
     */
    public Prerequisites(List<List<String>> prereq) {
        this.prereq = prereq;
    }

    /** Returns the list of OR-groups representing prerequisites. */
    public List<List<String>> getPrereq() {
        return prereq;
    }

    /**
     * Serializes prerequisites into a doubly-serialized string.
     * Each module code in a combination is serialized, then the combination is serialized.
     * Intended to be wrapped again by the caller for triple serialization.
     *
     * @return doubly-serialized prerequisites string
     */
    public String toFormatedString() {
        logger.log(Level.FINEST, "Serialising prerequisites");

        if (prereq == null || prereq.isEmpty()) {
            logger.log(Level.FINEST, "No prerequisites to serialize");
            return "";
        }

        SerialisationUtil serialisationUtil = new SerialisationUtil();
        StringBuilder outerBuilder = new StringBuilder();

        for (List<String> combination : prereq) {
            StringBuilder innerBuilder = new StringBuilder();
            for (String moduleCode : combination) {
                innerBuilder.append(serialisationUtil.serialiseMessage(moduleCode));
            }
            outerBuilder.append(serialisationUtil.serialiseMessage(innerBuilder.toString()));
        }

        String serialisedPrereqs = outerBuilder.toString();
        logger.log(Level.FINEST, "Successfully serialized prerequisites (doubly-serialized)");
        return serialisedPrereqs;
    }

    /** Returns a human-readable string representation of prerequisites. */
    @Override
    public String toString() {
        if (prereq == null || prereq.isEmpty()) {
            return "No prerequisites";
        }

        StringBuilder sb = new StringBuilder("Prerequisites: [");
        for (int i = 0; i < prereq.size(); i++) {
            List<String> group = prereq.get(i);
            sb.append(group);
            if (i < prereq.size() - 1) {
                sb.append(" OR ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
