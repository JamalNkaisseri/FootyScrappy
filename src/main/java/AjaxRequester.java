import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AjaxRequester {

    // Method to format strings for URL
    public String formatStringForUrl(String input) {
        return input.trim().toLowerCase().replace(" ", "-");
    }

    public Map<String, String> makeAjaxRequest(HttpClient httpClient, String seasonYear, String countryName, String playerName) {
        Map<String, String> stats = new HashMap<>();
        try {
            URI uri = URI.create("https://footystats.org/ajax_player_neo.php");

            // Create the form data
            String formData = createFormData(seasonYear, countryName, playerName);


            // Create the request
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(formData))
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .build();

            // Send the request
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check status code
            if (response.statusCode() == 200) {
                String jsonResponse = response.body();


                // Parse the JSON response and fill the stats map
                stats = PlayerStatsParser.parsePlayerStats(jsonResponse);
            } else {
                System.out.println("Failed to fetch data. Status Code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }

    private String createFormData(String year, String country, String player) {
        // Use URLEncoder to encode form data
        return "z=" + URLEncoder.encode(year, StandardCharsets.UTF_8)
                + "&zz=" + URLEncoder.encode(country, StandardCharsets.UTF_8)
                + "&zzz=" + URLEncoder.encode(player, StandardCharsets.UTF_8);
    }
}
