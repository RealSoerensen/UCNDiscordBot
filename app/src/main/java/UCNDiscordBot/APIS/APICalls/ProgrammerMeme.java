package UCNDiscordBot.APIS.APICalls;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import UCNDiscordBot.APIS.GetAPIKey;

public class ProgrammerMeme {
    public static String getMeme() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://programming-memes-images.p.rapidapi.com/v1/memes"))
                .header("X-RapidAPI-Key", GetAPIKey.getRapidAPIKey())
                .header("X-RapidAPI-Host", "programming-memes-images.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        } catch (InterruptedException e) {
            return "Error: " + e.getMessage();
        }

        JSONParser parser = new JSONParser();
        JSONArray json;
        try {
            json = (JSONArray) parser.parse(response.body());
        } catch (ParseException e) {
            return "Error: " + e.getMessage();
        }
        JSONObject data = (JSONObject) json.get(0);
        return (String) data.get("image");
    }

}