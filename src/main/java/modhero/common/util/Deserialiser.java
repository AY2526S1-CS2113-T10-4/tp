package modhero.common.util;

import modhero.common.exceptions.CorruptedDataFileException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static modhero.common.Constants.FORMATED_STRING_END_DELIMITER;
import static modhero.common.Constants.FORMATED_STRING_START_DELIMITER;

/**
 * Provides utility methods for deserialising strings back into their original string lists.
 */
public class Deserialiser {
    private static final Logger logger = Logger.getLogger(Deserialiser.class.getName());

    /**
     * Deserialises a list of serialised strings into a nested list structure.
     *
     * @param serialisedList the list of serialised strings to deserialise
     * @return a nested list where each inner list contains the deserialised messages from one serialised string
     * @throws CorruptedDataFileException if any serialised string is corrupted or cannot be parsed
     */
    public static List<List<String>> deserialiseList(List<String> serialisedList) throws CorruptedDataFileException {
        assert serialisedList != null && !serialisedList.isEmpty() : "deserialiseList list must not be null or empty";
        logger.log(Level.FINEST, "Deserialising List");

        List<List<String>> deserialisedList = new ArrayList<>();
        for (String serialisedMessage : serialisedList) {
            List<String> message = deserialiseMessage(serialisedMessage);
            if (message == null) {
                logger.log(Level.WARNING, "Corrupted data encountered during list deserialisation");
                throw new CorruptedDataFileException();
            }
            deserialisedList.add(message);
        }

        logger.log(Level.FINEST, "Successful deserialising list");
        return deserialisedList;
    }

    /**
     * Deserialises a single serialised string into a list of its component messages.
     *
     * @param serialisedMessage the serialised string to deserialise
     * @return a list of deserialised messages
     */
    public static List<String> deserialiseMessage(String serialisedMessage) {
        assert serialisedMessage != null && !serialisedMessage.isEmpty() : "deserialiseMessage serialisedMessage must not be null or empty";

        List<String> message = new ArrayList<>();
        int currentIndex = 0;
        int serialisedTaskLength = serialisedMessage.length();

        while (currentIndex < serialisedTaskLength) {
            int delimiterIndex = serialisedMessage.indexOf(FORMATED_STRING_START_DELIMITER, currentIndex);
            boolean isDelimiterMissing = delimiterIndex == -1;
            if (isDelimiterMissing) {
                logger.log(Level.WARNING, "Delimiter missing during deserialisation, " + serialisedMessage);
                return null;
            }

            int argumentLength = parseComponentLength(serialisedMessage, currentIndex, delimiterIndex);
            boolean isArgumentLengthCorrupted = argumentLength == -1;
            if (isArgumentLengthCorrupted) {
                logger.log(Level.WARNING, "Invalid argument length encountered, " + serialisedMessage);
                return null;
            }

            currentIndex = delimiterIndex + FORMATED_STRING_START_DELIMITER.length();
            int nextIndex = currentIndex + argumentLength;
            if (nextIndex > serialisedTaskLength) {
                logger.log(Level.WARNING, "Argument length exceeds message size, " + serialisedMessage);
                return null;
            }

            String argument = serialisedMessage.substring(currentIndex, nextIndex);
            message.add(argument);
            currentIndex = nextIndex + FORMATED_STRING_END_DELIMITER.length();
        }

        logger.log(Level.FINEST, "Successful deserialising:" + serialisedMessage);
        return message;
    }

    /**
     * Parses the length of the next component from its string representation.
     * Extracts the substring between 'start' and 'end', and converts it to an integer.
     *
     * @param serialisedTask the serialized string
     * @param startIndex the starting index of the substring
     * @param endIndex the ending index (exclusive) of the substring
     * @return the parsed integer length, or -1 if parsing fails
     */
    private static int parseComponentLength(String serialisedTask, int startIndex, int endIndex) {
        assert serialisedTask != null && !serialisedTask.isEmpty() : "String serialisedTask must not be null or empty";
        assert startIndex >= 0 && startIndex < serialisedTask.length() : "Integer start index must be within the string length";
        assert endIndex >= 0 && endIndex < serialisedTask.length() : "Integer end index must be within the string length";

        try {
            return Integer.parseInt(serialisedTask.substring(startIndex, endIndex));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
