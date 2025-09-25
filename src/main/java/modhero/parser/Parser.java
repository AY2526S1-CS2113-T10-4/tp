package modhero.parser;

import modhero.commands.ClearCommand;
import modhero.commands.Command;
import modhero.commands.DeleteCommand;
import modhero.commands.ElectiveCommand;
import modhero.commands.ExitCommand;
import modhero.commands.HelpCommand;
import modhero.commands.IncorrectCommand;
import modhero.commands.MajorCommand;
import modhero.commands.ScheduleCommand;

import java.util.ArrayList;
import java.util.List;

import static modhero.common.Constants.MESSAGE_INVALID_COMMAND_FORMAT;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        String[] words = userInput.trim().split(" ", 2);  // split the input into command and arguments
        if (words.length == 0) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = words[0];
        final String arguments = userInput.replaceFirst(commandWord, "").trim();

        switch (commandWord) {

        case MajorCommand.COMMAND_WORD:
            return prepareMajorCommand(arguments);

        case ElectiveCommand.COMMAND_WORD:
            return prepareElectiveCommand(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommand();

        case ScheduleCommand.COMMAND_WORD:
            return new ScheduleCommand();

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();
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
}