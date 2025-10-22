package modhero;

import modhero.commands.ClearCommand;
import modhero.commands.Command;
import modhero.commands.CommandResult;
import modhero.commands.DeleteCommand;
import modhero.commands.ElectiveCommand;
import modhero.commands.ExitCommand;
import modhero.commands.HelpCommand;
import modhero.commands.MajorCommand;
import modhero.commands.ScheduleCommand;
import modhero.data.Planner;
import modhero.data.Timetable;
import modhero.data.major.Major;
import modhero.data.modules.Module;
import modhero.data.modules.ModuleList;
import modhero.exception.CorruptedDataFileException;
import modhero.parser.Parser;
import modhero.storage.Storage;
import modhero.ui.Ui;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entry point of the ModHero application.
 * Handles initialization, user interaction loop, and program termination.
 */
public class ModHero {
    private static final Logger logger = Logger.getLogger(ModHero.class.getName());

    private Ui ui;
    private Timetable timetable;
    private ModuleList electiveList;
    private ModuleList coreList;
    private Storage storage;
    private Storage majorStorage;
    private Map<String, Module> allModulesData;
    private Map<String, Major> allMajorsData;

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
        setLoggerIntensity();
        logger.log(Level.INFO, "Starting ModHero");
        start();
        runCommandLoopUntilExitCommand();
        exit();
        logger.log(Level.INFO, "ModHero terminated");
    }

    /**
     * Sets up the required objects, and prints the welcome message.
     */
    private void start() {
        logger.log(Level.INFO, "Start setting up data required");
        this.ui = new Ui();
        this.timetable = new Timetable(4, 2);
        this.storage = new Storage("data/data.txt");
        this.majorStorage = new Storage("data/majorData.txt");
        this.allModulesData = new HashMap<>();
        this.allMajorsData = new HashMap<>();
        this.electiveList = new ModuleList();
        this.coreList = new ModuleList();
        initateStorageHashMap();
        logger.log(Level.INFO, "Completed setting up data required");
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
            logger.log(Level.INFO, "Parsing command");
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
        assert command != null : "Command must not be null";
        try {
            command.setData(timetable, electiveList, coreList, allModulesData, allMajorsData);
            CommandResult result = command.execute();
            logger.log(Level.INFO, () -> "Command execution completed");
            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Command execution failed", e);
            return new CommandResult(e.getMessage());
        }
    }

    private void initateStorageHashMap() {
        try {
            storage.loadAllModulesData(allModulesData);
            majorStorage.loadAllMajorsData(allModulesData, allMajorsData);
        } catch (CorruptedDataFileException e) {
            logger.log(Level.SEVERE, "Clearing corrupted file", e);
            storage.save("");
            majorStorage.save("");
        }
    }

    private void setLoggerIntensity() {
        logger.setLevel(Level.INFO);
        Parser.logger.setLevel(Level.INFO);
        Command.logger.setLevel(Level.INFO);
        MajorCommand.logger.setLevel(Level.INFO);
        ElectiveCommand.logger.setLevel(Level.INFO);
        DeleteCommand.logger.setLevel(Level.INFO);
        ScheduleCommand.logger.setLevel(Level.INFO);
        ClearCommand.logger.setLevel(Level.INFO);
        HelpCommand.logger.setLevel(Level.INFO);
        ExitCommand.logger.setLevel(Level.INFO);
        Major.logger.setLevel(Level.INFO);
        Module.logger.setLevel(Level.INFO);
        ModuleList.logger.setLevel(Level.INFO);
        Planner.logger.setLevel(Level.INFO);
        Timetable.logger.setLevel(Level.INFO);
    }
}
