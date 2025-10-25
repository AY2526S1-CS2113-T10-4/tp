package modhero.commands;

import modhero.data.major.MajorData;
import modhero.data.modules.Module;
import modhero.data.modules.ModuleList;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Defines the user's primary degree major.
 * Loads the relevant core modules and recommended study plan into the timetable.
 */
public class MajorCommand extends Command {
    private static final Logger logger = Logger.getLogger(MajorCommand.class.getName());

    public static final String COMMAND_WORD = "major";
    public static final String SPECIALISATION_REGEX = "specialisation";
    public static final String MINOR_REGEX = "minor";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Specify your major.\n"
            + "  Parameters: MAJOR_NAME [" + SPECIALISATION_REGEX + " SPECIALISATION] [" + MINOR_REGEX + " MINOR_NAME]\n"
            + "  Example: " + COMMAND_WORD + " Computer Science specialisation Artificial Intelligence";

    private final String major;
    private final String specialisation;
    private final String minor;

    /**
     * Creates a MajorCommand to set the user's major.
     *
     * @param major          The user's major (e.g., "Computer Science" or "Computer Engineering")
     * @param specialisation The chosen specialisation (optional)
     * @param minor          The chosen minor (optional)
     */
    public MajorCommand(String major, String specialisation, String minor) {
        assert major != null && !major.isEmpty() : "Major name must not be empty";
        this.major = major.trim();
        this.specialisation = specialisation;
        this.minor = minor;

        logger.log(Level.FINEST, () -> String.format(
                "Created MajorCommand: major=%s, specialisation=%s, minor=%s",
                this.major, this.specialisation, this.minor));
    }

    @Override
    public CommandResult execute() {
        logger.log(Level.INFO, "Executing Major Command");

        MajorData majorData = new MajorData();
        ModuleList coreModules = majorData.getCoreModules(major);

        if (coreModules == null) {
            return new CommandResult("Sorry, " + major
                    + " is not supported. Try 'Computer Science' or 'Computer Engineering'.");
        }

        // Reset current timetable and core list
        coreList.setList(coreModules.getList());
        data.clearTimetable();

        // Register all core modules in the global module map
        for (Module m : coreModules.getList()) {
            allModulesData.putIfAbsent(m.getCode(), m);
        }

        // Populate timetable based on schedule map
        Map<String, int[]> scheduleMap = majorData.getSchedule(major);
        for (Module m : coreModules.getList()) {
            int[] yAndS = scheduleMap.getOrDefault(m.getCode(), new int[]{1, 1});
            data.addModule(yAndS[0] - 1, yAndS[1] - 1, m);
        }

        logger.log(Level.INFO, () -> "Major successfully set to " + major);
        return new CommandResult("Major set to " + major + ". Type 'schedule' to view your 4-year plan!");
    }
}
