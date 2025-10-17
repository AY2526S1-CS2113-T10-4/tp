package modhero.NUSmodsAPI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

import modhero.data.modules.Module;
import modhero.data.modules.PreReqTree;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NusModsModuleResponse {
    public String acadYear;
    public String title;
    public String moduleCode;
    public int moduleCredit;
    public PreReqTree preReqTree;

    public NusModsModuleResponse( ){
        acadYear = new String();
        title = new String();
        moduleCode = new String();
        preReqTree = new PreReqTree();
    }

    public void printMod(){
        System.out.println("Response:\n" + acadYear+ "\n" + title + "\n" + moduleCode + "\n" + preReqTree.toString());
    }

    protected Module responseToModule(){
     Module module = new Module(moduleCode, title, moduleCredit,"elective", preReqTree);
     return module;
    }
}

