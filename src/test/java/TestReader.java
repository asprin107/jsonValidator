import org.json.JSONObject;
import org.junit.Test;
import reader.JsonReader;
import reader.Reader;

import java.io.File;
import java.io.IOException;

public class TestReader {

    Reader reader = new JsonReader();
    File metaFile = new File(System.getProperty("user.dir")+"\\meta\\rule.json");

    @Test
    public void testReader() {
        reader.readMetaFile();
        reader.readFiles();
        System.out.println(reader.getMetaFilePath());
        System.out.println(reader.getJsonFilesPath());
    }

    @Test
    public void testGetJsonFromFile() throws IOException {
        JSONObject json = reader.getJsonObjFromFile(metaFile);
        System.out.println(json.toString());
    }
}
