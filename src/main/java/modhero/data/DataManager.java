package modhero.data;

import modhero.common.exceptions.CorruptedDataFileException;
import modhero.data.major.Major;
import modhero.data.modules.Module;
import modhero.data.modules.ModuleList;
import modhero.storage.MajorLoader;
import modhero.storage.ModuleLoader;
import modhero.storage.Storage;
import modhero.data.timetable.Timetable;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages all application data including modules, majors, and timetable.
 */
public class DataManager {
    private static final Logger logger = Logger.getLogger(DataManager.class.getName());

    private final Timetable timetable;
    private final ModuleList electiveList;
    private final ModuleList coreList;
    private final Map<String, Module> allModulesData;
    private final Map<String, Major> allMajorsData;
    private final Storage preLoadModuleStorage;
    private final Storage preLoadMajorStorage;

    /**
     * Creates a new DataManager with specified storage paths.
     *
     * @param modulesPath Path to module data storage.
     * @param majorPath Path to major data storage.
     */
    public DataManager(String modulesPath, String majorPath) {
        this.timetable = new Timetable();
        this.electiveList = new ModuleList();
        this.coreList = new ModuleList();
        this.allModulesData = new HashMap<>();
        this.allMajorsData = new HashMap<>();
        this.preLoadModuleStorage = new Storage(modulesPath);
        this.preLoadMajorStorage = new Storage(majorPath);
    }

    /**
     * Loads all data from storage files.
     */
    private void initializeData() {
        try {
            ModuleLoader moduleLoader = new ModuleLoader(preLoadModuleStorage);
            moduleLoader.loadAllModulesData(allModulesData);
            MajorLoader majorLoader = new MajorLoader(preLoadMajorStorage);
            majorLoader.loadAllMajorsData(allModulesData, allMajorsData);
            logger.log(Level.INFO, "Data loaded successfully");
        } catch (CorruptedDataFileException e) {
            logger.log(Level.SEVERE, "Clearing corrupted file", e);
            preLoadModuleStorage.save("");
            preLoadMajorStorage.save("");
        }
    }

    // Getters
    public Timetable getTimetable() {
        return timetable;
    }

    public ModuleList getElectiveList() {
        return electiveList;
    }

    public ModuleList getCoreList() {
        return coreList;
    }

    public Map<String, Module> getAllModulesData() {
        return allModulesData;
    }

    public Map<String, Major> getAllMajorsData() {
        return allMajorsData;
    }
}
