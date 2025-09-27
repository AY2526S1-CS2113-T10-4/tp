package modhero.commands;

import modhero.data.Planner;

/**
 * Generates a personalised 4-year study plan, factoring in prerequisites, NUSMods module availability, exchanges, and graduation requirements.
 */
public class ScheduleCommand extends Command {

    public static final String COMMAND_WORD = "schedule";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Returns recommended Year 1 and Year 2 mods, including core modules.\n"
            + "  Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
        Planner p = new Planner(data, coreList, electiveList);
        p.planTimeTable();
        data.printTimetable();
        return new CommandResult(MESSAGE_USAGE);
    }
}
