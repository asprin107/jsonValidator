package domain;

import org.json.JSONObject;

public class JsonMetadata {

    public JSONObject data;

    //private construct
    private JsonMetadata() {}

    private static class InnerInstanceClazz {
        private static final JsonMetadata instance = new JsonMetadata();
    }

    public static JsonMetadata getInstance() {
        return InnerInstanceClazz.instance;
    }
}
