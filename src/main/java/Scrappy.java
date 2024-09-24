import java.net.http.HttpClient;
import java.util.Scanner;
import java.util.Map;

public class Scrappy {
    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newHttpClient();
        AjaxRequester ajaxRequester = new AjaxRequester();
        Scanner input = new Scanner(System.in);

        try {
            System.out.print("Enter player name: ");
            String name = input.nextLine();

            System.out.print("Enter country: ");
            String nation = input.nextLine(); // Use nextLine to capture full country name

            System.out.print("Enter season (e.g., 2024 for 23/24): ");
            String seasonYear = input.nextLine();

            // Format the player name and country for URL
            String formattedName = ajaxRequester.formatStringForUrl(name);
            String formattedNation = ajaxRequester.formatStringForUrl(nation);

            String mainPageUrl = "https://footystats.org/players/" + formattedNation + "/" + formattedName;

            // Simulate navigating to the main page
            System.out.println("Navigating to: " + mainPageUrl);
            System.out.println(name + " | Premier League Stats");

            if (seasonYear != null) {
                Map<String, String> playerStats = ajaxRequester.makeAjaxRequest(httpClient, seasonYear, formattedNation, formattedName);

                if (playerStats != null && !playerStats.isEmpty()) {
                    System.out.println(playerStats);
                } else {
                    System.out.println("No data available for the provided season.");
                }
            } else {
                System.out.println("Enter a valid season");
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
