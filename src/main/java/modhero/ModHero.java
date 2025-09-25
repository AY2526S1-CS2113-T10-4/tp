package modhero;

import modhero.commands.Command;
import modhero.commands.CommandResult;
import modhero.commands.ExitCommand;
import modhero.data.Timetable;
import modhero.data.modules.ModuleList;
import modhero.parser.Parser;
import modhero.ui.Ui;

/**
 * Entry point of the ModHero application.
 * Handles initialization, user interaction loop, and program termination.
 */
public class ModHero {
    private Ui ui;
    private Timetable timetable;
    private ModuleList electiveList;
    private ModuleList coreList;

    /**
     * Launches the ModHero application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new ModHero().run();
    }

    /**
     * Runs the application by initializing resources,
     * starting the command loop, and performing cleanup.
     */
    public void run() {
        start();
        runCommandLoopUntilExitCommand();
        exit();
    }

    /**
     * Sets up the required objects, and prints the welcome message.
     */
    private void start() {
        this.ui = new Ui();
        this.timetable = new Timetable();
        this.electiveList = new ModuleList();
        this.coreList = new ModuleList();
        ui.showWelcome();
    }

    /**
     * Prints the goodbye message and exits.
     */
    private void exit() {
        ui.showBye();
        ui.close();
    }

    /**
     * Continuously reads and executes user commands until the exit command is received.
     */
    private void runCommandLoopUntilExitCommand() {
        Command command;
        do {
            String userCommandText = ui.readCommand();
            command = new Parser().parseCommand(userCommandText);
            CommandResult result = executeCommand(command);
            ui.showResultToUser(result);
        } while (!ExitCommand.isExit(command));
    }

    /**
     * Executes the given command and returns its result.
     *
     * @param command The user command to execute.
     * @return The result of executing the command.
     */
    private CommandResult executeCommand(Command command) {
        try {
            command.setData(timetable, electiveList, coreList);
            CommandResult result = command.execute();
            return result;
        } catch (Exception e) {
            return new CommandResult(e.getMessage());
        }
    }

}
