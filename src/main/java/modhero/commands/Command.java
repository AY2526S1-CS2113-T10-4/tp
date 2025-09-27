package modhero.commands;

import modhero.data.Timetable;
import modhero.data.modules.Module;
import modhero.data.modules.ModuleList;
import modhero.storage.Storage;

import java.util.Map;

/**
 * Abstract base class for all commands in the ModHero application.
 * Provides access to the timetable and module lists, and defines
 * the contract for command execution.
 */
public abstract class Command {
    protected Timetable data;
    protected ModuleList electiveList;
    protected ModuleList coreList;
    protected Map<String, Module> allModulesData;


    /**
     * Sets the data context for the command, including the timetable
     * and lists of elective and core modules.
     *
     * @param data the timetable to operate on
     * @param electiveList the list of elective modules
     * @param coreList the list of core modules
     * @param allModulesData the hashmap for loading and saving data
     */
    public void setData(Timetable data, ModuleList electiveList, ModuleList coreList, Map<String, Module> allModulesData) {
        this.data = data;
        this.electiveList = electiveList;
        this.coreList = coreList;
        this.allModulesData = allModulesData;
    }

    /**
     * Executes the command.
     *
     * @return a {@link CommandResult} containing feedback for the user
     */
    public abstract CommandResult execute();

    /**
     * Indicates whether this command should terminate the application.
     * By default, commands do not exit.
     *
     * @return {@code true} if this command exits the application, {@code false} otherwise
     */
    public boolean isExit() {
        return false;
    }
}
