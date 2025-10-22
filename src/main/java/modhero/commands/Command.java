package modhero.commands;

import modhero.data.Timetable;
import modhero.data.major.Major;
import modhero.data.modules.Module;
import modhero.data.modules.ModuleList;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract base class for all commands in the ModHero application.
 * Provides access to the timetable and module lists, and defines
 * the contract for command execution.
 */
public abstract class Command {
    public static final Logger logger = Logger.getLogger(Command.class.getName());

    protected Timetable data;
    protected ModuleList electiveList;
    protected ModuleList coreList;
    protected Map<String, Module> allModulesData;
    protected Map<String, Major> allMajorsData;

    /**
     * Sets the data context for the command, including the timetable
     * and lists of elective and core modules.
     *
     * @param data the timetable to operate on
     * @param electiveList the list of elective modules
     * @param coreList the list of core modules
     * @param allModulesData the hashmap for loading and saving data
     * @param allMajorsData the hashmap for major information
     */
    public void setData(Timetable data, ModuleList electiveList, ModuleList coreList,
                        Map<String, Module> allModulesData, Map<String, Major> allMajorsData) {
        assert data != null : "Timetable must not be null";
        assert electiveList != null : "Elective list must not be null";
        assert coreList != null : "Core list must not be null";
        assert allModulesData != null : "All modules map must not be null";
        assert allMajorsData != null : "All majors map must not be null";

        this.data = data;
        this.electiveList = electiveList;
        this.coreList = coreList;
        this.allModulesData = allModulesData;
        this.allMajorsData = allMajorsData;

        logger.info("Command setData initialised");
        logger.log(Level.FINEST, () -> String.format("SetData sizes - electives: %d, core: %d, modules: %d, majors: %d",
                electiveList.getList().size(),
                coreList.getList().size(),
                allModulesData.size(),
                allMajorsData.size()));
        logger.log(Level.FINEST, () -> String.format(
                "ElectiveList: %s%nCoreList: %s%nModules: %s%nMajors: %s",
                electiveList.getList().stream()
                        .map(Module::getCode)
                        .toList(),
                coreList.getList().stream()
                        .map(Module::getCode)
                        .toList(),
                allModulesData.keySet(),
                allMajorsData.keySet()
        ));
    }

    /**
     * Executes the command.
     *
     * @return a {@link CommandResult} containing feedback for the user
     */
    public abstract CommandResult execute();

}
