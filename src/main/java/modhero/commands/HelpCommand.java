package modhero.commands;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Shows help instructions.
 */
public class HelpCommand extends Command {
    public static final Logger logger = Logger.getLogger(HelpCommand.class.getName());

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exits the program.\n"
            + "  Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {

        String helpMessage = """
            ┌────────────────────────────────────────────────────────────────┐
            │                          ModHero Help                          │
            ├────────────┬───────────────────────────────────────────────────┤
            │ Command    │ Description                                       │
            ├────────────┼───────────────────────────────────────────────────┤
            │ help       │ Show this help message                            │
            │            │ Format: help                                      │
            │            │ Example: help                                     │
            ├────────────┼───────────────────────────────────────────────────┤
            │ major      │ Specify your major                                │
            │            │ Format: major MAJOR_NAME                          │
            │            │ Example: major Computer Science                   │
            ├────────────┼───────────────────────────────────────────────────┤
            │ add        │ Add elective modules to specific Year & Semester  │
            │            │ Format: add MODULE_CODE to Y_S_                   │
            │            │ Example: add CS3240 to Y2S2                       │
            ├────────────┼───────────────────────────────────────────────────┤
            │ delete     │ Remove elective modules                           │
            │            │ Format: delete MODULE_CODE                        │
            │            │ Example: delete CS2109S                           │
            ├────────────┼───────────────────────────────────────────────────┤
            │ schedule   │ Generate your recommended 4-Year Study Plan       │
            │            │ Format: schedule                                  │
            │            │ Example: schedule                                 │
            ├────────────┼───────────────────────────────────────────────────┤
            │ clear      │ Clear all modules in the plan                     │
            │            │ Format: clear                                     │
            │            │ Example: clear                                    │
            ├────────────┼───────────────────────────────────────────────────┤
            │ exit       │ Exit the program                                  │
            │            │ Format: exit                                      │
            │            │ Example: exit                                     │
            └────────────┴───────────────────────────────────────────────────┘
            """;

        return new CommandResult(helpMessage);
    }
}
