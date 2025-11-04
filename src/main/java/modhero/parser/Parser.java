package modhero.parser;

import modhero.common.Constants.MessageConstants;

import modhero.commands.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Parses user input.
 */
public class Parser {
    public static final Logger logger = Logger.getLogger(Parser.class.getName());

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        assert userInput != null : "User input must not be null";
        logger.log(Level.FINEST, "Parsing command: " + userInput);

        if (!userInput.matches("[a-zA-Z0-9 ]+")) {
            return new IncorrectCommand(String.format(MessageConstants.INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        String[] words = userInput.trim().split(" ", 2);  // split the input into command and arguments
        if (words.length == 0) {
            return new IncorrectCommand(String.format(MessageConstants.INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = words[0];
        final String arguments = userInput.replaceFirst(commandWord, "").trim();
        logger.log(Level.FINEST, "Selecting command: " + commandWord);

        switch (commandWord) {
        case MajorCommand.COMMAND_WORD:
            return prepareMajorCommand(arguments);
        case AddCommand.COMMAND_WORD:
            return prepareAddCommand(arguments);
        case DeleteCommand.COMMAND_WORD:
            return prepareDeleteCommand(arguments);
        case ScheduleCommand.COMMAND_WORD:
            return new ScheduleCommand();
        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();
        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();
        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();
        default:
            return new IncorrectCommand(String.format(MessageConstants.INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Parses arguments in the context of the major command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareMajorCommand(String args) {
        if (args.isEmpty()) {
            return new IncorrectCommand(String.format(MessageConstants.INVALID_COMMAND_FORMAT, MajorCommand.MESSAGE_USAGE));
        }

        return new MajorCommand(args);
    }

    /**
     * Prepares the DeleteCommand from arguments.
     *
     * @param args command arguments
     * @return DeleteCommand or IncorrectCommand if invalid
     */
    private Command prepareDeleteCommand(String args) {
        if (args.isEmpty()) {
            return new IncorrectCommand(String.format(MessageConstants.INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        // Trim and get the module code
        String moduleCode = args.trim();

        // Validate that it's a single module code (no spaces)
        if (moduleCode.contains(" ")) {
            return new IncorrectCommand("Invalid format. Delete one module at a time. Use: delete MODULE_CODE");
        }

        return new DeleteCommand(moduleCode);
    }

    /**
     * Prepares the AddCommand from arguments.
     *
     * @param args command arguments
     * @return AddCommand or IncorrectCommand if invalid
     */
    private Command prepareAddCommand(String args) {
        if (args.isEmpty()) {
            return new IncorrectCommand(String.format(MessageConstants.INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        
        String[] parts = args.split("\\s+to\\s+");
        if (parts.length != 2) {
            return new IncorrectCommand("Invalid format. Use: add MODULE_CODE to YxSy");
        }

        String moduleCode = parts[0].trim();
        String destination = parts[1].trim().toUpperCase(); // Y3S2

        // \d accpets any digit that is from 0 to 9
        if (!destination.matches("Y\\dS\\d")) {
            return new IncorrectCommand("Invalid year/semester format. Use YxSy (e.g. Y2S1)");
        }

        int year = Character.getNumericValue(destination.charAt(1));
        int semester = Character.getNumericValue(destination.charAt(3));

        return new AddCommand(moduleCode, year, semester);
    }
}
