package modhero.commands;

import modhero.data.modules.Module;
import modhero.data.modules.ModuleList;

import java.util.List;
import java.util.ArrayList;

/**
 * Adds an elective (or multiple) to your plan.
 */
public class ElectiveCommand extends Command {

    public static final String COMMAND_WORD = "elective";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adding an elective.\n"
            + "  Parameters: MODULE_CODES...\n"
            + "  Example: " + COMMAND_WORD + " CS2109S"
            + "  Example: " + COMMAND_WORD + " CS2109S CS3230 CS3219";

    private final List<String> electives;

    public ElectiveCommand(List<String> electives) {
        this.electives = electives;
    }


    @Override
    public CommandResult execute() {
        StringBuilder feedback = new StringBuilder("Electives added: ");
        for (String elective : electives) {
            Module module = allModulesData.get(elective);
            if (module != null) {
                electiveList.add(module);
                feedback.append(elective).append(" ");
            } else {
                feedback.append("\nElective ").append(elective).append(" not found in master list\n");
            }
        }
        feedback.append("\n");
        return new CommandResult(feedback.toString());
    }
}
