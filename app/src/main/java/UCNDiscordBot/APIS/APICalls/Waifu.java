package UCNDiscordBot.APIS.APICalls;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Waifu {

    public static String getWaifu(boolean dirty) {
        String web;
        if (dirty) {
            web = "https://api.waifu.pics/nsfw/waifu";
        } else {
            web = "https://api.waifu.pics/sfw/waifu";
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(web))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response.body());
            String data = (String) json.get("url");

            System.out.println(response.body());
            return data;
            // return response.body().split("\"")[1];
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}