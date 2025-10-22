package modhero.commands;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {
    public static final Logger logger = Logger.getLogger(ExitCommand.class.getName());

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exits the program.\n"
            + "  Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
        logger.log(Level.INFO, "Executing Exit Command");
        return new CommandResult("Goodbye!");
    }

    public static boolean isExit(Command command) {
        return command instanceof ExitCommand;
    }
}
