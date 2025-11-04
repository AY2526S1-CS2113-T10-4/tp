package modhero.common;

import java.util.List;

/**
 * Holds all global constants used in ModHero.
 */
public final class Constants {
    private Constants() {} // Prevent instantiation

    /** UI-related text constants. */
    public static final class UiConstants {
        /** Welcome message shown at program start. */
        public static final String WELCOME = """
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
                │  1. Specify your major so ModHero can load core modules                       │
                │     (psst! Please only enter CS or CEG)                                       │
                │     Example: major CEG                                                        │
                │                                                                               │
                │  2. See your recommended schedule!                                            │
                │     Example: schedule                                                         │
                │                                                                               │
                │  3. Add electives you wish to include to the specific Year & Semester         │
                │     Example: add CS3240 to Y3S2                                               │
                │                                                                               │
                │ Tip: Type 'help' anytime to see all available commands.                       │
                │                                                                               │
                │ Ready? Let's begin!!                                                          │
                └───────────────────────────────────────────────────────────────────────────────┘
                """;
    }

    /** Message templates used throughout the application. */
    public static final class MessageConstants {
        /** Template for invalid command messages. */
        public static final String INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";

        /** Index out-of-bounds warning. */
        public static final String ARRAY_INDEX_OUT_BOUND = "(Your index is out of bound)";

        /** Help message displaying all available commands. */
        public static final String HELP = """
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
            │            │ Example: major cs                                 │
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
    }

    /** Academic configuration constants. */
    public static final class AcademicConstants {
        public static final int NUM_YEARS = 4;
        public static final int NUM_TERMS = 2;
        public static final String ACAD_YEAR = "2025-2026";
        public static final int MAX_MODULES_PER_SEM = 5;
    }

    /** File paths used for saving and loading data. */
    public static final class FilePathConstants {
        public static final String MODULES_FILE_PATH = "data/modules.txt";
        public static final String MAJOR_FILE_PATH = "data/major.txt";
        public static final String TIMETABLE_FILE_PATH = "data/save.txt";
    }

    /** Delimiters used in serialization. */
    public static final class FormatConstants {
        public static final String START_DELIMITER = "#";
        public static final String END_DELIMITER = "|";
    }

    /** Lists of modules exempted from prerequisites. */
    public static final class ExemptedModulesConstants {
        public static final List<String> CS_EXEMPTED_MODULES = List.of("MA1301", "PC1201") ;
        public static final List<String> CEG_EXEMPTED_MODULES = List.of("MA1301", "PC1201") ;
    }
}
