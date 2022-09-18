package UCNDiscordBot.APIS;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class GiphyAPI {
    private static String giphyKey = new GetAPIKey().getGiphyKey();

    // Find gif from giphy using search term
    public static String getGif(String searchTerm) throws ParseException, IOException, InterruptedException {
        // Make restful call to giphy api
        searchTerm = searchTerm.replaceAll(" ", "+");
        String url = "http://api.giphy.com/v1/gifs/search?api_key=" + giphyKey + "&q=" + searchTerm
                + "&limit=15";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        // Parse response
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(response.body());
        JSONArray data = (JSONArray) json.get("data");
        Random random = new Random();
        JSONObject gif = (JSONObject) data.get(random.nextInt(data.size()));
        JSONObject images = (JSONObject) gif.get("images");
        JSONObject original = (JSONObject) images.get("original");
        String gifUrl = (String) original.get("url");
        return gifUrl;
    }

    // Find random gif from giphy
    public static Object getRandomGif() throws InterruptedException, IOException, ParseException {
        // Make restful call to giphy api
        String url = "http://api.giphy.com/v1/gifs/random?api_key=" + giphyKey;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        // Parse response
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(response.body());
        JSONObject data = (JSONObject) json.get("data");
        JSONObject images = (JSONObject) data.get("images");
        JSONObject original = (JSONObject) images.get("original");
        String gif = (String) original.get("url");
        return gif;
    }

    public String gifOutput(SlashCommandInteractionEvent event) {
        // Init variables
        String gif = "";

        // If the message contains no arguments
        if (event.getOption("search") == null) {
            // Get a random gif
            try {
                gif = (String) GiphyAPI.getRandomGif();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // If the message contains arguments
        else {
            String args = event.getOption("search").getAsString();
            // Try and get gif from GiphyAPI
            try {
                gif = (String) GiphyAPI.getGif(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return gif;
    }
}
