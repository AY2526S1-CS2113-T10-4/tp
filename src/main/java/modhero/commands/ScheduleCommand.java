package modhero.commands;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Displays the current 4-year study plan (already populated when a major is set).
 */
public class ScheduleCommand extends Command {

    public static final String COMMAND_WORD = "schedule";
    private static final Logger logger = Logger.getLogger(ScheduleCommand.class.getName());

    @Override
    public CommandResult execute() {
        logger.log(Level.INFO, "Executing Schedule Command");

        // Just display the timetable; don’t rebuild or print module codes
        data.printTimetable();

        return new CommandResult("Here's your recommended 4-year schedule above!");
    }
}
