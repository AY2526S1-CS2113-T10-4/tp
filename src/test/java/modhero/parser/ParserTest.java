package modhero.parser;

// Import JUnit 5 classes
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Import all the command classes to check their types
import modhero.commands.ClearCommand;
import modhero.commands.Command;
import modhero.commands.DeleteCommand;
import modhero.commands.ElectiveCommand;
import modhero.commands.ExitCommand;
import modhero.commands.HelpCommand;
import modhero.commands.IncorrectCommand;
import modhero.commands.MajorCommand;
import modhero.commands.ScheduleCommand;

/**
 * Unit tests for {@link Parser}.
 * This test class checks that the parser returns the correct
 * *type* of command in response to user input.
 */
public class ParserTest {

    private Parser parser;

    @BeforeEach
    void setUp() {
        parser = new Parser();
    }

    // --- Basic and Unknown Command Tests ---

    @Test
    void parse_emptyInput_returnsIncorrectCommand() {
        Command result = parser.parseCommand("");
        // We only check if the *type* is correct
        assertInstanceOf(IncorrectCommand.class, result, "Empty string should return IncorrectCommand");
    }

    @Test
    void parse_whitespaceInput_returnsIncorrectCommand() {
        Command result = parser.parseCommand("    ");
        assertInstanceOf(IncorrectCommand.class, result, "Whitespace-only string should return IncorrectCommand");
    }

    @Test
    void parse_unknownCommand_returnsIncorrectCommand() {
        Command result = parser.parseCommand("nonsenseCommand");
        assertInstanceOf(IncorrectCommand.class, result);
    }

    // --- Simple Command Tests ---

    @Test
    void parse_helpCommand_returnsHelpCommand() {
        Command result = parser.parseCommand("help");
        assertInstanceOf(HelpCommand.class, result);
    }

    @Test
    void parse_helpCommandWithArgs_returnsHelpCommand() {
        // The parser should ignore extra arguments for simple commands
        Command result = parser.parseCommand("help me please");
        assertInstanceOf(HelpCommand.class, result);
    }

    @Test
    void parse_exitCommand_returnsExitCommand() {
        Command result = parser.parseCommand("exit");
        assertInstanceOf(ExitCommand.class, result);
    }

    @Test
    void parse_clearCommand_returnsClearCommand() {
        Command result = parser.parseCommand("clear");
        assertInstanceOf(ClearCommand.class, result);
    }

    @Test
    void parse_scheduleCommand_returnsScheduleCommand() {
        Command result = parser.parseCommand("schedule");
        assertInstanceOf(ScheduleCommand.class, result);
    }

    // --- Major Command Tests ---

    @Test
    void parse_majorCommandValid_returnsMajorCommand() {
        Command result = parser.parseCommand("major Computer Science specialisation AI minor Statistics");
        assertInstanceOf(MajorCommand.class, result);
    }

    @Test
    void parse_majorCommandOnlyMajor_returnsMajorCommand() {
        Command result = parser.parseCommand("major Computer Engineering");
        assertInstanceOf(MajorCommand.class, result);
    }

    @Test
    void parse_majorCommandMissingArgs_returnsIncorrectCommand() {
        Command result = parser.parseCommand("major");
        assertInstanceOf(IncorrectCommand.class, result);
    }

    // --- Elective Command Tests ---

    @Test
    void parse_electiveCommandSingle_returnsElectiveCommand() {
        Command result = parser.parseCommand("elective CS2100");
        assertInstanceOf(ElectiveCommand.class, result);
    }

    @Test
    void parse_electiveCommandMultiple_returnsElectiveCommand() {
        Command result = parser.parseCommand("elective CS2100 CS2113 CS3243");
        assertInstanceOf(ElectiveCommand.class, result);
    }

    @Test
    void parse_electiveCommandWithExtraSpaces_returnsElectiveCommand() {
        Command result = parser.parseCommand("elective   CS2100  CS3243 ");
        assertInstanceOf(ElectiveCommand.class, result);
    }

    @Test
    void parse_electiveCommandEmpty_returnsIncorrectCommand() {
        Command result = parser.parseCommand("elective");
        assertInstanceOf(IncorrectCommand.class, result);
    }

    // --- Delete Command Tests ---

    @Test
    void parse_deleteCommandSingle_returnsDeleteCommand() {
        Command result = parser.parseCommand("delete CS2100");
        assertInstanceOf(DeleteCommand.class, result);
    }

    @Test
    void parse_deleteCommandMultiple_returnsDeleteCommand() {
        Command result = parser.parseCommand("delete CS2100 CS2113 CS3243");
        assertInstanceOf(DeleteCommand.class, result);
    }

    @Test
    void parse_deleteCommandWithExtraSpaces_returnsDeleteCommand() {
        Command result = parser.parseCommand("delete   CS2100  CS3243 ");
        assertInstanceOf(DeleteCommand.class, result);
    }

    @Test
    void parse_deleteCommandEmpty_returnsIncorrectCommand() {
        Command result = parser.parseCommand("delete");
        assertInstanceOf(IncorrectCommand.class, result);
    }
}
