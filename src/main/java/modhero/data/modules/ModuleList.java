package modhero.data.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A wrapper class around a {@link List} of {@link Module} objects.
 * <p>
 * Provides basic operations to add, remove, and search for modules.
 * A Storage reference may optionally be provided to lookup the
 * list of all modules.
 * <p>
 */
public class ModuleList {
    public static final Logger logger = Logger.getLogger(ModuleList.class.getName());

    private List<Module> moduleList;

    /**
     * Creates a {@code ModuleList} backed by a new empty list,
     * without any storage reference.
     * Use this constructor when no global module lookup is required.
     */
    public ModuleList() {
        this.moduleList = new ArrayList<>();
    }

    /**
     * Adds a module to the list.
     *
     * @param module the module to add
     */
    public void add(Module module) {
        assert module != null : "add module cannot be null";

        moduleList.add(module);

        logger.log(Level.FINEST, "Added module: " + module);
    }

    /**
     * Removes a module at the specified index.
     *
     * @param taskIndex the index of the module to remove
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public void remove(int taskIndex) {
        assert taskIndex >= 0 && taskIndex < moduleList.size() : "remove taskIndex out of bounds";

        Module removedModule = moduleList.remove(taskIndex);

        logger.log(Level.FINEST, "Removed module: " + removedModule);
    }

    /**
     * Get module list
     *
     * @return moduleList
     */
    public List<Module> getList() {
        return moduleList;
    }

    public Module getModuleByCode(String code){
        assert code != null : "getModuleByCode code cannot be null";
        for (Module module : moduleList){
            if (module.getCode().equals(code)){
                return module;
            }
        }
        return null; //add proper exception handling here later
    }

    /**
     * Replace all module in the list with new list
     *
     * @param newList to be replaced with
     */
    public void setList(List<Module> newList) {
        this.moduleList = newList;
        logger.log(Level.FINEST, "Module List has been replaced");
    }

    /**
     *  Get the number of modules in the module list
     * @return the size of the module list
     */
    public int size() {
        return this.moduleList.size();
    }

    /**
     * Know whether the module list is empty or not
     * @return the state of whether the module list is empty
     */
    public boolean isEmpty() {
        return this.moduleList.isEmpty();
    }
}
