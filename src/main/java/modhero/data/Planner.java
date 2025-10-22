package modhero.data;

import modhero.data.modules.Module;
import modhero.data.modules.ModuleList;

import java.util.ArrayList;
import java.util.List;

/**
 * Algorithm to plan the timetable
 */
public class Planner {
    private final Timetable timetable;
    private final List<Module> moduleList;

    final int years = 4;
    final int terms = 2;

    public Planner(Timetable timetable, ModuleList coreList, ModuleList electiveList) {
        this.timetable = timetable;
        moduleList = new ArrayList<>(coreList.getList());
        moduleList.addAll(electiveList.getList());
    }

    public void planTimeTable() {
        sortModuleList();
        addToTimetable();
    }

    public void sortModuleList() {
        moduleList.sort(Module.ModuleCodeComparator);
    }

    public void addToTimetable() {
        for (int i = 0; i < moduleList.size(); i++) {
            int year = (i / terms) % years;
            int term = i % terms;
            Module module = moduleList.get(i);
            timetable.addModule(year, term, module);
        }
    }
}
