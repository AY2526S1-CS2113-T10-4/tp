package modhero.common.util;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static modhero.common.Constants.FORMATED_STRING_END_DELIMITER;
import static modhero.common.Constants.FORMATED_STRING_START_DELIMITER;

/**
 * Provides utility methods for serialising strings and string lists into a
 * length-prefixed format suitable for persistent storage.
 * Serialisation Format: [length of content][START_DELIMITER][content][END_DELIMITER]
 */
public class Serialiser {
    private static final Logger logger = Logger.getLogger(Serialiser.class.getName());

    /**
     * Serialises a list of strings into a format suitable for file storage.
     *
     * @param messageList the list of strings to serialise
     * @return the serialised representation of the entire list
     */
    public static String serialiseList(List<String> messageList) {
        assert messageList != null : "serialiseList message list must not be null";
        logger.log(Level.FINEST, "Serialising list");

        StringBuilder stringBuilder = new StringBuilder();
        for (String message : messageList) {
            stringBuilder.append(serialiseMessage(message));
        }
        String serialisedList = serialiseMessage(stringBuilder.toString());

        logger.log(Level.FINEST, "Successful serialising list");
        return serialisedList;
    }

    /**
     * Serialises a single string into a format suitable for file storage.
     *
     * @param message the string to serialise
     * @return the serialised string
     */
    public static String serialiseMessage(String message) {
        assert message != null : "serialiseMessage message must not be null";

        String serialisedMessage = message.length() + FORMATED_STRING_START_DELIMITER + message + FORMATED_STRING_END_DELIMITER;

        logger.log(Level.FINEST, "Successful serialising message: " + serialisedMessage);
        return serialisedMessage;
    }
}
