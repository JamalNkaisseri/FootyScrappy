import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerStatsParser {

    public static Map<String, String> parsePlayerStats(String jsonResponse) throws IOException {
        Map<String, String> stats = new HashMap<>();

        // Parse JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);
        String htmlContent = rootNode.get("html2").asText();

        // Parse HTML using HtmlUnit
        try (WebClient webClient = new WebClient()) {
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);

            HtmlPage page = webClient.loadHtmlCodeIntoCurrentWindow(htmlContent);

            String[] rowNames = {"Matches Played", "Goals", "Assists", "YC", "RC", "Pens"};

            // Extract season
            HtmlElement seasonElement = page.getFirstByXPath("//h2[@class='section-title dBlock']");
            if (seasonElement != null) {
                stats.put("Season", seasonElement.getTextContent().trim());
            }

            // Extract average rating
            HtmlElement ratingElement = page.getFirstByXPath("//div[@class='form-box playerLarge good']");
            if (ratingElement != null) {
                stats.put("Average Rating", ratingElement.getTextContent().trim());
            }

            // Extract rank (if needed)
            HtmlElement rankElement = page.getFirstByXPath("//span[@class='semi-bold ml05e dark-gray fs08e lh16e w100 cf']");
            if (rankElement != null) {
                stats.put("Rank", rankElement.getTextContent().trim());
            }

            // Extract table stats
            List<HtmlElement> dataColumns = page.getByXPath("(//div[@class='col-lg-1'])[position() <= 6]");
            if (dataColumns.size() >= rowNames.length) {
                for (int i = 0; i < rowNames.length; i++) {
                    HtmlElement dataElement = dataColumns.get(i);
                    if (dataElement != null) {
                        stats.put(rowNames[i], dataElement.asNormalizedText());
                    }
                }
            } else {
                System.out.println("Warning: Fewer data columns than expected.");
            }

            // Print stats in the specified format
            System.out.println("Season: " + stats.getOrDefault("Season", "N/A"));
            System.out.println("Average Rating: " + stats.getOrDefault("Average Rating", "N/A"));
            System.out.println("Matches Played: " + stats.getOrDefault("Matches Played", "N/A"));
            System.out.println("Goals: " + stats.getOrDefault("Goals", "N/A"));
            System.out.println("Assists: " + stats.getOrDefault("Assists", "N/A"));
            System.out.println("Yellow Cards: " + stats.getOrDefault("YC", "N/A"));
            System.out.println("Red Cards: " + stats.getOrDefault("RC", "N/A"));
            System.out.println("Pens: " + stats.getOrDefault("Pens", "N/A"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stats;
    }

}
