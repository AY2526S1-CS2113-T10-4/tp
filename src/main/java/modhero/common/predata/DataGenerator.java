package modhero.common.predata;

import static modhero.common.Constants.FilePathConstants.MAJOR_FILE_PATH;
import static modhero.common.Constants.FilePathConstants.MODULES_FILE_PATH;

import modhero.common.util.SerialisationUtil;
import modhero.data.nusmods.NusmodsAPIClient;
import modhero.parser.ModuleParser;
import modhero.storage.MajorStorage;
import modhero.storage.ModuleStorage;
import modhero.data.modules.Module;
import modhero.data.modules.Prerequisites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Generates serialized text files for module and major data used by ModHero.
 * Requires an active internet connection to fetch data from NUSMods.
 */
public class DataGenerator {
    /**
     * Academic year to fetch module data from NUSMods API.
     */
    private static final String ACAD_YEAR = "2025-2026";

    /**
     * Entry point for generating modules.txt and majors.txt.
     * Fetches, serializes, and saves live module and major data.
     */
    public static void main(String[] args) {
        // Generate Module Data
        System.out.println("Fetching live module data from NUSMods API...");
        String moduleFileContent = generateModulesTxt();
        if (moduleFileContent != null) {
            ModuleStorage moduleStorage = new ModuleStorage(MODULES_FILE_PATH);
            moduleStorage.saveToTextFile(moduleFileContent);
            System.out.println("Successfully saved to " + MODULES_FILE_PATH);
        } else {
            System.err.println("Failed to generate module data. File not saved.");
        }

        // Generate Major Data
        System.out.println("\nGenerating major data...");
        String majorFileContent = generateMajorsTxt();
        MajorStorage majorStorage = new MajorStorage(MAJOR_FILE_PATH);

        majorStorage.saveToTextFile(majorFileContent);
        System.out.println("Successfully saved to " + MAJOR_FILE_PATH);
    }

