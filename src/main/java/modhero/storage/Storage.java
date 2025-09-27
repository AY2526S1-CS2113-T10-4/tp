package modhero.storage;

import modhero.data.modules.Module;
import modhero.data.modules.ModuleList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Handles loading from and saving to a text file.
 * Provides persistence for data by reading and writing plain text.
 */
public class Storage {

    private final String filePath;

    /**
     * Creates a new {@code Storage} tied to the specified file path.
     *
     * @param filePath the path of the file to load from or save to
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Ensures that the directory for the file path exists.
     * Creates directories if not present.
     */
    public void ensureFileDirectoryExist() {
        new File(filePath).getParentFile().mkdirs();
    }

    /**
     * Loads the file contents into a list of strings, each line a list element.
     *
     * @return list of lines from file, or null if file not found
     */
    public List<String> load() {
        try {
            ensureFileDirectoryExist();
            return readFromFile();
        } catch (FileNotFoundException e) {
            return null;
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
        return rawTaskList;
    }

    /**
     * Saves the given text string to the file, overwriting any existing content.
     *
     * @param textToAdd the text content to save
     */
    public void save(String textToAdd) {
        try {
            ensureFileDirectoryExist();
            writeToFile(textToAdd);
        } catch (IOException e) {
            System.out.println("Failed save file");
        }
    }

    /**
     * Writes the provided text content to the file at filePath.
     *
     * @param textToAdd text to write to the file
     * @throws IOException if an I/O error occurs during writing
     */
    private void writeToFile(String textToAdd) throws IOException {
        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(textToAdd);
        fileWriter.close();
    }

    /**
     * Loads all modules from a data file, assuming that the file at filePath contains list of all NUS modules.
     *
     */
    public void loadAllModulesData(Map<String, Module> allModulesData) {
        Serialiser serialiser = new Serialiser();
        List<String> rawModulesList = load();
        List<List<String>> allModulesList = serialiser.deserialiseList(rawModulesList);
        for (List<String> moduleArgs : allModulesList) {
            try {
                Module module = new Module(moduleArgs.get(0), moduleArgs.get(1), Integer.parseInt(moduleArgs.get(2)), moduleArgs.get(3), serialiser.deserialiseMessage(moduleArgs.get(4)));
                allModulesData.put(module.getCode(), module);
            } catch (NumberFormatException e) {
                System.out.println("Invalid module code");
            }
        }
    }
}
