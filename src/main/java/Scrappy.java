import java.net.http.HttpClient;
import java.util.Scanner;
import java.util.Map;

public class Scrappy {
    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newHttpClient();
        AjaxRequester ajaxRequester = new AjaxRequester();
        Scanner input = new Scanner(System.in);

        try {
            String mainPageUrl = "https://footystats.org/players/england/bukayo-saka";

            // Simulate navigating to the main page (this part is now omitted since we're focusing on AJAX)
            System.out.println("Navigating to: " + mainPageUrl);

            // Just a placeholder since we're not actually loading the HTML page with HttpClient
            String pageTitle = "Bukayo Saka Player Stats | FootyStats";
            System.out.println("Page Title: " + pageTitle);

            System.out.print("Enter season (e.g., 2024 for 23/24): ");
            String seasonYear = input.next();

            if (seasonYear != null) {
                Map<String, String> playerStats = ajaxRequester.makeAjaxRequest(httpClient, seasonYear);

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
