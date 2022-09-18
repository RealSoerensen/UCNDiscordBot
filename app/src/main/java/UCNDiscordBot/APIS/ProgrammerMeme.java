package UCNDiscordBot.APIS;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ProgrammerMeme {
    public static String getMeme() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://programming-memes-images.p.rapidapi.com/v1/memes"))
                    .header("X-RapidAPI-Key", GetAPIKey.getRapidAPIKey())
                    .header("X-RapidAPI-Host", "programming-memes-images.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            // System.out.println(response.body());

            JSONParser parser = new JSONParser();
            JSONArray json = (JSONArray) parser.parse(response.body());
            JSONObject data = (JSONObject) json.get(0);
            String url = (String) data.get("image");

            return url;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
