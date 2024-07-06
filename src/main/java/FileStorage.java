import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;

public class FileStorage {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final ObjectNode jsonData;

    public FileStorage() {
        this.jsonData = OBJECT_MAPPER.createObjectNode();
    }

    public void addData(String key, String value) {
        jsonData.put(key, value);
    }

    public void saveToFile(String filename) throws IOException {
        OBJECT_MAPPER.writeValue(new File(filename), jsonData);
    }

    public ObjectNode getJsonData() {
        return jsonData;
    }

    public String getJsonString() {
        return jsonData.toString();
    }
}
