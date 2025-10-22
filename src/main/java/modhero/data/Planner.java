package modhero.data;

import static modhero.common.Constants.NUM_TERMS;
import static modhero.common.Constants.NUM_YEARS;

import modhero.data.modules.Module;
import modhero.data.modules.ModuleList;
import modhero.parser.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Algorithm to plan the timetable
 */
public class Planner {
    public static final Logger logger = Logger.getLogger(Parser.class.getName());

    private final Timetable timetable;
    private final List<Module> moduleList;

    final int years = 4;
    final int terms = 2;

    public Planner(Timetable timetable, ModuleList coreList, ModuleList electiveList) {
        assert timetable != null : "Timetable must not be null";
        assert coreList != null : "Core list must not be null";
        assert electiveList != null : "Elective list must not be null";

        this.timetable = timetable;
        this.moduleList = new ArrayList<Module>();
        List <Module> coreAsArrayList = coreList.getList();
        List <Module> electivesAsArrayList = electiveList.getList();
        this.moduleList.addAll(coreAsArrayList);
        this.moduleList.addAll(electivesAsArrayList);
    }

    /**
     * Plan the Timetable given the moduleList
     */
    public void planTimeTable() {
        topologicallySortModuleList();
        for ( Module module: sortedModuleList){
            String moduleCode = module.getCode();
            System.out.println(moduleCode);
        }
        addToTimetable();
    }


    private void generatePrereqGraph(){
        prereqGraph = new PrereqGraph(moduleList);
    }

    private void trimPrereqGraph(HashMap<String, List<String>> graphToSort, ArrayList<String> entriesToRemove){

        for (String key: entriesToRemove){
            List<String>  preReqs = graphToSort.get(key);
            for ( String preReq: preReqs){
                if (entriesToRemove.contains(preReq)){
                    preReqs.remove(preReq);
                }
            }
            if (entriesToRemove.contains(key)){
                graphToSort.remove(key);
            }
        }
    }

    private Module findModuleByCode(String code){
        for (Module module : moduleList){
            if (module.getCode().equals(code)){
                return module;
            }
        }
        return null; //add proper exception handling here later
    }

    private void topologicallySortModuleList (){
        prereqGraph = new PrereqGraph(moduleList);
        sortedModuleList = new ArrayList<>();
        HashMap<String, List<String>> graphToSort = prereqGraph.getGraph();
        while (!graphToSort.isEmpty()){
            ArrayList<String> entriesToRemove = new ArrayList<>();
            for (String key : graphToSort.keySet()) {
                if ( graphToSort.get(key).isEmpty()){
                    Module currentModule = findModuleByCode(key);
                    sortedModuleList.add(currentModule);
                    entriesToRemove.add(key);
                }
            }
            trimPrereqGraph(graphToSort, entriesToRemove);
        }
    }

    public void addToTimetable() {
        for (int i = 0; i < sortedModuleList.size(); i++) {
            int year = (i / NUM_TERMS) % NUM_YEARS;
            int term = i % NUM_TERMS;
            Module module = moduleList.get(i);
            timetable.addModule(year, term, module);
        }
    }
}
