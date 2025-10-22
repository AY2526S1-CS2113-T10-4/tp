package modhero.storage;

import modhero.data.major.Major;
import modhero.data.modules.Module;
import modhero.data.modules.ModuleList;
import modhero.exception.CorruptedDataFileException;

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
    public Storage(String filePath) {
        assert filePath != null && !filePath.isEmpty() : "File path must not be empty";

        this.filePath = filePath;
    }

    /** Ensures that the directory for the file path exists. */
    public void ensureFileDirectoryExist() {
        new File(filePath).getParentFile().mkdirs();

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

    /** Loads the file contents into a list of strings, each line a list element. */
    public List<String> load() {
        logger.log(Level.FINEST, "Loading file: " + filePath);

        try {
            ensureFileDirectoryExist();
            ensureFileExist();
            return readFromFile();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to load file, " + e);
            return new ArrayList<>();
        }
    }

    /** Reads all lines from the file at filePath. */
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

    /** Saves the given text string to the file, overwriting any existing content. */
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

    /** Writes the provided text content to the file at filePath. */
    private void writeToFile(String textToAdd) throws IOException {
        assert textToAdd != null : "writeToFile textToAdd must not be null";

        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(textToAdd);
        fileWriter.close();

        logger.log(Level.FINEST, "Save file characters: " + textToAdd.length());
    }

    /**
     * Loads all modules from a data file, assuming that the file at filePath
     * contains a serialized list of all NUS modules.
     */
    public void loadAllModulesData(Map<String, Module> allModulesData) throws CorruptedDataFileException {
        assert allModulesData != null : "loadAllModulesData allModulesData must not be null";
        logger.log(Level.FINEST, "Loading all modules data");

        Serialiser serialiser = new Serialiser();
        List<String> rawModulesList = load();

        if (rawModulesList == null) {
            System.out.println("⚠️ No module data file found at " + filePath);
            return;
        }

        List<List<String>> allModulesList = serialiser.deserialiseList(rawModulesList);

        for (List<String> moduleArgs : allModulesList) {
            if (moduleArgs.size() != 5) {
                logger.log(Level.WARNING, "Incorrect number of arguments for module: " + moduleArgs.size());
                break;
            }
            try {
                // Convert old-style flat prerequisites to new nested structure
                List<String> prereqFlat = serialiser.deserialiseMessage(moduleArgs.get(4));
                List<List<String>> prereqNested = new ArrayList<>();
                if (prereqFlat != null && !prereqFlat.isEmpty()) {
                    prereqNested.add(prereqFlat); // Wrap old data in a single OR group
                }

                Module module = new Module(
                        moduleArgs.get(0),
                        moduleArgs.get(1),
                        Integer.parseInt(moduleArgs.get(2)),
                        moduleArgs.get(3),
                        prereqNested
                );

                allModulesData.put(module.getCode(), module);

            } catch (NumberFormatException e) {
                System.out.println("Invalid MC value for module: " + moduleArgs);
            } catch (Exception e) {
                System.out.println("Error parsing module: " + e.getMessage());
            }
        }
        return moduleList;
    }
}
