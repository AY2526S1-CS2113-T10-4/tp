package modhero.commands;

import modhero.data.modules.Module;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Adds an elective (or multiple) to your plan.
 */
public class ElectiveCommand extends Command {
    public static final Logger logger = Logger.getLogger(ElectiveCommand.class.getName());

    public static final String COMMAND_WORD = "elective";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adding an elective.\n"
            + "  Parameters: MODULE_CODES...\n"
            + "  Example: " + COMMAND_WORD + " CS2109S"
            + "  Example: " + COMMAND_WORD + " CS2109S CS3230 CS3219";

    private final List<String> electives;

    public ElectiveCommand(List<String> electives) {
        assert electives != null : "Elective codes must not be null";
        this.electives = electives;
        logger.log(Level.FINEST, "Create add electives: " + electives.toString());
    }

    @Override
    public CommandResult execute() {
        logger.log(Level.INFO, "Executing Elective Command");
        logger.log(Level.INFO, () -> String.format("Adding %d electives", electives.size()));

        StringBuilder feedback = new StringBuilder("Electives added: ");
        for (String elective : electives) {
            Module module = allModulesData.get(elective);
            if (module != null) {
                electiveList.add(module);
                feedback.append(elective).append(", ");
                logger.log(Level.FINEST, () -> "Added elective: " + module.getCode());
            } else {
                feedback.append("\nElective ").append(elective).append(" not found in master list\n");
                logger.log(Level.WARNING, () -> "Elective not found in master list: " + elective);
            }
        }

        feedback.append("\n");

        return new CommandResult(feedback.toString());
    }
}
