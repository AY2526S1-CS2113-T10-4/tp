package modhero.parser;

import static modhero.common.Constants.MESSAGE_INVALID_COMMAND_FORMAT;

import modhero.commands.*;

import java.util.ArrayList;
import java.util.Arrays;
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

        String[] words = userInput.trim().split(" ", 2);  // split the input into command and arguments
        if (words.length == 0) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = words[0];
        final String arguments = userInput.replaceFirst(commandWord, "").trim();
        logger.log(Level.FINEST, "Selecting command: " + commandWord);

        switch (commandWord) {
        case MajorCommand.COMMAND_WORD:
            return prepareMajorCommand(arguments);
        case ElectiveCommand.COMMAND_WORD:
            return prepareElectiveCommand(arguments);
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
        case "add":
            return prepareAddCommand(arguments);
        default:
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
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
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MajorCommand.MESSAGE_USAGE));
        }
        String[] majorAndMinor = args.split(MajorCommand.MINOR_REGEX, 2);
        String minor = majorAndMinor.length > 1 ? majorAndMinor[1].trim() : "";
        String[] majorAndSpecialise = majorAndMinor[0].split(MajorCommand.SPECIALISATION_REGEX, 2);
        String specialisation = majorAndSpecialise.length > 1 ? majorAndSpecialise[1].trim() : "";
        String major = majorAndSpecialise[0].trim();

        return new MajorCommand(major, specialisation, minor);
    }

    private Command prepareElectiveCommand(String args) {
        if (args.isEmpty()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ElectiveCommand.MESSAGE_USAGE));
        }
        List<String> electiveList = new ArrayList<>();
        String[] argsList =  args.split(" ");
        for (String arg : argsList) {
            electiveList.add(arg);
        }
        return new ElectiveCommand(electiveList);
    }

    private Command prepareDeleteCommand(String args) {
        if (args.isEmpty()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
        List<String> electiveList = new ArrayList<>();
        String[] argsList =  args.split(" ");
        for (String arg : argsList) {
            electiveList.add(arg);
        }
        return new DeleteCommand(electiveList);
    }

    private Command prepareAddCommand(String args) {
        if (args.isEmpty()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        // Expect format: add CS3240 to Y3S2
        String[] parts = args.split("\\s+to\\s+");
        if (parts.length != 2) {
            return new IncorrectCommand("Invalid format. Use: add MODULE_CODE to YxSy");
        }

        String moduleCode = parts[0].trim();
        String destination = parts[1].trim().toUpperCase(); // Y3S2

        if (!destination.matches("Y\\dS\\d")) {
            return new IncorrectCommand("Invalid year/semester format. Use YxSy (e.g. Y2S1)");
        }

        int year = Character.getNumericValue(destination.charAt(1));
        int semester = Character.getNumericValue(destination.charAt(3));

        return new AddCommand(moduleCode, year, semester);
    }

}
