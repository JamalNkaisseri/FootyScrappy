public class UrlGenerator {

    // Method to construct the URL using player name and country
    public String generatePlayerUrl(String country, String playerName) {
        // Convert the country and player name to lowercase and replace spaces with hyphens for proper URL formatting
        String formattedCountry = formatStringForUrl(country);
        String formattedPlayerName = formatStringForUrl(playerName);

        // Construct the URL based on the pattern from the example
        return "https://footystats.org/players/" + formattedCountry + "/" + formattedPlayerName;
    }

    // Helper method to format the string for the URL
    private String formatStringForUrl(String str) {
        // Convert to lowercase and replace spaces with hyphens
        return str.trim().toLowerCase().replace(" ", "-");
    }
}

