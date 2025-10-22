package modhero.commands;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Deletes all modules and resets your plan.
 */
public class ClearCommand extends Command {
    public static final Logger logger = Logger.getLogger(ClearCommand.class.getName());

    public static final String COMMAND_WORD = "clear";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes all modules.\n"
            + "  Example: " + COMMAND_WORD;


    @Override
    public CommandResult execute() {
        logger.log(Level.INFO, "Executing Clear Command");

        data.clearTimetable();
        return new CommandResult("Reset the timetable.");
    }
}
