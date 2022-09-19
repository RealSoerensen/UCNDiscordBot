package UCNDiscordBot.APIS;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RandomWord {

    public static String getRandomWord() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://random-word-api.herokuapp.com/word"))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());

            return response.body().split("\"")[1];
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