    /**
     * Fetches module data from NUSMods, parses it, and serializes the results.
     * Uses dummy data if fetching or parsing fails.
     *
     * @return serialized text content for modules.txt, or null if an error occurs
     */
    private static String generateModulesTxt() {
        NusmodsAPIClient client = new NusmodsAPIClient();
        ModuleParser parser = new ModuleParser();
        StringBuilder fileContent = new StringBuilder();

        List<String> allModules = Arrays.asList(
                // Computer Science
                "CS1101S", "MA1522", "CS1231S", "ES2660",
                "CS2030S", "CS2040S", "MA1521",
                "CS2100", "CS2101", "CS2103T", "CS2109S", "IS1108",
                "CS2106", "CS3230", "ST2334",
                // Computer Engineering
                "CG1111A", "EG1311", "MA1511", "MA1512", "CS1010",
                "CG2111A", "DTK1234", "MA1508E", "EE2026", "CS2040C",
                "CS2107", "CG2023", "CS2113", "CS1231", "EE2211", "ST2334",
                "CG2027", "CG2028", "CG2271", "CG3201", "EE4204", "CG3207"
        );

        for (String code : allModules) {
            try {
                String json = client.fetchModuleDataSafely(ACAD_YEAR, code);
                String moduleCode = code;
                String moduleName;
                String moduleMc;
                String preclude;
                String serialisedPrereqsBlob;

                if (json == null) {
                    System.err.println("Failed to fetch data for " + code + ": null response. Using dummy data.");
                    moduleName = code;
                    moduleMc = "4";
                    preclude = "";
                    serialisedPrereqsBlob = serializePrereqList(new ArrayList<>()); // Empty prerequisites
                } else {
                    Module module = parser.parseModule(json);

                    if (module == null) {
                        System.err.println("ModuleParser failed to parse " + code + ". Using dummy data.");
                        moduleName = code;
                        moduleMc = "4";
                        preclude = "";
                        serialisedPrereqsBlob = serializePrereqList(new ArrayList<>());
                    } else {
                        moduleName = module.getName();
                        moduleMc = String.valueOf(module.getMc());
                        preclude = module.getPreclude();
                        Prerequisites prereqs = module.getPrerequisites();

                        // Get the prerequisite combinations from the Prerequisites object
                        // List<List<String>> prereqCombos = prereqs.getPrerequisiteCombinations();
                        serialisedPrereqsBlob = prereqs.toFormatedString();
                    }
                }

                String desc = "core";

                // Build the module line with triple-serialized prerequisites
                String line = SerialisationUtil.serialiseMessage(moduleCode)
                        + SerialisationUtil.serialiseMessage(moduleName)
                        + SerialisationUtil.serialiseMessage(moduleMc)
                        + SerialisationUtil.serialiseMessage(desc)
                        + SerialisationUtil.serialiseMessage(preclude)
                        + SerialisationUtil.serialiseMessage(serialisedPrereqsBlob);

                fileContent.append(line).append(System.lineSeparator());

            } catch (Exception e) {
                System.err.println("Failed to generate data for " + code + ": " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        return fileContent.toString();
    }

    /**
     * Serializes nested prerequisite structures into a compact, text-safe format.
     *
     * @param prereqCombinations list of OR-groups of prerequisite module codes
     * @return doubly serialized string representing prerequisites
     */
    private static String serializePrereqList(List<List<String>> prereqCombinations) {
        if (prereqCombinations == null || prereqCombinations.isEmpty()) {
            return "";
        }

        StringBuilder prereqBlobBuilder = new StringBuilder();

        for (List<String> combo : prereqCombinations) {
            StringBuilder comboBuilder = new StringBuilder();
            for (String moduleCode : combo) {
                comboBuilder.append(SerialisationUtil.serialiseMessage(moduleCode));
            }
            prereqBlobBuilder.append(SerialisationUtil.serialiseMessage(comboBuilder.toString()));
        }

        // Return the doubly-serialized blob
        return prereqBlobBuilder.toString();
    }

    /**
     * Builds major data entries for majors.txt based on predefined module lists.
     *
     * @return serialized text content for majors.txt
     */
    private static String generateMajorsTxt() {
        StringBuilder fileContent = new StringBuilder();
        MajorSchedule majorSchedule = new MajorSchedule();

        // CS
        List<String> csCodes = Arrays.asList(
                "CS1101S", "MA1522", "CS1231S", "ES2660",
                "CS2030S", "CS2040S", "MA1521",
                "CS2100", "CS2101", "CS2103T", "CS2109S", "IS1108",
                "CS2106", "CS3230", "ST2334"
        );
        Map<String, int[]> csSchedule = majorSchedule.getSchedule("computer science");
        fileContent.append(buildMajorLine("Computer Science", "CS", csCodes, csSchedule))
                .append(System.lineSeparator());

        // CEG
        List<String> cegCodes = Arrays.asList(
                "CG1111A", "EG1311", "MA1511", "MA1512", "CS1010", "CG2111A", "DTK1234", "MA1508E",
                "EE2026", "CS2040C", "CS2107", "CG2023", "CS2113", "CS1231", "EE2211", "ST2334",
                "CG2027", "CG2028", "CG2271", "CG3201", "EE4204", "CG3207"
        );
        Map<String, int[]> cegSchedule = majorSchedule.getSchedule("computer engineering");
        fileContent.append(buildMajorLine("Computer Engineering", "CEG", cegCodes, cegSchedule))
                .append(System.lineSeparator());

        return fileContent.toString();
    }

    /**
     * Builds one serialized major entry with its modules, year, and semester info.
     *
     * @param name major name
     * @param abbr major abbreviation
     * @param moduleCodes module codes under the major
     * @param schedule map of module codes to year-semester pairs
     * @return serialized major line
     */
    private static String buildMajorLine(String name, String abbr,
                                         List<String> moduleCodes, Map<String, int[]> schedule) {
        StringBuilder modulesBlobBuilder = new StringBuilder();
        for (String code : moduleCodes) {
            int[] yearSem = schedule.get(code);
            if (yearSem == null) {
                yearSem = new int[]{0, 0};
            }

            String tripletContent = SerialisationUtil.serialiseMessage(code)
                    + SerialisationUtil.serialiseMessage(String.valueOf(yearSem[0]))
                    + SerialisationUtil.serialiseMessage(String.valueOf(yearSem[1]));

            modulesBlobBuilder.append(SerialisationUtil.serialiseMessage(tripletContent));
        }
        String modulesBlob = modulesBlobBuilder.toString();
        String line = SerialisationUtil.serialiseMessage(name)
                + SerialisationUtil.serialiseMessage(abbr)
                + SerialisationUtil.serialiseMessage(modulesBlob);
        return line;
    }
}
