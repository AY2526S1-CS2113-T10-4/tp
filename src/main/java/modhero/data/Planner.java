package modhero.data;

import static modhero.common.Constants.NUM_TERMS;
import static modhero.common.Constants.NUM_YEARS;

import modhero.data.modules.Module;
import modhero.data.modules.ModuleList;

import java.util.ArrayList;
import java.util.List;

/**
 * Algorithm to plan the timetable
 */
public class Planner {
    private final Timetable timetable;
    private final ModuleList moduleList;

    public Planner(Timetable timetable, ModuleList coreList, ModuleList electiveList) {
        this.timetable = timetable;
        this.moduleList = coreList.merge(electiveList);
    }

    /**
     * Plan the Timetable given the moduleList
     */
    public void planTimeTable() {
        moduleList.sort();
        addToTimetable();
    }

    /**
     * Generates the recommended timetable based on the ModuleLists
     * TODO: Needs to be adapted to use the prequisite tree, this is where our recommendation algo comes from
     */
    public void addToTimetable() {
        for (int i = 0; i < moduleList.size(); i++) {
            int year = (i / NUM_TERMS) % NUM_YEARS;
            int term = i % NUM_TERMS;
            Module module = moduleList.getModule(i);
            timetable.addModule(year, term, module);
        }
    }
}
