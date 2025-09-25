package modhero.data.modules;

import java.util.ArrayList;

/**
 * A simple list wrapper for {@link Module} objects.
 * Provides basic operations to add or remove modules.
 */
public class ModuleList {
    private final ArrayList<Module> moduleList;

    /**
     * Initializes an empty modulelist
     */
    public ModuleList() {
        this.moduleList = new ArrayList<>();
    }

    /**
     * Initializes the module list using an existing module list
     * @param moduleList an existing module list
     */
    public ModuleList(ArrayList<Module> moduleList) {
        this.moduleList = new ArrayList<>(moduleList);
    }

    /**
     * Adds a module to the list.
     *
     * @param module the module to add
     */
    public void add(Module module) {
        moduleList.add(module);
    }

    /**
     * Removes a module at the specified index.
     *
     * @param taskIndex the index of the module to remove
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public void remove(int taskIndex) {
        moduleList.remove(taskIndex);
    }

    /**
     * Gets the size of the module list
     * @return the size of the module list
     */
    public int size() {
        return moduleList.size();
    }

    /**
     * Get the specified course based on the index
     * @return the module at index
     */
    public Module getModule(int index) {
        return moduleList.get(index);
    }

    /**
     * Merge the targetList into the current module list
     * @param targetList the target module list that you want to merge
     * @return the merged module list
     */
    public ModuleList merge(ModuleList targetList) {
        ArrayList<Module> merged = new ArrayList<>(this.moduleList);
        merged.addAll(targetList.getList());
        return new ModuleList(merged);
    }

    /**
     * Gets the module list
     */
    public ArrayList<Module> getList() {
        return moduleList;
    }

    /**
     * Sorts the modules in the module list
     */
    public void sort() {
        moduleList.sort(Module.ModuleCodeComparator);
    }
}
