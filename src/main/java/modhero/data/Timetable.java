package modhero.data;

import static modhero.common.Constants.NUM_TERMS;
import static modhero.common.Constants.NUM_YEARS;

import modhero.data.modules.Module;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a multi-year academic timetable, storing modules organized
 * by year and term.
 */
public class Timetable {
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
    }

    /**
     * Adds a module to a specific year and term.
     *
     * @param year the year index (0-based)
     * @param term the term index (0-based)
     * @param module the module to add
     */
    public void addModule(int year, int term, Module module) {
        timetable.get(year).get(term).add(module);
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
        return timetable.get(year).get(term)
                .removeIf(m -> m.getCode().equals(moduleCode));
    }

    /**
     * Retrieves the list of modules for a given year and term.
     *
     * @param year the year index (0-based)
     * @param term the term index (0-based)
     * @return list of modules in the specified term
     */
    public List<Module> getModules(int year, int term) {
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
        for (int year = 0; year < timetable.size(); year++) {
            System.out.println("=".repeat(COL_WIDTH * timetable.get(year).size() + timetable.get(year).size() + 1));
            String yearTitle = "YEAR " + (year + 1);
            int tableWidth = COL_WIDTH * timetable.get(year).size() + timetable.get(year).size() + 1;
            int padding = (tableWidth - yearTitle.length()) / 2;
            System.out.println(" ".repeat(Math.max(0, padding)) + yearTitle);
            System.out.println("=".repeat(COL_WIDTH * timetable.get(year).size() + timetable.get(year).size() + 1));

            List<List<Module>> yearTerms = timetable.get(year);

            // Header row
            System.out.print("|");
            for (int term = 0; term < yearTerms.size(); term++) {
                String header = "Term " + (term + 1);
                System.out.print(padCell(header, COL_WIDTH) + "|");
            }
            System.out.println();

            // Find max rows needed
            int maxModules = 0;
            for (List<Module> termModules : yearTerms) {
                maxModules = Math.max(maxModules, termModules.size());
            }

            // Print modules row by row
            for (int row = 0; row < maxModules; row++) {
                System.out.print("|");
                for (List<Module> termModules : yearTerms) {
                    String content = (row < termModules.size()) ? termModules.get(row).getCode() : "";
                    System.out.print(padCell(content, COL_WIDTH) + "|");
                }
                System.out.println();
            }

            System.out.println("=".repeat(COL_WIDTH * timetable.get(year).size() + timetable.get(year).size() + 1));
            System.out.println();
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
