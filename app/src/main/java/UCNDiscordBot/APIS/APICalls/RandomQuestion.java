package UCNDiscordBot.APIS.APICalls;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RandomQuestion {
    public static String[] getQuestion() {
        String[] outputArray = new String[6];
        // 0: category
        // 1: question
        // 2: correct_answer
        // 3,4,5 incorrect_answers

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://opentdb.com/api.php?amount=1&difficulty=easy&type=multiple"))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response.body());
            JSONArray result = (JSONArray) json.get("results");
            JSONObject output = (JSONObject) result.get(0);
            String[] what = output.get("incorrect_answers").toString().split("\"");

            outputArray[0] = output.get("category").toString();
            outputArray[1] = output.get("question").toString();
            outputArray[2] = output.get("correct_answer").toString();
            outputArray[3] = what[1];
            outputArray[4] = what[3];
            outputArray[5] = what[5];

            return outputArray;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
