package UCNDiscordBot.Functions;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import UCNDiscordBot.APIS.GetAPIKey;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ProgrammerMeme {

    public static void getMeme(MessageReceivedEvent event) {
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

            // System.out.println(response.body());
            // String[] dummy = response.body().split(":");
            // dummy = dummy[dummy.length - 1].split("\"");
            System.out.println(url);
            event.getChannel().sendMessage(url).queue();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
