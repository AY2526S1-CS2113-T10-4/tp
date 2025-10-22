package modhero.ui;

import java.util.Scanner;

import modhero.commands.CommandResult;
import modhero.data.modules.Module;

/**
 * Handles all interactions with the user, including displaying messages
 * and reading user input.
 */
public class Ui {
    private Scanner scanner;

    /**
     * Creates a new {@code Ui} with a scanner for reading user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message when the program starts.
     */
    public void showWelcome() {
        System.out.println("""
                ┌───────────────────────────────────────────────────────────────────────────────┐
                │                               Welcome to ModHero                              │
                ├───────────────────────────────────────────────────────────────────────────────┤
                │ Hello there!                                                                  │
                │                                                                               │
                │ ModHero helps you plan your degree efficiently through fast, structured       │
                │ commands and a clear, consolidated overview.                                  │
                │                                                                               │
                │ Why ModHero?                                                                  │
                │ • Because planning your modules should feel logical, not messy.               │
                │ • Because good decisions come from seeing the whole picture clearly.          │
                │                                                                               │
                │ Let's get you started:                                                        │
                │  1. Specify your major so ModHero can load core modules.                      │
                │     Example: major Computer Engineering                                       │
                │                                                                               │
                │  2. Add electives you wish to include.                                        │
                │     Example: elective CS2109S CS3230                                          │
                │                                                                               │
                │  3. Generate a recommended schedule.                                          │
                │     Example: schedule                                                         │
                │                                                                               │
                │ Tip: Type 'help' anytime to see all available commands.                       │
                │                                                                               │
                │ Ready? Let's begin!!                                                          │
                └───────────────────────────────────────────────────────────────────────────────┘
                """);
    }

    /**
     * Displays the goodbye message when the program exits.
     */
    public void showBye() {
        System.out.println("Bye");
    }

    /**
     * Reads a command entered by the user from the console.
     *
     * @return The trimmed user input string.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Displays the result of a command execution to the user.
     *
     * @param result The {@code CommandResult} containing feedback and relevant module information.
     */
    public void showResultToUser(CommandResult result) {
        assert result != null : "CommandResult must not be null";

        System.out.println(result.getFeedbackToUser());

        Module module = result.getrelevantModule();
        if (module != null) {
            System.out.println("  " + module);
            if (result.getTotalModules() > 0) {
                System.out.println("Now you have " + result.getTotalModules() + " modules in the list.");
            }
        }
    }

    /**
     * Closes the scanner used for reading user input.
     */
    public void close() {
        scanner.close();
    }
}
