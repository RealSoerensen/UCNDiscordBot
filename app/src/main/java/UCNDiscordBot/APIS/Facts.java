package UCNDiscordBot.APIS;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Facts {

    public static String getFact() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://matchilling-chuck-norris-jokes-v1.p.rapidapi.com/jokes/random"))
                    .header("accept", "application/json")
                    .header("X-RapidAPI-Key", GetAPIKey.getRapidAPIKey())
                    .header("X-RapidAPI-Host", "matchilling-chuck-norris-jokes-v1.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response.body());
            String data = (String) json.get("value");

            return data;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
