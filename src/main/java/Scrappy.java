import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Scanner;

public class Scrappy {

    public static void main(String[] args) {

        //Get team name from the user
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the team name: ");
        String teamName = scanner.nextLine().trim();

        if (teamName.isEmpty()) {
            System.out.println("Team name cannot be empty. Exiting program.");
            scanner.close();
            return;
        }

        try {
            // Step 2: Create an instance of WebDataSource to fetch data
            WebDataSource source = new WebDataSource();

            // Step 3: Fetch data for the given team name
            String result = source.fetchData(teamName);

            // Step 4: Check if data fetching was successful
            if (result != null) {
                // Step 5: Print the fetched result (if any)
                System.out.println(result);

                // Step 6: Get the FileStorage object from the data source
                FileStorage fileStorage = source.getFileStorage();

                // Step 7: Display the stats using the previously defined method
                displayTeamStats(fileStorage.getJsonData());

                // Step 8: Save the JSON data to a file with a formatted name
                String fileName = teamName.toLowerCase().replace(" ", "_") + "Arsenal_data.json";
                fileStorage.saveToFile(fileName);

                // Step 9: Print confirmation of data saved
                System.out.println("Data saved to " + fileName);
            } else {
                // Step 10: Handle case where data fetching failed
                System.out.println("Failed to fetch data for " + teamName);
            }
        } catch (Exception e) {
            // Step 11: Catch and handle any exceptions that occur
            System.err.println("Error occurred: " + e.getMessage());
        }
    }


    private static void displayTeamStats(JsonNode jsonData) {
        // Step 1: Print section header
        System.out.println("\n===== Team Statistics =====");

        // Step 2: Print team name
        System.out.println("Team: " + jsonData.get("teamName").asText());

        // Step 3: Print separator
        System.out.println("---------------------------");

        // Step 4: Define stat names
        String[] statNames = {"Home Form", "Played", "W", "D", "L", "Goals For", "Goals Against"};

        // Step 5: Iterate over stat names
        for (String statName : statNames) {
            // Step 6: Check if stat exists in JSON data
            if (jsonData.has(statName)) {
                // Step 7: Print formatted stat name and value
                System.out.printf("%-20s: %s\n", statName, jsonData.get(statName).asText());
            }
        }

        // Step 8: Print separator
        System.out.println("===========================");

        // Step 9: Calculate and display win percentage
        if (jsonData.has("Matches Played") && jsonData.has("Wins")) {
            int played = Integer.parseInt(jsonData.get("Matches Played").asText());
            int won = Integer.parseInt(jsonData.get("Wins").asText());
            if (played > 0) {
                double winPercentage = (double) won / played * 100;
                // Step 10: Print formatted win percentage
                System.out.printf("Win Percentage    : %.2f%%\n", winPercentage);
            }
        }

        // Step 11: Print final separator
        System.out.println("===========================");
    }

}
