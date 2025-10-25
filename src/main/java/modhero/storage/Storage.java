package modhero.storage;

import modhero.common.util.Deserialiser;
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
    public Storage(String filePath) {
        assert filePath != null && !filePath.isEmpty() : "File path must not be empty";

        this.filePath = filePath;
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
            logger.log(Level.WARNING, "Failed to load file, " + e);
            return new ArrayList<>();
        }
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
     * Ensures that the directory for the file path exists.
     * Creates directories if not present.
     */
    private void ensureFileDirectoryExist() {
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
}
