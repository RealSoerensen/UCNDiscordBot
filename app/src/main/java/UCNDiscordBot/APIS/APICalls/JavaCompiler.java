package UCNDiscordBot.APIS.APICalls;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import UCNDiscordBot.APIS.GetAPIKey;

public class JavaCompiler {
    private static String rapidAPIKey = GetAPIKey.getRapidAPIKey();

    private static String post(String code) {
        byte ptext[] = code.getBytes();
        try {
            code = new String(ptext, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://codex7.p.rapidapi.com/"))
                    .header("content-type", "application/x-www-form-urlencoded")
                    .header("X-RapidAPI-Key", rapidAPIKey)
                    .header("X-RapidAPI-Host", "codex7.p.rapidapi.com")
                    .method("POST", HttpRequest.BodyPublishers.ofString(
                            "code=" + code + "&language=java"))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());

            JSONParser parser = new JSONParser();
            JSONObject json;
            String output = "";
            json = (JSONObject) parser.parse(response.body());
            System.out.println(parser.parse(response.body()));

            if ((boolean) json.get("success") == true) {
                output = json.get("output").toString();
            } else {
                output = "Error: " + json.get("error");
            }
            return output;

        } catch (IOException e) {
            return "Error: " + e.getMessage();
        } catch (InterruptedException e) {
            return "Error: " + e.getMessage();
        } catch (ParseException e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String compile(String code) {
        return post(code);
    }
}
