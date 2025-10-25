package modhero.parser;

import modhero.common.util.JsonUtil;
import modhero.data.modules.Prerequisites;
import modhero.data.modules.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Parses a JSON response into a Module object.
 */
public class ModuleParser {
    private static final Logger logger = Logger.getLogger(ModuleParser.class.getName());

    public static final String CODE = "moduleCode";
    public static final String NAME = "title";
    public static final String MC = "moduleCredit";
    public static final String PREREQ = "prereqTree";
    private static final int MAX_MC = 20;

    /**
     * Parses the JSON representation of a module into a Module object.
     *
     * @param json raw JSON string from NUSMods API
     * @return a Module, or null if required fields are missing
     */
    public Module parseModule(String json) {
        if (json == null) {
            return null;
        }

        // Use static methods from JsonUtil
        try {
            String code = JsonUtil.getArg(json, CODE);
            String name = JsonUtil.getArg(json, NAME);
            String mc = JsonUtil.getArg(json, MC);
            String prereq = JsonUtil.getArg(json, PREREQ);

            if (!isValidRawData(code, name, mc)) {
                logger.log(Level.WARNING, () ->
                        String.format("Module retrieved contains null %s, %s, %s, %s", code, name, mc, prereq)
                );
                return null;
            }

            int parsedMc = parseModuleCredit(mc);
            if (parsedMc == -1) {
                logger.log(Level.WARNING, "Unable to parse module credit: " + mc);
                return null;
            }

            Prerequisites parsedPrereqObj = new Prerequisites();
            if (prereq != null) {
                try {
                    List<List<String>> parsedPrereq = parsePrereq(prereq);
                    parsedPrereqObj = new Prerequisites(parsedPrereq);
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Failed to parse prerequisites for " + code, e);
                }
            }
            return new Module(code, name, parsedMc, "core", parsedPrereqObj);
        } catch (Exception e) {
            System.out.println( " unexpected error parsing json");
            return null;
        }
    }

    /**
     * Validates that all required raw data fields are non-null.
     *
     * @param code The module code.
     * @param name The module name/title.
     * @param mc The module credit as a string.
     * @return True if all fields are non-null, false otherwise.
     */
    private boolean isValidRawData(String code, String name, String mc) {
        return code != null && name != null && mc != null;
    }

