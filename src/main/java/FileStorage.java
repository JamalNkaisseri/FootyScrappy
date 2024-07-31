import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;

public class FileStorage {
    private ObjectNode jsonData;

    public FileStorage() {
        this.jsonData = new ObjectMapper().createObjectNode();
    }

    public void storeData(String key, String value) {
        jsonData.put(key, value);
    }

    public JsonNode getJsonData() {
        return jsonData;
    }

    public void saveToFile(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), jsonData);
            System.out.println("Data saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
