package json;

import domain.JsonMetadata;
import domain.JsonReport;
import exception.JsonValidationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import reader.JsonReader;
import reader.Reader;

import java.io.File;
import java.io.IOException;
import java.rmi.UnexpectedException;
import java.util.Arrays;

public class JsonValidator implements Validator {

    public JsonMetadata metadata;
    public String[] notKey = {"type", "items", "enum"};
    public String propertyKey = "properties";

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
    public JsonReport check(File file, JsonReport report) throws IOException {
        JSONObject targetObj = new JsonReader().getJsonObjFromFile(file);
        return checkAllKeysInit(file.getName(), metadata.data, targetObj, report);
    }

    private JsonReport checkAllKeysInit(String fileName, JSONObject metaJson, JSONObject targetJson, JsonReport report) {
        JsonReport rsltReport = report;
        try {
            rsltReport.addReport(checkAllKeys(metaJson, targetJson, report));
            rsltReport.validFiles.add(fileName);
        } catch (UnexpectedException ue) {
            rsltReport.invalidFiles.add(fileName);
        } catch (JsonValidationException jve) {
            rsltReport.invalidFiles.add(fileName);
            int lastErrMsgAdded = report.errMsg.size()-1;
            rsltReport.errMsg.set(lastErrMsgAdded, report.errMsg.get(lastErrMsgAdded).setFileName(fileName));
        }
        return rsltReport;
    }

    private JsonReport checkAllKeys(JSONObject metaJson, Object targetJson, JsonReport report) throws UnexpectedException, JsonValidationException {
        JsonReport rsltReport = report;
        for(String key : metaJson.keySet()) {
            switch (key) {
                case"enum": break; // value Chk
                case"items": break; // array type Chk
                case"type": break; // type Chk
                case"title": case"description": break;// do nothing
                case "properties":
                    rsltReport.addReport(checkAllKeys(metaJson.getJSONObject(propertyKey), targetJson, report));
                    break;
                default:
                    // general keys
                    if(targetJson instanceof JSONObject) {
                        if(((JSONObject)targetJson).has(key)) {
                            rsltReport.addReport(checkType(key, metaJson.getJSONObject(key), ((JSONObject)targetJson).get(key), report));
                        } else {
                            // 해당 키는 optional 일 가능성 있음. 구문적합성 검사에 의해 skip 하고 정상처리.
                        }
                    } else if (targetJson instanceof JSONArray) {
                        rsltReport.addReport(checkType(key, metaJson.getJSONObject(key), targetJson, report));
                    }
                    break;
            }
        }
        return rsltReport;
    }

    private JsonReport checkType(String key, JSONObject metaJson, Object targetJsonValue, JsonReport report) throws UnexpectedException, JsonValidationException {
        String typeErrMsg = "[" + key + "] 키에서 타입 불일치.";
        String undefinedErrMsg = "정의되지 않은 타입이 메타데이터에 포함됨.";

        JsonReport rsltReport = report;

        switch (metaJson.getString("type")) {
            case "string":
                if(targetJsonValue instanceof String) {
                    rsltReport.addReport(checkValue(key, metaJson, targetJsonValue, report));
                    break;
                } else {
                    rsltReport.errMsg.add(new JsonInvalidErrMsg(typeErrMsg));
                    throw new JsonValidationException(typeErrMsg);
                }

            case "number": if(targetJsonValue instanceof Integer) {
                rsltReport.addReport(checkValue(key, metaJson, targetJsonValue, report));
                break;
            } else {
                rsltReport.errMsg.add(new JsonInvalidErrMsg(typeErrMsg));
                throw new JsonValidationException(typeErrMsg);
            }

            case "object":
                if(targetJsonValue instanceof JSONObject) {
                    rsltReport.addReport(checkAllKeys(metaJson, targetJsonValue, report));
                    break;
                } else {
                    rsltReport.errMsg.add(new JsonInvalidErrMsg(typeErrMsg));
                    throw new JsonValidationException(typeErrMsg);
                }

            case "array":
                if(targetJsonValue instanceof JSONArray) {
                    JSONArray jsonArr = (JSONArray) targetJsonValue;
                    for(int i=0 ; i<jsonArr.length() ; i++) {
                        rsltReport.addReport(checkAllKeys(metaJson.getJSONObject("items"), ((JSONArray)targetJsonValue).get(i), report));
                    }
                    break;
                } else {
                    rsltReport.errMsg.add(new JsonInvalidErrMsg(typeErrMsg));
                    throw new JsonValidationException(typeErrMsg);
                }

            default:
                throw new UnexpectedException(undefinedErrMsg);
        }

        return report;
    }

    private JsonReport checkValue(String key, JSONObject metaJson, Object targetJson, JsonReport report) {
        String valErrMsg = "[" + key + "] 키에서 허용되지 않은 값 사용됨.";
        JsonReport rsltReport = report;

        if(metaJson.has("enum") && !enumChk(metaJson.getJSONArray("enum"), targetJson)) {
            // 에러 보고
            JsonReport errReport = new JsonReport();
            errReport.errMsg.add(new JsonInvalidErrMsg(valErrMsg));
            rsltReport.addReport(errReport);
        }

        return rsltReport;
    }

    private boolean enumChk(JSONArray enumList, Object value) {
        Object[] availableValueList = new Object[enumList.length()];
        for(int i=0 ; i < enumList.length() ; i++) {
            availableValueList[i] = enumList.get(i);
        }
        return Arrays.asList(availableValueList).contains(value);
    }

}
