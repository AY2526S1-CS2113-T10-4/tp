package modhero.NUSmodsAPI;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ModuleInformationFetcher {
    private URL url;
    private ObjectMapper mapper = new ObjectMapper();

    public ModuleInformationFetcher() {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(Duration.ofSeconds(20))
                    .build();
            HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.nusmods.com/v2/2025-2026/modules/CS1010.json"))
                    .header("accept", "application/json").build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
           // System.out.println(response.statusCode());
            //System.out.println(response.body());
            NusModsModuleResponse apiResponse = mapper.readValue(response.body(), NusModsModuleResponse.class);
            apiResponse.printMod();

        }catch (MalformedURLException e){
            System.out.println("Check the url format");
        }catch (JacksonException exception){
            System.out.println(exception.getOriginalMessage());
        } catch (IOException e){
            System.out.println("Connection error");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Module fetchByCode(){
        return null;
    }
}

