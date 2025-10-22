package modhero.commands;

/**
 * Shows help instructions.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows all available commands.\n"
            + "  Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {

        String helpMessage = """
            ┌────────────────────────────────────────────────────────────┐
            │                        ModHero Help                        │
            ├────────────┬───────────────────────────────────────────────┤
            │ Command    │ Description                                   │
            ├────────────┼───────────────────────────────────────────────┤
            │ help       │ Show this help message                        │
            │            │ Format: help                                  │
            │            │ Example: help                                 │
            ├────────────┼───────────────────────────────────────────────┤
            │ major      │ Specify your major                            │
            │            │ Format: major MAJOR_NAME [specialisation ...] │
            │            │ Example: major Computer Science ...           │
            ├────────────┼───────────────────────────────────────────────┤
            │ elective   │ Add elective modules                          │
            │            │ Format: elective MODULE_CODE...               │
            │            │ Example: elective CS2109S CS3230              │
            ├────────────┼───────────────────────────────────────────────┤
            │ delete     │ Remove elective modules                       │
            │            │ Format: delete MODULE_CODE...                 │
            │            │ Example: delete CS2109S                       │
            ├────────────┼───────────────────────────────────────────────┤
            │ schedule   │ Generate a recommended Year 1 & 2 plan        │
            │            │ Format: schedule                              │
            │            │ Example: schedule                             │
            ├────────────┼───────────────────────────────────────────────┤
            │ clear      │ Clear all modules in the plan                 │
            │            │ Format: clear                                 │
            │            │ Example: clear                                │
            ├────────────┼───────────────────────────────────────────────┤
            │ exit       │ Exit the program                              │
            │            │ Format: exit                                  │
            │            │ Example: exit                                 │
            └────────────┴───────────────────────────────────────────────┘
            """;

        return new CommandResult(helpMessage);
    }
}
