package modhero.data.timetable;

import static modhero.common.Constants.NUM_TERMS;
import static modhero.common.Constants.NUM_YEARS;

import modhero.data.modules.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a multi-year academic timetable, storing modules organized
 * by year and term.
 */
public class Timetable {
    public static final Logger logger = Logger.getLogger(Timetable.class.getName());

    private final int COL_WIDTH = 15; // standard column width for all cells
    private List<List<List<Module>>> timetable;

    /**
     * Creates a timetable structure for the specified number of years and terms.
     * Each year contains a list of terms, which in turn contain modules.
     *
     */
    public Timetable() {
        timetable = new ArrayList<>();

        // Initialize the timetable
        for (int year = 0; year < NUM_YEARS; year++) {
            List<List<Module>> yearSemesters = new ArrayList<>();
            for (int sem = 0; sem < NUM_TERMS; sem++) {
                yearSemesters.add(new ArrayList<>()); // each semester starts empty
            }
            timetable.add(yearSemesters);
        }

        logger.log(Level.FINE, () -> String.format("Timetable initialised for %d years and %d terms", NUM_YEARS, NUM_TERMS));
    }

    /**
     * Adds a module to a specific year and term.
     *
     * @param year the year index (0-based)
     * @param term the term index (0-based)
     * @param module the module to add
     */
    public void addModule(int year, int term, Module module) {
        assert year >= 0 && year < NUM_YEARS : "addModule year out of bounds";
        assert term >= 0 && term < NUM_TERMS : "addModule term out of bounds";
        assert module != null : "addModule module must not be null";

        timetable.get(year).get(term).add(module);
        logger.log(Level.FINEST, () -> String.format("Module %s added to year %d term %d", module.getCode(), year, term));
    }

    /**
     * Removes a module from a specific year and term by module code.
     *
     * @param year the year index (0-based)
     * @param term the term index (0-based)
     * @param moduleCode the code of the module to remove
     * @return {@code true} if a module was removed, {@code false} otherwise
     */
    public boolean removeModule(int year, int term, String moduleCode) {
        assert year >= 0 && year < NUM_YEARS : "removeModule year out of bounds";
        assert term >= 0 && term < NUM_TERMS : "removeModule term out of bounds";
        assert moduleCode != null : "removeModule moduleCode must not be null";

        boolean hasRemoved = timetable.get(year).get(term)
                .removeIf(m -> m.getCode().equals(moduleCode));

        if (hasRemoved) {
            logger.log(Level.FINEST, () -> String.format("Module %s removed from year %d term %d", moduleCode, year, term));
        }
        return hasRemoved;
    }

    /**
     * Retrieves the list of modules for a given year and term.
     *
     * @param year the year index (0-based)
     * @param term the term index (0-based)
     * @return list of modules in the specified term
     */
    public List<Module> getModules(int year, int term) {
        assert year >= 0 && year < NUM_YEARS : "getModules year out of bounds";
        assert term >= 0 && term < NUM_TERMS : "getModules term out of bounds";

        return timetable.get(year).get(term);
    }

    /**
     * Retrieves all modules across all years and terms in the timetable.
     *
     * @return a flat list of all modules
     */
    public List<Module> getAllModules() {
        List<Module> all = new ArrayList<>();
        for (List<List<Module>> year : timetable) {
            for (List<Module> term : year) {
                all.addAll(term);
            }
        }
        return all;
    }

    /**
     * Prints a formatted view of the timetable to the console,
     * organized by year and term in table format.
     */
    public void printTimetable() {
        int COL_WIDTH = 20;

        for (int year = 0; year < timetable.size(); year++) {
            System.out.println("+--------------------+--------------------+");
            String yearTitle = "YEAR " + (year + 1);
            System.out.printf("%22s\n", yearTitle);
            System.out.println("+--------------------+--------------------+");
            System.out.printf("|%-20s|%-20s|\n", "Semester 1", "Semester 2");
            System.out.println("+--------------------+--------------------+");

            List<List<Module>> semesters = timetable.get(year);
            int maxRows = Math.max(semesters.get(0).size(), semesters.get(1).size());

            for (int row = 0; row < maxRows; row++) {
                String s1 = row < semesters.get(0).size() ? semesters.get(0).get(row).getCode() : "";
                String s2 = row < semesters.get(1).size() ? semesters.get(1).get(row).getCode() : "";
                System.out.printf("|%-20s|%-20s|\n", s1, s2);
            }

            System.out.println("+--------------------+--------------------+\n");
        }
    }


    /**
     * Pads the given text with spaces or truncates it so that
     * it fits within a fixed-width table cell.
     *
     * @param text the text to pad
     * @param width the fixed width of the cell
     * @return the padded (or truncated) string
     */
    private String padCell(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width - 1) + " "; // truncate if too long
        }
        int spaces = width - text.length();
        return text + " ".repeat(spaces);
    }

    /**
     * Removes all modules from the entire timetable.
     */
    public void clearTimetable() {
        for (List<List<Module>> year : timetable) {
            for (List<Module> sem : year) {
                sem.clear();
            }
        }
    }
}