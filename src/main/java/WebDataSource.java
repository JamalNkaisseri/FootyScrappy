
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebDataSource implements DataSource {

    private static final String BASE_URL = "https://footystats.org/clubs/";
    private static final Map<String, String> TEAM_MAP = new HashMap<>();
    private static final WebClient CLIENT = createWebClient();
    private final FileStorage fileStorage;

    static {
        // Add full names and common abbreviations
        TEAM_MAP.put("arsenal", "arsenal fc");
        TEAM_MAP.put("chelsea", "chelsea fc");
        TEAM_MAP.put("manchester united", "manchester united fc");

        // Add the corresponding IDs
        TEAM_MAP.put("arsenal fc", "59");
        TEAM_MAP.put("chelsea fc", "60");
        TEAM_MAP.put("manchester united fc", "61");
        // Add more teams as needed
    }


    private static WebClient createWebClient() {
        WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setThrowExceptionOnScriptError(false);
        return client;
    }

    public WebDataSource(){
        this.fileStorage = new FileStorage();
    }

    public String fetchData(String teamName) {
        try {
            String teamUrl = constructTeamUrl(teamName);
            System.out.println("Attempting to fetch: " + teamUrl); // Debug print
            HtmlPage page = CLIENT.getPage(teamUrl);
            System.out.println("Page title: " + page.getTitleText()); // Debug print
            fetchTeamStats(page, teamName);
            return "Data fetched successfully for " + teamName;
        } catch (Exception e) {
            System.err.println("Error fetching data: " + e.getMessage());
            e.printStackTrace(); // Print full stack trace for debugging
            return null;
        }
    }


    private void fetchTeamStats(HtmlPage page, String teamName) {
        try {
            // Add the team name to the file storage
            fileStorage.addData("teamName", teamName);

            // Adjust the XPath to select the first row of stats containing seven data elements
            List<HtmlElement> statElements = page.getByXPath("(//div[contains(@class,'row club-data-table-row dFlex')])[1]//div[position() <= 7]");

            // Check if at least 7 stats are found
            if (statElements.size() >= 7) {
                // Array of stat names corresponding to the selected elements
                String[] statNames = {"Home Form", "Played", "W", "D", "L", "Goals For", "Goals Against"};

                // Loop through each stat name and its corresponding element
                for (int i = 0; i < statNames.length; i++) {
                    // Get the text content of the stat element, normalize it, and trim any extra spaces
                    String statValue = statElements.get(i).asNormalizedText().trim();

                    // Debug print to show the stat name and its value
                    System.out.println(statNames[i] + ": " + statValue);

                    // Add the stat name and value to the file storage
                    fileStorage.addData(statNames[i], statValue);
                }
            } else {
                // Print an error message if the expected number of stats are not found
                System.err.println("Found " + statElements.size() + " stats for " + teamName + ". Expected at least 7.");
            }
        } catch (Exception e) {
            // Print an error message and stack trace if an exception occurs
            System.err.println("Error fetching team stats: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private String constructTeamUrl(String teamName) {
        try {
            // Step 1: Trim the team name and convert to lowercase
            String normalizedTeamName = teamName.trim().toLowerCase();

            // Step 2: Handle common abbreviations
            if (TEAM_MAP.containsKey(normalizedTeamName)) {
                normalizedTeamName = TEAM_MAP.get(normalizedTeamName);
            }

            // Step 3: Get the identifier from the map
            String teamId = TEAM_MAP.get(normalizedTeamName);
            if (teamId == null) {
                throw new IllegalArgumentException("Unknown team name: " + teamName);
            }

            // Step 4: Replace spaces with hyphens
            String urlFriendlyName = normalizedTeamName.replaceAll("\\s+", "-");

            // Step 5: Construct the URL
            String constructedUrl = BASE_URL + urlFriendlyName + "-" + teamId;
            System.out.println("Constructed URL: " + constructedUrl); // Debug print

            return constructedUrl;
        } catch (Exception e) {
            System.err.println("Error constructing URL: " + e.getMessage());
            e.printStackTrace();
            return BASE_URL + "unknown-team/";
        }
    }





    public FileStorage getFileStorage(){
        return fileStorage;
    }

}
