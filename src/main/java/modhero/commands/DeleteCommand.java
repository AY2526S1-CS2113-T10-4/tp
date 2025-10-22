package modhero.commands;

import modhero.data.modules.Module;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Removes a specified elective from your plan.
 */
public class DeleteCommand extends Command {
    public static final Logger logger = Logger.getLogger(DeleteCommand.class.getName());

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes the person identified by the index number used in the last person listing.\n"
            + "  Parameters: MODULE_CODE ...\n"
            + "  Example: " + COMMAND_WORD + " CS2109S"
            + "  Example: " + COMMAND_WORD + " CS2109S CS3230 CS3219";

    private final List<String> electives;

    public DeleteCommand(List<String> electives) {
        assert electives != null : "Elective list argument must not be null";
        this.electives = electives;
        logger.log(Level.FINEST, "Create delete electives: " + electives.toString());
    }


    /** Searches for the corresponding module in electiveList and deletes it if found.*/
    @Override
    public CommandResult execute() {
        logger.log(Level.INFO, "Executing Delete Command");
        logger.log(Level.FINE, () -> String.format("Removing %d electives", electives.size()));

        StringBuilder feedback = new StringBuilder("Electives removed: ");
        for (String elective : electives) {
            boolean isFound = false;
            for (int i = 0; i < electives.size(); i++) {
                Module module = electiveList.getList().get(i);
                if (module.getCode().equals(elective)) {
                    electiveList.remove(i);
                    feedback.append(elective).append(" ");
                    isFound = true;
                    break;
                }
            }

            if (!isFound) {
                feedback.append("Elective Not Found").append("\n");
                return new CommandResult(feedback.toString());
            }
            feedback.append("\n");
        }

        return new CommandResult(feedback.toString());
    }
}
