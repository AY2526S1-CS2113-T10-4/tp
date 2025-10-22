package modhero.data;

import modhero.data.modules.Module;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Fetch module data from NUSMOD.
 */
public class Nusmod {
    public static final Logger logger = Logger.getLogger(Nusmod.class.getName());

    private final String CODE = "moduleCode";
    private final String NAME = "title";
    private final String MC = "moduleCredit";
    private final String PREREQ = "prereqTree";

    /**
     * Fetches raw module data from the NUSMods API for a given academic year and module code.
     *
     * @param acadYear The academic year in format "YYYY-YYYY" (e.g., "2023-2024").
     * @param moduleCode The module code (e.g., "CS2030").
     * @return The raw JSON response as a string.
     * @throws Exception If the HTTP request fails or encounters network issues.
     */
    private String fetchModuleData(String acadYear, String moduleCode) throws Exception {
        String url = "https://api.nusmods.com/v2/" + acadYear + "/modules/" + moduleCode + ".json";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    /**
     * Extracts the value associated with a given key from a JSON string.
     * Handles both JSON objects (enclosed in {}) and string/primitive values.
     *
     * @param json The JSON string to parse.
     * @param dataType The key name to search for in the JSON.
     * @return The extracted value as a string, or null if the key is not found.
     */
    public String getArg(String json, String dataType) {
        int keyIndex = findKeyIndex(json, dataType);
        if (keyIndex == -1) {
            return null;
        }

        int valueStart = findValueStart(json, keyIndex);
        char firstChar = json.charAt(valueStart);

        if (firstChar == '{') {
            return extractJsonObject(json, valueStart);
        } else if (firstChar == '\"') {
            return extractJsonString(json, valueStart);
        } else {
            return extractRawValue(json, valueStart);
        }
    }

    /**
     * Retrieves and constructs a Module object from the NUSMods API.
     * Fetches the module data, parses required fields, validates them,
     * and returns a fully constructed Module object.
     *
     * @param acadYear The academic year in format "YYYY-YYYY" (e.g., "2023-2024").
     * @param moduleCode The module code (e.g., "CS2030").
     * @return A Module object containing the parsed module information, or null if
     *         fetching or parsing fails.
     */
    public Module getModule(String acadYear, String moduleCode) {
        String json = fetchModuleDataSafely(acadYear, moduleCode);
        if (json == null) {
            return null;
        }

        String code = getArg(json, CODE);
        String name = getArg(json, NAME);
        String mc = getArg(json, MC);
        String prereq = getArg(json, PREREQ);

        if (!isValidRawData(code, name, mc, prereq)) {
            logInvalidRawData(code, name, mc, prereq);
            return null;
        }

        int parsedMc = parseModuleCredit(mc);
        List<String> parsedPrereq = parsePrereq(prereq);

        if (!isValidParsedData(parsedMc, parsedPrereq)) {
            logParsingErrors(parsedMc, parsedPrereq, mc, prereq);
            return null;
        }

        return new Module(code, name, parsedMc, "core", parsedPrereq);
    }

    /**
     * Finds the index of a given key in a JSON string.
     *
     * @param json The JSON string to search in.
     * @param key The key name to find.
     * @return The index of the key, or -1 if not found.
     */
    private int findKeyIndex(String json, String key) {
        return json.indexOf('"' + key + '"');
    }

    /**
     * Finds the starting index of the value associated with a key in JSON.
     * Skips whitespace after the colon separator.
     *
     * @param json The JSON string.
     * @param keyIndex The index where the key was found.
     * @return The index where the value starts.
     */
    private int findValueStart(String json, int keyIndex) {
        int colonIdx = json.indexOf(':', keyIndex);
        int idx = colonIdx + 1;
        while (idx < json.length() && Character.isWhitespace(json.charAt(idx))) {
            idx++;
        }
        return idx;
    }

    /**
     * Extracts a JSON object (enclosed in curly braces) from a JSON string.
     * Handles nested objects by counting opening and closing braces.
     *
     * @param json The JSON string.
     * @param startIdx The index where the object starts (position of opening brace).
     * @return The extracted JSON object as a string, including the braces, or null if malformed.
     */
    private String extractJsonObject(String json, int startIdx) {
        int braceCount = 0;
        for (int i = startIdx; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                braceCount++;
            } else if (c == '}') {
                braceCount--;
                if (braceCount == 0) {
                    return json.substring(startIdx, i + 1);
                }
            }
        }
        return null;
    }

    /**
     * Extracts a string value (enclosed in quotes) from a JSON string.
     *
     * @param json The JSON string.
     * @param startIdx The index where the opening quote is located.
     * @return The extracted string value without the surrounding quotes.
     */
    private String extractJsonString(String json, int startIdx) {
        int quoteStart = startIdx + 1;
        int quoteEnd = json.indexOf('\"', quoteStart);
        return json.substring(quoteStart, quoteEnd);
    }

    /**
     * Extracts a raw value (number, boolean, or null) from a JSON string.
     *
     * @param json The JSON string.
     * @param startIdx The index where the raw value starts.
     * @return The extracted raw value as a string.
     */
    private String extractRawValue(String json, int startIdx) {
        int endIdx = startIdx;
        while (endIdx < json.length() &&
                (Character.isLetterOrDigit(json.charAt(endIdx))
                        || json.charAt(endIdx) == '.'
                        || json.charAt(endIdx) == '-')) {
            endIdx++;
        }
        return json.substring(startIdx, endIdx);
    }

