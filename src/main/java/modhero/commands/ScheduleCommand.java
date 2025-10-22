package modhero.commands;

import modhero.data.Planner;
import modhero.data.modules.Module;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates a personalised 4-year study plan, factoring in prerequisites, NUSMods module availability, exchanges, and graduation requirements.
 */
public class ScheduleCommand extends Command {

    public static final String COMMAND_WORD = "schedule";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Returns recommended Year 1 and Year 2 mods, including core modules.\n"
            + "  Example: " + COMMAND_WORD;

    public static final Logger logger = Logger.getLogger(ScheduleCommand.class.getName());

    @Override
    public CommandResult execute() {
        logger.log(Level.INFO, "Executing Schedule Command");

        Planner planner = new Planner(data, coreList, electiveList);
        planner.planTimeTable();
        data.printTimetable();
        return new CommandResult(MESSAGE_USAGE);
    }
}
