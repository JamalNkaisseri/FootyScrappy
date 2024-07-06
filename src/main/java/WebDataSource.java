
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;

import java.util.List;

public class WebDataSource implements DataSource {

    private static final String BASE_URL = "https://footystats.org";
    private static final WebClient CLIENT = createWebClient();
    private final FileStorage fileStorage;


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

    public String fetchData(String teamName){

        try{
            String teamUrl = constructTeamUrl(teamName);
            HtmlPage page = CLIENT.getPage(teamUrl);
            fetchTeamStats(page, teamName);
            return "Data fetched successfully for " + teamName;
        }catch (Exception e){
            System.err.println("Error fetching data: " + e.getMessage());
            return null;
        }
    }

    private String constructTeamUrl(String teamName) {
        // Convert team name to URL format (e.g., "Manchester United" to "manchester-united")
        String formattedName = teamName.toLowerCase().replace(" ", "-");
        return BASE_URL + formattedName;
    }

    private void fetchTeamStats(HtmlPage page, String teamName) {
        try {
            // Step 1: Add teamName to JSON storage
            fileStorage.addData("teamName", teamName);

            // Step 2: Use XPath to get the row of stats
            List<HtmlElement> statElements = page.getByXPath("(//div[contains(@class,'row club-data-table-row dFlex')])[1]//div[position() <= 7]");

            // Step 3: Check if we retrieved enough stat elements
            if (statElements.size() >= 7) {
                // Step 4: Define the names of the stats
                String[] statNames = {"Home Form", "Played", "W", "D", "L", "Goals For", "Goals Against"};

                // Step 5: Iterate over stat names array
                for (int i = 0; i < statNames.length; i++) {
                    // Step 6: Get text content of each stat element and trim whitespace
                    String statValue = statElements.get(i).getTextContent().trim();

                    // Step 7: Add stat name and value to JSON storage
                    fileStorage.addData(statNames[i], statValue);
                }
            } else {
                // Step 8: Handle case where not enough stats were found
                System.err.println("Couldn't find all expected stats for " + teamName);
            }
        } catch (Exception e) {
            // Step 9: Catch and handle any exceptions
            System.err.println("Error fetching team stats: " + e.getMessage());
        }
    }


    public FileStorage getFileStorage(){
        return fileStorage;
    }



}
