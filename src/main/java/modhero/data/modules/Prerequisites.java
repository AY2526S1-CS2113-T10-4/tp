package modhero.data.modules;

import modhero.common.util.Serialiser;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Prerequisites {
    public static final Logger logger = Logger.getLogger(Prerequisites.class.getName());

    private List<List<String>> prereq;

    public Prerequisites() {
        this.prereq = new ArrayList<>();
    }

    public Prerequisites(List<List<String>> prereq) {
        this.prereq = prereq;
    }

    public List<List<String>> getPrereq() {
        return prereq;
    }

    public String toFormatedString() {
        logger.log(Level.FINEST, "Serialising prerequisites");

        Serialiser serialiser = new Serialiser();
        StringBuilder stringBuilder = new StringBuilder();
        for (List<String> messageList : prereq) {
            stringBuilder.append(serialiser.serialiseList(messageList));
        }
        String serialisedList = serialiser.serialiseMessage(stringBuilder.toString());

        logger.log(Level.FINEST, "Successful serialising list");
        return serialisedList;
    }
}
