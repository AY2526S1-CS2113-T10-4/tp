package modhero.NUSmodsAPI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NusModsModuleResponse {
    public String acadYear;
    public String title;
    public String moduleCode;
    public ArrayList<String> fulfillRequirements;

    public NusModsModuleResponse( ){
        acadYear = new String();
        title = new String();
        moduleCode = new String();
        fulfillRequirements = new ArrayList<String>();
    }
    public NusModsModuleResponse( String acadYear, String title, String moduleCode, ArrayList<String> fulfillRequirements){
        this.acadYear = acadYear;
        this.title = title;
        this.moduleCode = moduleCode;
        this.fulfillRequirements = fulfillRequirements;
    }

    public void printMod(){
        System.out.println("Response:\n" + acadYear+ "\n" + title + "\n" + moduleCode + "\n" + fulfillRequirements.toString());
    }
}

