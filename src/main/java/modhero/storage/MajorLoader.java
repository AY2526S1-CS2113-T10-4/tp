package modhero.storage;

import modhero.common.exceptions.CorruptedDataFileException;
import modhero.common.util.Deserialiser;
import modhero.data.major.Major;
import modhero.data.modules.Module;
import modhero.data.modules.ModuleList;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads major data from persistent storage into memory.
 */
public class MajorLoader {
    private static final Logger logger = Logger.getLogger(MajorLoader.class.getName());
    private static final int EXPECTED_MAJOR_ARGS = 3;

    private final Storage storage;

    /**
     * Creates a MajorLoader with specified storage.
     *
     * @param storage the storage instance to use for file operations
     */
    public MajorLoader(Storage storage) {
        assert storage != null : "Storage must not be null";
        this.storage = storage;
    }

    /**
     * Loads all majors from storage and populates the provided map.
     *
     * @param allModulesData existing modules map for module lookup
     * @param allMajorsData map to populate; indexed by both abbreviation and name
     * @throws CorruptedDataFileException if critical data corruption detected
     */
    public void loadAllMajorsData(Map<String, Module> allModulesData, Map<String, Major> allMajorsData)
            throws CorruptedDataFileException {
        assert allModulesData != null : "loadAllMajorsData allModulesData must not be null";
        assert allMajorsData != null : "loadAllMajorsData allMajorsData must not be null";
        logger.log(Level.FINEST, "Loading all major data");


        List<String> rawMajorsList = storage.load();
        List<List<String>> allMajorsList = Deserialiser.deserialiseList(rawMajorsList);
        for (List<String> majorArgs : allMajorsList) {
            if (majorArgs.size() != 3) {
                logger.log(Level.WARNING, "Incorrect number of arguments for major: " + majorArgs.size());
                break;
            }
            Major major = new Major(majorArgs.get(0), majorArgs.get(1),
                    createModuleList(allModulesData, Deserialiser.deserialiseMessage(majorArgs.get(2))));
            allMajorsData.put(major.getAbbrName(), major);
            allMajorsData.put(major.getName(), major);
            logger.log(Level.FINEST, "Added major into database: " + major.getAbbrName());
        }
    }


    /**
     * Return ModuleList for major object.
     *
     * @param allModulesData hashmap to get modules object from
     * @param moduleCodes modules code in a list of string
     * @return ModuleList
     */
    private ModuleList createModuleList(Map<String, Module> allModulesData, List<String> moduleCodes) {
        ModuleList moduleList = new ModuleList();
        for (String code : moduleCodes) {
            Module module = allModulesData.get(code);
            if (module != null) {
                moduleList.add(module);
            } else {
                logger.log(Level.WARNING, "Missing module for major: " + code);
            }
        }
        return moduleList;
    }
}
