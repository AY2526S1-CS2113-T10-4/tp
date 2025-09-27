package modhero.storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Serialises a string message into a format suitable for storage.
 */
public class Serialiser {
    private static final String DELIMITER = "#";
    private static final String EXTRA_DELIMITER = "|";

    /**
     * Serialise list of string to be suitable format for storing into text file.
     *
     * @param messageList string of data to be stored
     * @return the serialised string
     */
    public String serialiseList(List<String> messageList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String message :  messageList) {
            stringBuilder.append(serialiseMessage(message));
        }
        String result = stringBuilder.toString();
        return result.length() + DELIMITER + result + EXTRA_DELIMITER;
    }

    /**
     * Serialise string to be suitable format for storing into text file.
     *
     * @param message string of data to be stored
     * @return the serialised string
     */
    public String serialiseMessage(String message) {
        return message.length() + DELIMITER + message + EXTRA_DELIMITER;
    }

    /**
     * Deserialises stored serialised array of string into nested lists.
     *
     * @param serialisedList list of serialised strings
     * @return the deserialised nested list
     */
    public List<List<String>> deserialiseList(List<String> serialisedList) {
        List<List<String>> deserialisedList = new ArrayList<>();
        for (String serialisedMessage : serialisedList) {
            List<String> message = deserialiseMessage(serialisedMessage);
            deserialisedList.add(message);
        }
        return deserialisedList;
    }

    /**
     * Deserialises serialised string in array.
     *
     * @param serialisedMessage array of string from store data
     * @return the deserialised array
     */
    public List<String> deserialiseMessage(String serialisedMessage) {
        List<String> message = new ArrayList<>();
        int currentIndex = 0;
        int serialisedTaskLength = serialisedMessage.length();

        while (currentIndex < serialisedTaskLength) {
            int delimiterIndex = serialisedMessage.indexOf(DELIMITER, currentIndex);
            boolean isDelimiterMissing = delimiterIndex == -1;
            if (isDelimiterMissing) {
                break;
            }

            int argumentLength = parseArgumentLength(serialisedMessage, currentIndex, delimiterIndex);
            boolean isArgumentLengthCorrupted = argumentLength == -1;
            if (isArgumentLengthCorrupted) {
                break;
            }

            currentIndex = delimiterIndex + DELIMITER.length();
            int nextIndex = currentIndex + argumentLength;
            if (nextIndex > serialisedTaskLength) {
                break;
            }

            String argument = serialisedMessage.substring(currentIndex, nextIndex);
            message.add(argument);
            currentIndex = nextIndex + EXTRA_DELIMITER.length();
        }

        return message;
    }

    /**
     * Parses the length of the next argument from its string representation.
     * Extracts the substring between 'start' and 'end', and converts it to an integer.
     *
     * @param serialisedTask the serialized string
     * @param start the starting index of the substring
     * @param end the ending index (exclusive) of the substring
     * @return the parsed integer length, or -1 if parsing fails
     */
    private int parseArgumentLength(String serialisedTask, int start, int end) {
        try {
            return Integer.parseInt(serialisedTask.substring(start, end));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}