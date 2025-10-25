package modhero.data.major;

import modhero.data.modules.Module;
import modhero.data.modules.ModuleList;

import java.util.*;

/**
 * Stores predefined core modules and schedule mappings
 * for supported majors (Computer Science & Computer Engineering).
 */
public class MajorData {

    /**
     * Returns the list of core modules for a given major.
     *
     * @param major the major name (case-insensitive)
     * @return ModuleList of core modules
     */
    public ModuleList getCoreModules(String major) {
        major = major.trim().toLowerCase();
        ModuleList moduleList = new ModuleList();

        if (major.equals("computer science")) {
            // === Computer Science Core Mods ===
            moduleList.add(new Module("CS1010S", "Programming Methodology", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CS1231S", "Discrete Structures", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CS2030S", "Programming Methodology II", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CS2040S", "Data Structures and Algorithms", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CS2101", "Effective Communication for Computing Professionals", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CS2100", "Computer Organisation", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CS2103T", "Software Engineering", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CS2106", "Introduction to Operating Systems", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CS2109S", "Introduction to AI and Machine Learning", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CS3230", "Design and Analysis of Algorithms", 4, "core", new ArrayList<>()));
            return moduleList;
        }

        if (major.equals("computer engineering")) {
            // === Computer Engineering Core Mods ===
            moduleList.add(new Module("CS1010", "Programming Methodology", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CS1231", "Discrete Structures", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CS2040C", "Data Structures and Algorithms", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CS2113", "Software Engineering & OOP", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("EE2026", "Digital Design", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("EE4204", "Computer Networks", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CG1111A", "Engineering Principles and Practice I", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CG2111A", "Engineering Principles and Practice II", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CG2023", "Signals and Systems", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CG2027", "Transistor-level Digital Circuits", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CG2028", "Computer Organisation", 4, "core", new ArrayList<>()));
            moduleList.add(new Module("CG2271", "Real-time Operating Systems", 4, "core", new ArrayList<>()));
            return moduleList;
        }

        return null;
    }

    /**
     * Returns a schedule mapping (moduleCode → [year, sem]) for each major.
     */
    public Map<String, int[]> getSchedule(String major) {
        major = major.trim().toLowerCase();

        if (major.equals("computer science")) {
            Map<String, int[]> csSchedule = new HashMap<>();
            csSchedule.put("CS1010S", new int[]{1, 1});
            csSchedule.put("CS1231S", new int[]{1, 1});
            csSchedule.put("CS2030S", new int[]{1, 2});
            csSchedule.put("CS2040S", new int[]{1, 2});
            csSchedule.put("CS2101", new int[]{1, 2});
            csSchedule.put("CS2100", new int[]{2, 1});
            csSchedule.put("CS2103T", new int[]{2, 1});
            csSchedule.put("CS2106", new int[]{2, 1});
            csSchedule.put("CS2109S", new int[]{2, 2});
            csSchedule.put("CS3230", new int[]{3, 1});
            return csSchedule;
        }

        if (major.equals("computer engineering")) {
            Map<String, int[]> cegSchedule = new HashMap<>();
            cegSchedule.put("CG1111A", new int[]{1, 1});
            cegSchedule.put("CS1010", new int[]{1, 1});
            cegSchedule.put("CS1231", new int[]{1, 1});
            cegSchedule.put("CG2111A", new int[]{1, 2});
            cegSchedule.put("CS2040C", new int[]{1, 2});
            cegSchedule.put("CG2023", new int[]{1, 2});
            cegSchedule.put("CS2113", new int[]{2, 1});
            cegSchedule.put("EE2026", new int[]{2, 1});
            cegSchedule.put("CG2027", new int[]{2, 2});
            cegSchedule.put("CG2028", new int[]{2, 2});
            cegSchedule.put("CG2271", new int[]{2, 2});
            cegSchedule.put("EE4204", new int[]{3, 1});
            return cegSchedule;
        }

        return new HashMap<>();
    }
}
