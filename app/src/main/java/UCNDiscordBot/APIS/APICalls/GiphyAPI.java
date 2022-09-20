package UCNDiscordBot.APIS.APICalls;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import UCNDiscordBot.APIS.GetAPIKey;

public class GiphyAPI {
    private static String giphyKey = new GetAPIKey().getGiphyKey();

    // Find gif from giphy using search term
    public static String getGif(String searchTerm) {
        // Make restful call to giphy api
        searchTerm = searchTerm.replaceAll(" ", "+");
        String url = "http://api.giphy.com/v1/gifs/search?api_key=" + giphyKey + "&q=" + searchTerm
                + "&limit=15";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        } catch (InterruptedException e) {
            return "Error: " + e.getMessage();
        }

        // Parse response
        JSONParser parser = new JSONParser();
        JSONObject json;
        try {
            json = (JSONObject) parser.parse(response.body());
        } catch (ParseException e) {
            return "Error: " + e.getMessage();
        }
        JSONArray data = (JSONArray) json.get("data");
        Random random = new Random();
        JSONObject gif = (JSONObject) data.get(random.nextInt(data.size()));
        JSONObject images = (JSONObject) gif.get("images");
        JSONObject original = (JSONObject) images.get("original");
        String gifUrl = (String) original.get("url");
        return gifUrl;
    }

    // Find random gif from giphy
    public static String getRandomGif() {
        // Make restful call to giphy api
        String url = "http://api.giphy.com/v1/gifs/random?api_key=" + giphyKey;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        } catch (InterruptedException e) {
            return "Error: " + e.getMessage();
        }

        // Parse response
        JSONParser parser = new JSONParser();
        JSONObject json;
        try {
            json = (JSONObject) parser.parse(response.body());
        } catch (ParseException e) {
            return "Error: " + e.getMessage();
        }
        JSONObject data = (JSONObject) json.get("data");
        JSONObject images = (JSONObject) data.get("images");
        JSONObject original = (JSONObject) images.get("original");
        return (String) original.get("url");
    }
}