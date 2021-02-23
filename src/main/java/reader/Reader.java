package reader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Reader {
    void readFiles();
    void readMetaFile();
    String getMetaFilePath();
    List<String> getJsonFilesPath();
    Object getMetadata() throws IOException;
    File[] getFiles();
    JSONObject getJsonObjFromFile(File file) throws IOException, JSONException;
}
