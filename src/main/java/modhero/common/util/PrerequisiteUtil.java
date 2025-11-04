package modhero.common.util;

import modhero.data.modules.Prerequisites;
import modhero.exceptions.ModuleAdditionBlockedException;
import modhero.exceptions.ModuleDeletionBlockedException;
import modhero.data.modules.Module;

import java.util.List;

/**
 * Utility methods for checking and validating module prerequisites.
 */
public final class PrerequisiteUtil {

    private PrerequisiteUtil() {} // prevent instantiation

    /**
     * Checks if a single prerequisite module code is satisfied by completed or exempted modules.
     * Supports wildcard prefix match (ending with '%').
     */
    private static boolean isPrerequisiteSatisfied(String prereqCode, List<String> completedCodes, List<String> exemptedModules) {
        if (prereqCode.endsWith("%") && prereqCode.length() > 1) {
            String prefix = prereqCode.substring(0, prereqCode.length() - 1);
            return completedCodes.stream().anyMatch(c -> c.startsWith(prefix));
        }
        return (completedCodes.contains(prereqCode) || exemptedModules.contains(prereqCode));
    }

    /**
     * Checks if a set of prerequisite groups (OR logic) is satisfied.
     *
     * @param prereqSets list of OR-groups (each group is a list of module codes)
     * @param completedCodes modules already completed
     * @param exemptedModules modules exempted from prerequisites
     * @return true if at least one group is fully satisfied
     */
    public static boolean arePrerequisitesMet(List<List<String>> prereqSets, List<String> completedCodes, List <String> exemptedModules) {
        if (prereqSets == null || prereqSets.isEmpty()) return true;
        return prereqSets.stream().anyMatch(
                option -> option.stream().allMatch(code -> isPrerequisiteSatisfied(code, completedCodes, exemptedModules))
        );
    }

    /**
     * Validates that a module's prerequisites are satisfied.
     *
     * @param moduleCode the module being added
     * @param prereqs its prerequisites object
     * @param completedCodes modules already completed
     * @param exemptedModules modules exempted from prerequisites
     * @throws ModuleAdditionBlockedException if prerequisites are not satisfied
     */
    public static void validatePrerequisites(String moduleCode, Prerequisites prereqs, List<String> completedCodes, List<String> exemptedModules)
            throws ModuleAdditionBlockedException{
        if (prereqs == null || prereqs.getPrereq() == null || prereqs.getPrereq().isEmpty()) return;

        boolean satisfied = arePrerequisitesMet(prereqs.getPrereq(), completedCodes, exemptedModules);

        if (!satisfied) {
            throw new ModuleAdditionBlockedException(moduleCode, prereqs.toString());
        }
    }

    /**
     * Validates that deleting a module does not break future modules' prerequisites.
     *
     * @param moduleCodeToDelete the module being deleted
     * @param futureModules modules scheduled after the target module
     * @param completedCodes modules already completed
     * @param exemptedModules modules exempted from prerequisites
     * @throws ModuleDeletionBlockedException if deletion breaks future prerequisites
     */
    public static void validateFutureDependencies(String moduleCodeToDelete, List<Module> futureModules,
                                                  List<String> completedCodes, List<String> exemptedModules)
            throws ModuleDeletionBlockedException {
        for (Module futureModule : futureModules) {
            try {
                PrerequisiteUtil.validatePrerequisites(
                        futureModule.getCode(),
                        futureModule.getPrerequisites(),
                        completedCodes,
                        exemptedModules
                );
            } catch (ModuleAdditionBlockedException e) {
                throw new ModuleDeletionBlockedException(moduleCodeToDelete, futureModule.getCode());
            }
        }
    }

    /**
     * Checks if a module is exempted from prerequisites.
     *
     * @param moduleCode the module code
     * @param exemptedModules list of exempted module codes
     * @return true if the module is exempted
     */
    public static boolean isExemptedModule(String moduleCode, List<String> exemptedModules) {
        return exemptedModules.contains(moduleCode);
    }
}
