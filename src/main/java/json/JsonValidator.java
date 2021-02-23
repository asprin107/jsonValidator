package json;

import domain.JsonMetadata;
import domain.JsonReport;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import reader.JsonReader;
import reader.Reader;

import java.io.*;
import java.rmi.UnexpectedException;

public class JsonValidator implements Validator {

    public JsonMetadata metadata;

    @Override
    public boolean setMetadata() {
        Reader reader = new JsonReader();
        reader.readMetaFile();
        metadata = JsonMetadata.getInstance();
        try {
            metadata.data = (JSONObject) reader.getMetadata();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean isValid(File file) throws IOException {
        // json parsing
        try {
            JsonReader jr = new JsonReader();
            jr.getJsonObjFromFile(file);
        } catch (JSONException je) {
            je.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public JsonReport check(File file) throws IOException {
//        JSONObject propertiesObj = metadata.data.getJSONObject("properties");
        JSONObject targetObj = new JsonReader().getJsonObjFromFile(file);
//        for(String key : propertiesObj.keySet()) {
//            System.out.println(key);
//            System.out.println(checkType(propertiesObj.getJSONObject(key), targetObj.get(key)));
//        }
        return checkAllKeys(file.getName(), metadata.data.getJSONObject("properties"), targetObj, new JsonReport());
    }

    private JsonReport checkAllKeys(String fileName, JSONObject metaJson, JSONObject targetJson, JsonReport report) {
        JsonReport rsltReport = new JsonReport();
        try {
            rsltReport = checkAllKeys(metaJson, targetJson, report);
            report.validFiles.add(fileName);
        } catch (UnexpectedException ue) {
            report.invalidFiles.add(fileName);
        }
        return rsltReport;
    }
    private JsonReport checkAllKeys(JSONObject metaJson, JSONObject targetJson, JsonReport report) throws UnexpectedException {
        boolean flag = false;
        for(String key : metaJson.keySet()) {
            checkType(key, metaJson.getJSONObject(key), targetJson.get(key), report);
        }
        return report;
    }

    private boolean checkType(String key, JSONObject metaJson, Object targetJsonValue, JsonReport report) throws UnexpectedException {
        boolean flag = false;
        switch (metaJson.getString("type")) {
            case "string":
                if(targetJsonValue instanceof String) {
                    flag = true;
                    break;
                } else {
                    // err msg 추가하고
//                    report.errMsg.add(key + "에서 타입 불일치.");
                }
            case "number": if(targetJsonValue instanceof Integer) flag = true; break;
            case "object":
                if(targetJsonValue instanceof JSONObject) {
                    checkAllKeys(metaJson, (JSONObject) targetJsonValue, report);
                } else {
                    flag = true;
                    break;
                }
            case "array":
                if(targetJsonValue instanceof JSONArray) {
                    JSONArray jsonArr = (JSONArray) targetJsonValue;
                    for(int i=0 ; i<jsonArr.length() ; i++) {
                        checkAllKeys(metaJson, jsonArr.getJSONObject(i), report);
                    }
                } else {
                    flag = true;
                    break;
                }
            default:
                throw new UnexpectedException("정의되지 않은 타입이 메타데이터에 포함됨.");
        }
        return flag;
    }

}
