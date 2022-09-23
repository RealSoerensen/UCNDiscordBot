package UCNDiscordBot.APIS.APICalls;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import UCNDiscordBot.APIS.GetAPIKey;

public class JavaCompiler {
    private static String rapidAPIKey = GetAPIKey.getRapidAPIKey();

    private static String post(String code) {
        // remove whitespace from string
        byte ptext[] = code.getBytes();
        try {
            code = new String(ptext, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            return "";
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

            if ((boolean) json.get("success") == true) {
                output = json.get("output").toString();
            } else {
                output = "Error: " + json.get("error");
            }
            return output;

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String getCompileResult(String code) {
        return post(code);
    }

    public static String formater(String code) {
        StringBuilder reformatCode = new StringBuilder();
        try {
            // split code into lines after each ";"
            String[] lines = code.split("(?<=\\{)|((?<=\\})|(?=\\}))");
            int indention = 0;

            for (String line : lines) {
                // if line is empty, skip
                if (line.equals(" ")) {
                    continue;
                }

                // Split code into new line after each ";"
                if (line.contains(";")) {
                    String[] split = line.split("(?<=;)");
                    for (String s : split) {
                        // if line is empty, skip
                        if (s.equals(" ")) {
                            continue;
                        }

                        else {
                            s = s.trim();
                            reformatCode.append("\t".repeat(indention) + s + "\n");
                        }
                    }
                }

                // if the line ends with "{" append to string and then increase indention
                else if (line.endsWith("{")) {
                    reformatCode.append(("\t").repeat(indention) + line + "\n");
                    indention++;
                }

                // if the line ends with "}" decrease indention and then append to string
                else if (line.endsWith("}")) {
                    indention--;
                    reformatCode.append(("\t").repeat(indention) + line + "\n");
                }

                // else just append the line to the string
                else {
                    reformatCode.append(("\t").repeat(indention) + line + "\n");
                }
            }
        }

        catch (Exception e) {
            return "Error: " + e.getMessage();
        }

        return reformatCode.toString();
    }
}