    /**
     * Parses a module credit string into an integer value.
     * Validates that the credit is within the acceptable range (1-20).
     *
     * @param rawText The raw module credit string.
     * @return The parsed module credit as an integer, or -1 if parsing fails or the value is out of range.
     */
    private Integer parseModuleCredit(String rawText) {
        try {
            int moduleCredit = Integer.parseInt(rawText);
            if (moduleCredit >= 0 && moduleCredit <= MAX_MC) {
                return moduleCredit;
            }
            return -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Recursively parses a prerequisite tree JSON string and extracts all valid module code combinations.
     * The JSON structure contains nested "or" and "and" logical operators with module code leaves.
     *
     * @param json The prerequisite tree as a JSON string from NUSMods API.
     * @return A list of lists, where each inner list represents one valid combination of module codes that satisfies the prerequisite requirement.
     */
    public static List<List<String>> parsePrereq(String json) {
        json = json.trim();
        // Parse JSON object (either "or" or "and")
        if (json.startsWith("{")) {
            int fieldNameStart = json.indexOf('\"') + 1;
            int fieldNameEnd = json.indexOf('\"', fieldNameStart);
            String logicOperator = json.substring(fieldNameStart, fieldNameEnd);
            String childArrayText = extractArrayValue(json);

            List<List<String>> parsedCombinations = new ArrayList<>();
            if (logicOperator.equals("or")) {
                // OR: collect all child combinations as separate options
                List<String> childBranches = splitTopLevel(childArrayText.substring(1, childArrayText.length() - 1), ',');
                for (String branch : childBranches) {
                    parsedCombinations.addAll(parsePrereq(branch.trim()));
                }
            } else if (logicOperator.equals("and")) {
                // AND: compute cartesian product of all child combinations
                List<String> childBranches = splitTopLevel(childArrayText.substring(1, childArrayText.length() - 1), ',');
                List<List<List<String>>> childCombinationsGroups = new ArrayList<>();
                for (String branch : childBranches) {
                    childCombinationsGroups.add(parsePrereq(branch.trim()));
                }
                parsedCombinations = cartesianProduct(childCombinationsGroups);
            }
            return parsedCombinations;
        } else {
            // Parse leaf node (module code string like "CS2113:D")
            String moduleCode = json.replaceAll("[\"']", "").split(":")[0];
            List<String> singleCombo = new ArrayList<>();
            singleCombo.add(moduleCode);
            List<List<String>> result = new ArrayList<>();
            result.add(singleCombo);
            return result;
        }
    }

    /**
     * Extracts the array value (including brackets) from a JSON object string.
     * Finds the first '[' and last ']' and returns the substring between them (inclusive).
     *
     * @param jsonObject A JSON object string containing an array value.
     * @return The array portion of the JSON string, including the brackets.
     */
    private static String extractArrayValue(String jsonObject) {
        int arrayStart = jsonObject.indexOf('[');
        int arrayEnd = jsonObject.lastIndexOf(']');
        return jsonObject.substring(arrayStart, arrayEnd + 1);
    }

    /**
     * Splits a string by a delimiter, but only at the top level (ignoring delimiters inside nested structures).
     * This method respects quoted strings and nested JSON objects/arrays.
     *
     * @param input     The string to split (typically the content inside a JSON array).
     * @param delimiter The character to split by (typically ',').
     * @return A list of substrings split at top-level delimiters only.
     */
    private static List<String> splitTopLevel(String input, char delimiter) {
        List<String> elements = new ArrayList<>();
        StringBuilder currentElement = new StringBuilder();
        int nestingDepth = 0;
        boolean insideQuotes = false;
        for (int i = 0; i < input.length(); ++i) {
            char currentChar = input.charAt(i);
            if (currentChar == '"' || currentChar == '\'') {
                insideQuotes = !insideQuotes;
                currentElement.append(currentChar);
            } else if (!insideQuotes && (currentChar == '[' || currentChar == '{')) {
                nestingDepth++;
                currentElement.append(currentChar);
            } else if (!insideQuotes && (currentChar == ']' || currentChar == '}')) {
                nestingDepth--;
                currentElement.append(currentChar);
            } else if (!insideQuotes && currentChar == delimiter && nestingDepth == 0) {
                elements.add(currentElement.toString().trim());
                currentElement.setLength(0);
            } else {
                currentElement.append(currentChar);
            }
        }
        if (!currentElement.isEmpty()) {
            elements.add(currentElement.toString().trim());
        }
        return elements;
    }

    /**
     * Computes the cartesian product of multiple lists of module code combinations.
     * Used to combine results from "and" prerequisite branches.
     * <p>
     * Example: Given [[A, B], [C]] and [[X], [Y]], produces:
     * [[A, X], [A, Y], [B, X], [B, Y], [C, X], [C, Y]]
     *
     * @param groupsOfCombinations A list of groups, where each group contains multiple possible module code combinations.
     * @return A list of all possible combinations formed by taking one element from each group and merging them.
     */
    private static List<List<String>> cartesianProduct(List<List<List<String>>> groupsOfCombinations) {
        List<List<String>> cartesianResult = new ArrayList<>();
        if (groupsOfCombinations == null || groupsOfCombinations.isEmpty()) return cartesianResult;
        cartesianResult.add(new ArrayList<>());

        for (List<List<String>> combinationGroup : groupsOfCombinations) {
            List<List<String>> nextResult = new ArrayList<>();
            for (List<String> partialCombo : cartesianResult) {
                for (List<String> newCombo : combinationGroup) {
                    List<String> combined = new ArrayList<>(partialCombo);
                    combined.addAll(newCombo);
                    nextResult.add(combined);
                }
            }
            cartesianResult = nextResult;
        }
        return cartesianResult;
    }
}
