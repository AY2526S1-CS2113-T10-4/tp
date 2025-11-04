package modhero.common.predata;

import java.util.*;

/**
 * Provides predefined core module schedules for supported majors.
 */
public class MajorSchedule {
    /**
     * Returns a mapping of module codes to their recommended year and semester.
     * Supports Computer Science and Computer Engineering majors.
     *
     * @param major the name of the major
     * @return a map of module codes to [year, semester]
     */
    public Map<String, int[]> getSchedule(String major) {
        major = major.trim().toLowerCase();

        if (major.equals("computer science")) {
            Map<String, int[]> csSchedule = new HashMap<>();
            csSchedule.put("CS1101S", new int[]{1, 1});
            csSchedule.put("CS1231S", new int[]{1, 1});
            csSchedule.put("MA1522", new int[]{1, 1});
            csSchedule.put("ES2660", new int[]{1, 1});
            csSchedule.put("CS2030S", new int[]{1, 2});
            csSchedule.put("CS2040S", new int[]{1, 2});
            csSchedule.put("MA1521", new int[]{1, 2});
            csSchedule.put("CS2100", new int[]{2, 1});
            csSchedule.put("CS2101", new int[]{2, 1});
            csSchedule.put("CS2103T", new int[]{2, 1});
            csSchedule.put("IS1108", new int[]{2, 1});
            csSchedule.put("CS2109S", new int[]{2, 1});
            csSchedule.put("CS2106", new int[]{2, 2});
            csSchedule.put("ST2334", new int[]{2, 2});
            csSchedule.put("CS3230", new int[]{2, 2});
            return csSchedule;
        }

        if (major.equals("computer engineering")) {
            Map<String, int[]> cegSchedule = new HashMap<>();

            // CEG Graduation List
            cegSchedule.put("CG1111A", new int[]{1, 1});
            cegSchedule.put("EG1311", new int[]{1, 1});
            cegSchedule.put("MA1511", new int[]{1, 1});
            cegSchedule.put("MA1512", new int[]{1, 1});
            cegSchedule.put("CS1010", new int[]{1, 1});

            cegSchedule.put("CG2111A", new int[]{1, 2});
            cegSchedule.put("DTK1234", new int[]{1, 2});
            cegSchedule.put("MA1508E", new int[]{1, 2});
            cegSchedule.put("CS1231", new int[]{1, 2});

            cegSchedule.put("EE2026", new int[]{2, 1});
            cegSchedule.put("CS2040C", new int[]{2, 1});
            cegSchedule.put("CS2107", new int[]{2, 1});

            cegSchedule.put("CG2023", new int[]{2, 2});
            cegSchedule.put("CS2113", new int[]{2, 2});
            cegSchedule.put("EE2211", new int[]{2, 2});
            cegSchedule.put("CG2271", new int[]{2, 2});

            cegSchedule.put("CG2027", new int[]{3, 2});
            cegSchedule.put("CG2028", new int[]{3, 2});
            cegSchedule.put("CG3201", new int[]{3, 2});
            cegSchedule.put("ST2334", new int[]{3, 2});

            cegSchedule.put("EE4204", new int[]{4, 1});
            cegSchedule.put("CG3207", new int[]{4, 1});

            return cegSchedule;
        }
        return new HashMap<>();
    }
}
