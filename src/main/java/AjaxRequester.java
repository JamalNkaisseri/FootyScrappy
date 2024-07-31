import org.htmlunit.HttpMethod;
import org.htmlunit.WebClient;
import org.htmlunit.WebRequest;
import org.htmlunit.WebResponse;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AjaxRequester {

    public Map<String, String> makeAjaxRequest(WebClient webClient, String seasonYear) {
        Map<String, String> stats = new HashMap<>();
        try {
            URL url = new URL("https://footystats.org/ajax_player_neo.php");

            // Create the request
            WebRequest request = new WebRequest(url, HttpMethod.POST);

            // Set the required headers
            request.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            request.setAdditionalHeader("Cookie", "your-cookie-here");
            request.setAdditionalHeader("Referer", "https://footystats.org/players/belgium/leandro-trossard");
            request.setAdditionalHeader("User-Agent", "your-user-agent-here");
            request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

            // Set the request body using the provided seasonYear
            String formData = createFormData(seasonYear, "england", "bukayo-saka");
            request.setRequestBody(formData);

            // Send the request
            WebResponse response = webClient.loadWebResponse(request);

            // Check status code
            if (response.getStatusCode() == 200) {
                String jsonResponse = response.getContentAsString();

                // Parse the JSON response and fill the stats map
                stats = PlayerStatsParser.parsePlayerStats(jsonResponse);
            } else {
                System.out.println("Failed to fetch data. Status Code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }

    private String createFormData(String year, String country, String playerName) {
        return "z=" + year + "&zz=" + country + "&zzz=" + playerName;
    }
}

