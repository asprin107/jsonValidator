package reader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JsonReader implements Reader{
    private File metaFile;
    private File[] files;

    private final String WINDOWS_SEPARATOR = "\\";
    private final String LINUX_MAC_SEPARATOR = "/";

    private final String CURR_DIR = System.getProperty("user.dir");
    private final String JSON_DIR = "json";
    private final String META_DIR = "meta";
    private final String RULE_JSON_FILE = "rule.json";

    @Override
    public void readFiles() {
        this.files = new File(CURR_DIR + WINDOWS_SEPARATOR + JSON_DIR).listFiles();
    }

    @Override
    public void readMetaFile() {
        this.metaFile = new File(CURR_DIR + WINDOWS_SEPARATOR + META_DIR + WINDOWS_SEPARATOR + RULE_JSON_FILE);
    }

    @Override
    public String getMetaFilePath() {
        return metaFile.getPath();
    }

    @Override
    public File[] getFiles() {
        return files;
    }

    public List<String> getJsonFilesPath() {
        return Arrays.stream(files).map(File::getPath).collect(Collectors.toList());
    }

    @Override
    public Object getMetadata() throws IOException {
        JSONObject json = null;
        try {
            json = getJsonObjFromFile(metaFile);
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return json;
    }

    public JSONObject getJsonObjFromFile(File file) throws IOException, JSONException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder jsonRawData = new StringBuilder();

        String bytesRead;
        while ((bytesRead = br.readLine()) != null) {
            jsonRawData.append(bytesRead);
        }

        // json parsing
        return new JSONObject(jsonRawData.toString());
    }
}
