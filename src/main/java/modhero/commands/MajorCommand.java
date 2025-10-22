package modhero.commands;

import modhero.data.major.Major;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Defines your primary degree major, which ModHero uses to load graduation requirements.
 */
public class MajorCommand extends Command {
    public static final Logger logger = Logger.getLogger(MajorCommand.class.getName());

    public static final String COMMAND_WORD = "major";
    public static final String SPECIALISATION_REGEX = "specialisation";
    public static final String MINOR_REGEX = "minor";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Specifying your major.\n"
            + "  Parameters: MAJOR_NAME [" + SPECIALISATION_REGEX + " SPECIALISATION] [" + MINOR_REGEX + " MINOR_NAME]\n"
            + "  Example: " + COMMAND_WORD + " Computer Science specialisation Artificial Intelligence";

    private final String major;
    private final String specialisation;
    private final String minor;

    public MajorCommand(String major, String specialisation, String minor) {
        assert major != null && !major.isEmpty() : "Major name must not be empty";

        this.major = major;
        this.specialisation = specialisation;
        this.minor = minor;

        logger.log(Level.FINEST, "Create major: ", major + specialisation + minor);

    }

    @Override
    public CommandResult execute() {
        logger.log(Level.INFO, "Executing Major Command");

        Major majorObj = allMajorsData.get(major);
        if (majorObj == null) {
            logger.log(Level.WARNING, () -> "Major not found: " + major);
            return new CommandResult("Failed to retrieve major " + major);
        }

        logger.log(Level.FINE, () -> "Setting major: " + major);
        coreList.setList(majorObj.getModules().getList());
        return new CommandResult(major + (specialisation == null ? "" : "|" + specialisation)
                + (minor == null ? "" : "|" + minor));
    }
}
