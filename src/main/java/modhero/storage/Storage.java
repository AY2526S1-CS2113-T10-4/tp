package modhero.storage;

import modhero.data.modules.Prerequisites;
import modhero.data.major.Major;
import modhero.data.modules.Module;
import modhero.data.modules.ModuleList;
import modhero.common.exceptions.CorruptedDataFileException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles loading from and saving to a text file.
 * Provides persistence for data by reading and writing plain text.
 */
public class Storage {
    private static final Logger logger = Logger.getLogger(Storage.class.getName());

    private final String filePath;

    /**
     * Creates a new {@code Storage} tied to the specified file path.
     *
     * @param filePath the path of the file to load from or save to
     */
    public Storage(String filePath) throws IllegalArgumentException{
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path must not be null or empty");
        }
        this.filePath = filePath;
    }

    /**
     * Ensures that the directory for the file path exists.
     * Creates directories if not present.
     */
    private void ensureFileDirectoryExist() throws IOException {
        File parent = new File(filePath).getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Failed to create directory: " + parent.getAbsolutePath());
        }
        logger.log(Level.FINEST, "Ensured directory existence");
    }

    /**
     * Ensures that the file exists.
     * Creates file if not present.
     * @throws IOException if an I/O error occurs during creating
     */
    private void ensureFileExist() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        logger.log(Level.FINEST, "Ensured file existence");
    }

    /**
     * Loads the file contents into a list of strings, each line a list element.
     *
     * @return list of lines from file, or empty list if file not found
     */
    public List<String> load() {
        logger.log(Level.FINEST, "Loading file: " + filePath);

        try {
            ensureFileDirectoryExist();
            ensureFileExist();
            return readFromFile();
        } catch (IOException e) {
            System.out.println("Failed to load file");
            return new ArrayList<>();
        }
    }

    /**
     * Reads all lines from the file at filePath.
     *
     * @return list of strings read line-by-line from the file
     * @throws FileNotFoundException when the file does not exist
     */
    private List<String> readFromFile() throws FileNotFoundException {
        File file = new File(filePath);
        Scanner s = new Scanner(file);
        List<String> rawTaskList = new ArrayList<>();
        while (s.hasNext()) {
            rawTaskList.add(s.nextLine());
        }

        logger.log(Level.FINEST, "Read file has " + rawTaskList.size() + " of sentences: ");
        return rawTaskList;
    }

    /**
     * Saves the given text string to the file, overwriting any existing content.
     *
     * @param textToAdd the text content to save
     */
    public void save(String textToAdd) {
        assert textToAdd != null : "save textToAdd must not be null";
        logger.log(Level.FINEST, "Saving file: " + filePath);

        try {
            ensureFileDirectoryExist();
            writeToFile(textToAdd);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to save file" + e);
        }
    }

    /**
     * Writes the provided text content to the file at filePath.
     *
     * @param textToAdd text to write to the file
     * @throws IOException if an I/O error occurs during writing
     */
    private void writeToFile(String textToAdd) throws IOException {
        assert textToAdd != null : "writeToFile textToAdd must not be null";

        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(textToAdd);
        fileWriter.close();

        logger.log(Level.FINEST, "Save file characters: " + textToAdd.length());
    }

    /**
     * Loads all modules from a data file, assuming that the file at filePath contains list of all NUS modules.
     *
     */
    public void loadAllModulesData(Map<String, Module> allModulesData) throws CorruptedDataFileException {
        assert allModulesData != null : "loadAllModulesData allModulesData must not be null";
        logger.log(Level.FINEST, "Loading all modules data");

        Serialiser serialiser = new Serialiser();
        List<String> rawModulesList = load();
        try {
            List<List<String>> allModulesList = serialiser.deserialiseList(rawModulesList);
            for (List<String> moduleArgs : allModulesList) {
                if (moduleArgs.size() != 5) {
                    logger.log(Level.WARNING, "Incorrect number of arguments for module: " + moduleArgs.size());
                    break;
                }
                try {
                    List<String> deserialsedPrerequisites = serialiser.deserialiseMessage(moduleArgs.get(4));
                    List<List<String>> deserialsedPrerequisitesList = serialiser.deserialiseList(deserialsedPrerequisites);
                    if (!deserialsedPrerequisitesList.isEmpty()) {
                        logger.log(Level.WARNING, "Unable to parse prerequisites: " + moduleArgs.get(4));
                        break;
                    }
                    Module module = new Module(moduleArgs.get(0), moduleArgs.get(1), Integer.parseInt(moduleArgs.get(2)),
                            moduleArgs.get(3), new Prerequisites(deserialsedPrerequisitesList));
                    allModulesData.put(module.getCode(), module);
                    allModulesData.put(module.getName(), module);
                    logger.log(Level.FINEST, "Added module into database: " + module.getCode());
                } catch (NumberFormatException e) {
                    logger.log(Level.WARNING, "Unable to parse module credit: " + moduleArgs.get(2));
                }
            }
        } catch (CorruptedDataFileException e){
            System.out.println("System data file got corrupted :(");
        }
    }

    /**
     * Loads all modules from a data file, assuming that the file at filePath contains list of all NUS modules.
     *
     */
    public void loadAllMajorsData(Map<String, Module> allModulesData, Map<String, Major> allMajorsData)
            throws CorruptedDataFileException {
        assert allModulesData != null : "loadAllMajorsData allModulesData must not be null";
        assert allMajorsData != null : "loadAllMajorsData allMajorsData must not be null";
        logger.log(Level.FINEST, "Loading all major data");


        Serialiser serialiser = new Serialiser();
        List<String> rawMajorsList = load();
        List<List<String>> allMajorsList = serialiser.deserialiseList(rawMajorsList);
        for (List<String> majorArgs : allMajorsList) {
            if (majorArgs.size() != 3) {
                logger.log(Level.WARNING, "Incorrect number of arguments for major: " + majorArgs.size());
                break;
            }
            Major major = new Major(majorArgs.get(0), majorArgs.get(1),
                    createModuleList(allModulesData, serialiser.deserialiseMessage(majorArgs.get(2))));
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
    private ModuleList createModuleList(Map<String, Module> allModulesData, List<String> moduleCodes) throws CorruptedDataFileException {
        ModuleList moduleList = new ModuleList();
        for (String code : moduleCodes) {
            Module module = allModulesData.get(code);
            if (module != null) {
                moduleList.add(module);
            } else {
                throw new CorruptedDataFileException();
            }
        }
        return moduleList;
    }
}
