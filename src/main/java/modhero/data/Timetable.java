package modhero.data;

import modhero.data.modules.Module;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a multi-year academic timetable, storing modules organized
 * by year and semester.
 */
public class Timetable {
    private final int COL_WIDTH = 20; // column width for nice alignment
    private List<List<List<Module>>> timetable;

    /**
     * Creates a timetable structure for the specified number of years and semesters.
     *
     * @param numYears number of academic years
     * @param numSemestersPerYear number of semesters per year
     */
    public Timetable(int numYears, int numSemestersPerYear) {
        timetable = new ArrayList<>();

        for (int year = 0; year < numYears; year++) {
            List<List<Module>> yearSemesters = new ArrayList<>();
            for (int sem = 0; sem < numSemestersPerYear; sem++) {
                yearSemesters.add(new ArrayList<>()); // each semester starts empty
            }
            timetable.add(yearSemesters);
        }
    }

    /** Adds a module to a specific year and semester. */
    public void addModule(int year, int sem, Module module) {
        timetable.get(year).get(sem).add(module);
    }

    /** Removes a module by code from a specific year/semester. */
    public boolean removeModule(int year, int sem, String moduleCode) {
        return timetable.get(year).get(sem)
                .removeIf(m -> m.getCode().equals(moduleCode));
    }

    /** Gets all modules for a specific year/semester. */
    public List<Module> getModules(int year, int sem) {
        return timetable.get(year).get(sem);
    }

    /** Gets all modules across all years. */
    public List<Module> getAllModules() {
        List<Module> all = new ArrayList<>();
        for (List<List<Module>> year : timetable) {
            for (List<Module> sem : year) {
                all.addAll(sem);
            }
        }
        return all;
    }

    /** Clears all modules. */
    public void clearTimetable() {
        for (List<List<Module>> year : timetable) {
            for (List<Module> sem : year) {
                sem.clear();
            }
        }
    }

    /** Prints a neat rectangular timetable with 2 semesters per year. */
    public void printTimetable() {
        for (int year = 0; year < timetable.size(); year++) {
            List<List<Module>> yearSems = timetable.get(year);
            int cols = yearSems.size(); // should be 2
            String border = makeBorder(cols, COL_WIDTH);

            System.out.println(border);
            int totalWidth = (COL_WIDTH * cols) + (cols + 1);
            String title = "YEAR " + (year + 1);
            int padLeft = Math.max(0, (totalWidth - title.length()) / 2);
            System.out.println(" ".repeat(padLeft) + title);
            System.out.println(border);

            // Header row: Semester 1 | Semester 2
            System.out.print("|");
            for (int sem = 0; sem < cols; sem++) {
                String header = "Semester " + (sem + 1);
                System.out.print(padCell(center(header, COL_WIDTH), COL_WIDTH) + "|");
            }
            System.out.println();
            System.out.println(border);

            // Find max modules in any semester
            int maxRows = 0;
            for (List<Module> sem : yearSems) {
                maxRows = Math.max(maxRows, sem.size());
            }

            // Print each row of module codes
            for (int r = 0; r < maxRows; r++) {
                System.out.print("|");
                for (int sem = 0; sem < cols; sem++) {
                    List<Module> semMods = yearSems.get(sem);
                    String text = (r < semMods.size()) ? semMods.get(r).getCode() : "";
                    System.out.print(padCell(text, COL_WIDTH) + "|");
                }
                System.out.println();
            }

            System.out.println(border);
            System.out.println();
        }
    }

    // ---------- Helper methods ----------

    private String makeBorder(int cols, int cellW) {
        StringBuilder sb = new StringBuilder();
        sb.append("+");
        for (int i = 0; i < cols; i++) sb.append("-".repeat(cellW)).append("+");
        return sb.toString();
    }

    private String padCell(String s, int width) {
        if (s.length() >= width) return s.substring(0, width);
        return s + " ".repeat(width - s.length());
    }

    private String center(String s, int width) {
        if (s.length() >= width) return s.substring(0, width);
        int left = (width - s.length()) / 2;
        int right = width - s.length() - left;
        return " ".repeat(left) + s + " ".repeat(right);
    }
}
