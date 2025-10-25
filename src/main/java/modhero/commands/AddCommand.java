package modhero.commands;

import modhero.data.modules.Module;
import modhero.data.timetable.Planner;
import modhero.data.timetable.Timetable;
import modhero.data.timetable.PrereqGraph;
import modhero.common.Constants;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AddCommand extends Command {
    public static final Logger logger = Logger.getLogger(AddCommand.class.getName());
    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a module to a specific year and semester.\n"
            + "  Format: add MODULE_CODE to YxSy\n"
            + "  Example: add CS3240 to Y3S2";

    private final String moduleCode;
    private final int year;
    private final int semester;

    public AddCommand(String moduleCode, int year, int semester) {
        this.moduleCode = moduleCode.toUpperCase();
        this.year = year;
        this.semester = semester;
    }

    @Override
    public CommandResult execute() {
        logger.log(Level.INFO, () -> String.format("Adding module %s to Y%dS%d", moduleCode, year, semester));

        // 1. Check if module exists
        Module module = allModulesData.get(moduleCode);
        if (module == null) {
            return new CommandResult("Module " + moduleCode + " not found in database.");
        }

        // 2. Check if already exists in timetable
        if (data.getAllModules().stream().anyMatch(m -> m.getCode().equalsIgnoreCase(moduleCode))) {
            return new CommandResult(moduleCode + " is already in your timetable!");
        }

        // 3. Check prerequisites
        PrereqGraph prereqGraph = new PrereqGraph(data.getAllModules());
        if (!prereqGraph.hasMetPrerequisites(module)) {
            return new CommandResult("Prerequisites not met for " + moduleCode);
        }

        // 4. Check overload
        if (data.getModules(year - 1, semester - 1).size() >= Constants.MAX_MODULES_PER_SEM) {
            data.addModule(year - 1, semester - 1, module);
            return new CommandResult("You are overloading this semester! Please seek help if you need to. (" + moduleCode + " added)");
        }

        // 5. Add to timetable
        data.addModule(year - 1, semester - 1, module);
        return new CommandResult(moduleCode + " added successfully to Y" + year + "S" + semester + "!");
    }
}