    /**
     * Safely fetches module data by handling exceptions.
     * Logs any errors that occur during the fetch operation.
     *
     * @param acadYear The academic year in format "YYYY-YYYY" (e.g., "2023-2024").
     * @param moduleCode The module code (e.g., "CS2030").
     * @return The raw JSON response as a string, or null if an error occurs.
     */
    private String fetchModuleDataSafely(String acadYear, String moduleCode) {
        try {
            return fetchModuleData(acadYear, moduleCode);
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Failed to fetch module data", ex);
            return null;
        }
    }

    /**
     * Validates that all required raw data fields are non-null.
     *
     * @param code The module code.
     * @param name The module name/title.
     * @param mc The module credit as a string.
     * @param prereq The prerequisite tree as a JSON string.
     * @return True if all fields are non-null, false otherwise.
     */
    private boolean isValidRawData(String code, String name, String mc, String prereq) {
        return code != null && name != null && mc != null && prereq != null;
    }

    /**
     * Logs a warning when raw data validation fails, indicating which fields are null.
     *
     * @param code The module code.
     * @param name The module name/title.
     * @param mc The module credit as a string.
     * @param prereq The prerequisite tree as a JSON string.
     */
    private void logInvalidRawData(String code, String name, String mc, String prereq) {
        logger.log(Level.WARNING, () ->
                String.format("Module retrieved contains null %s, %s, %s, %s",
                        code, name, mc, prereq));
    }

    /**
     * Validates that parsed data is valid (module credit is positive and prerequisites exist).
     *
     * @param parsedMc The parsed module credit value.
     * @param parsedPrereq The list of parsed prerequisite module codes.
     * @return True if both parsed values are valid, false otherwise.
     */
    private boolean isValidParsedData(int parsedMc, List<String> parsedPrereq) {
        return parsedMc != -1 && !parsedPrereq.isEmpty();
    }

    /**
     * Logs warnings when parsing fails for module credit or prerequisites.
     *
     * @param parsedMc The parsed module credit value (-1 indicates parsing failure).
     * @param parsedPrereq The list of parsed prerequisite module codes (empty indicates failure).
     * @param mc The original module credit string that failed to parse.
     * @param prereq The original prerequisite string that failed to parse.
     */
    private void logParsingErrors(int parsedMc, List<String> parsedPrereq,
                                  String mc, String prereq) {
        if (parsedMc == -1) {
            logger.log(Level.WARNING, "Unable to parse module credit: " + mc);
        }
        if (parsedPrereq.isEmpty()) {
            logger.log(Level.WARNING, "Unable to parse prerequisites: " + prereq);
        }
    }

    /**
     * Parses a module credit string into an integer value.
     * Validates that the credit is within the acceptable range (1-20).
     *
     * @param rawText The raw module credit string.
     * @return The parsed module credit as an integer, or -1 if parsing fails
     *         or the value is out of range.
     */
    private Integer parseModuleCredit(String rawText) {
        try {
            int moduleCredit = Integer.parseInt(rawText);
            if (moduleCredit > 0 && moduleCredit <= 20) {
                return moduleCredit;
            }
            return -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Parses the prerequisite tree JSON string and extracts module codes.
     *
     * @param rawText The prerequisite tree as a JSON string.
     * @return A list of prerequisite module codes, or an empty list if parsing fails.
     */
    private List<String> parsePrereq(String rawText) {
        String arrayContent = extractArrayContent(rawText);
        if (arrayContent == null) {
            return new ArrayList<>();
        }

        String[] entries = splitArrayEntries(arrayContent);
        return parseModuleCodes(entries);
    }

    /**
     * Extracts the content within square brackets from a JSON array string.
     *
     * @param json The JSON string containing an array.
     * @return The content between the square brackets, or null if no array is found.
     */
    private String extractArrayContent(String json) {
        int arrayStart = json.indexOf('[');
        int arrayEnd = json.lastIndexOf(']');

        if (arrayStart == -1 || arrayEnd == -1) {
            return null;
        }

        return json.substring(arrayStart + 1, arrayEnd);
    }

    /**
     * Splits a JSON array content string into individual entries by comma.
     *
     * @param arrayContent The content of the JSON array (without brackets).
     * @return An array of individual entry strings.
     */
    private String[] splitArrayEntries(String arrayContent) {
        return arrayContent.split(",");
    }

    /**
     * Parses module codes from an array of JSON entry strings.
     *
     * @param entries The array of JSON entry strings.
     * @return A list of extracted module codes.
     */
    private List<String> parseModuleCodes(String[] entries) {
        List<String> moduleCodes = new ArrayList<>();

        for (String entry : entries) {
            String moduleCode = extractModuleCode(entry);
            if (moduleCode != null) {
                moduleCodes.add(moduleCode);
            }
        }

        return moduleCodes;
    }

    /**
     * Extracts a module code from a single JSON entry string.
     * Expects format like "CS2030:D" and extracts the part before the colon.
     *
     * @param entry A single JSON entry string.
     * @return The extracted module code, or null if the format is invalid.
     */
    private String extractModuleCode(String entry) {
        int quoteStart = entry.indexOf('"');
        int colon = entry.indexOf(':');

        if (quoteStart != -1 && colon != -1) {
            return entry.substring(quoteStart + 1, colon);
        }

        return null;
    }
}
