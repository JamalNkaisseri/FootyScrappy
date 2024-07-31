import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import java.util.Scanner;
import java.util.Map;

public class Scrappy {
    public static void main(String[] args) {
        WebClient webClient = WebDataSource.createWebClient();
        AjaxRequester ajaxRequester = new AjaxRequester();
        Scanner input = new Scanner(System.in);

        try {
            String mainPageUrl = "https://footystats.org/players/england/bukayo-saka";
            HtmlPage mainPage = webClient.getPage(mainPageUrl);

            String pageTitle = mainPage.getTitleText();
            System.out.println("Page Title: " + pageTitle);


            System.out.print("Enter season(eg 2024 for 23/24):");
            String seasonYear = input.next();
            if(seasonYear != null){
                Map<String, String> playerStats = ajaxRequester.makeAjaxRequest(webClient, seasonYear);

                if (playerStats != null) {
                    System.out.println(playerStats);
                }
            }else{
                System.out.println("Enter a valid season");
            }


        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            webClient.close();
        }
    }
}

