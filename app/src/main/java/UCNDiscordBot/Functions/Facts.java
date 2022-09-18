package UCNDiscordBot.Functions;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import UCNDiscordBot.APIS.GetAPIKey;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Facts {

    public static void getFact(MessageReceivedEvent event) {
        try {
            System.out.println("Trying to give you a fact");
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

            // System.out.println(response.body());
            // String[] dummy = response.body().split(":");
            // dummy = dummy[dummy.length - 1].split("\"");
            // System.out.println(data);
            event.getChannel().sendMessage(data).queue();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
