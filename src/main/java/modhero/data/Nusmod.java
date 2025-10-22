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
 * Fetch module data from NUSMODS API and parse into Module objects.
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
     */
    public String getArg(String json, String dataType) {
        int keyIndex = findKeyIndex(json, dataType);
        if (keyIndex == -1) return null;

        int valueStart = findValueStart(json, keyIndex);
        char firstChar = json.charAt(valueStart);

        if (firstChar == '{') return extractJsonObject(json, valueStart);
        else if (firstChar == '"') return extractJsonString(json, valueStart);
        else return extractRawValue(json, valueStart);
    }

    /**
     * Retrieves and constructs a Module object from the NUSMods API.
     */
    public Module getModule(String acadYear, String moduleCode) {
        String json = fetchModuleDataSafely(acadYear, moduleCode);
        if (json == null) return null;

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

        // wrap parsedPrereq inside a nested list to match List<List<String>>
        return new Module(code, name, parsedMc, "core", List.of(parsedPrereq));
    }

    /** Helper for JSON key lookup */
    private int findKeyIndex(String json, String key) {
        return json.indexOf('"' + key + '"');
    }

    private int findValueStart(String json, int keyIndex) {
        int colonIdx = json.indexOf(':', keyIndex);
        int idx = colonIdx + 1;
        while (idx < json.length() && Character.isWhitespace(json.charAt(idx))) idx++;
        return idx;
    }

    private String extractJsonObject(String json, int startIdx) {
        int braceCount = 0;
        for (int i = startIdx; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') braceCount++;
            else if (c == '}') {
                braceCount--;
                if (braceCount == 0) return json.substring(startIdx, i + 1);
            }
        }
        return null;
    }

    private String extractJsonString(String json, int startIdx) {
        int quoteStart = startIdx + 1;
        int quoteEnd = json.indexOf('"', quoteStart);
        return json.substring(quoteStart, quoteEnd);
    }

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

    /** Safe fetch with logging */
    private String fetchModuleDataSafely(String acadYear, String moduleCode) {
        try {
            return fetchModuleData(acadYear, moduleCode);
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Failed to fetch module data", ex);
            return null;
        }
    }

    private boolean isValidRawData(String code, String name, String mc, String prereq) {
        return code != null && name != null && mc != null && prereq != null;
    }

    private void logInvalidRawData(String code, String name, String mc, String prereq) {
        logger.log(Level.WARNING, () ->
                String.format("Module retrieved contains null fields: %s, %s, %s, %s",
                        code, name, mc, prereq));
    }

    private boolean isValidParsedData(int parsedMc, List<String> parsedPrereq) {
        return parsedMc != -1 && !parsedPrereq.isEmpty();
    }

    private void logParsingErrors(int parsedMc, List<String> parsedPrereq,
                                  String mc, String prereq) {
        if (parsedMc == -1)
            logger.log(Level.WARNING, "Unable to parse module credit: " + mc);
        if (parsedPrereq.isEmpty())
            logger.log(Level.WARNING, "Unable to parse prerequisites: " + prereq);
    }

    private Integer parseModuleCredit(String rawText) {
        try {
            int moduleCredit = Integer.parseInt(rawText);
            return (moduleCredit > 0 && moduleCredit <= 20) ? moduleCredit : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private List<String> parsePrereq(String rawText) {
        String arrayContent = extractArrayContent(rawText);
        if (arrayContent == null) return new ArrayList<>();

        String[] entries = splitArrayEntries(arrayContent);
        return parseModuleCodes(entries);
    }

    private String extractArrayContent(String json) {
        int arrayStart = json.indexOf('[');
        int arrayEnd = json.lastIndexOf(']');
        if (arrayStart == -1 || arrayEnd == -1) return null;
        return json.substring(arrayStart + 1, arrayEnd);
    }

    private String[] splitArrayEntries(String arrayContent) {
        return arrayContent.split(",");
    }

    private List<String> parseModuleCodes(String[] entries) {
        List<String> moduleCodes = new ArrayList<>();
        for (String entry : entries) {
            String moduleCode = extractModuleCode(entry);
            if (moduleCode != null) moduleCodes.add(moduleCode);
        }
        return moduleCodes;
    }

    private String extractModuleCode(String entry) {
        int quoteStart = entry.indexOf('"');
        int colon = entry.indexOf(':');
        if (quoteStart != -1 && colon != -1) {
            return entry.substring(quoteStart + 1, colon);
        }
        return null;
    }
}
