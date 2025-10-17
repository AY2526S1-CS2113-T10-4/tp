package modhero.NUSmodsAPI;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import modhero.data.modules.Module;


import com.fasterxml.jackson.databind.ObjectMapper;

public class ModuleInformationFetcher {
    private ObjectMapper mapper = new ObjectMapper();
    private final String BASE_API ="https://api.nusmods.com/v2/";
    private String year = "2025-2026";
    private HttpClient client;

    public ModuleInformationFetcher() {
        this.client = HttpClient.newBuilder()
               .version(HttpClient.Version.HTTP_1_1)
               .followRedirects(HttpClient.Redirect.NORMAL)
               .connectTimeout(Duration.ofSeconds(20))
               .build();
    }

    public Module fetchModuleByCode(String input){
        String code = input.toUpperCase();
        String apiQuery = BASE_API + "/" + year + "/modules/" + code + ".json";
        HttpRequest request = HttpRequest.newBuilder(URI.create(apiQuery))
                .header("accept", "application/json").build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            NusModsModuleResponse apiResponse = mapper.readValue(response.body(), NusModsModuleResponse.class);
            Module module = apiResponse.responseToModule();
            return module;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

