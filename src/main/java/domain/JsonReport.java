package domain;

import json.JsonInvalidErrMsg;

import java.util.ArrayList;
import java.util.List;

public class JsonReport {
    public List<String> validFiles;
    public List<String> invalidFiles;
    public List<JsonInvalidErrMsg> errMsg;

    public JsonReport() {
        this.validFiles = new ArrayList<>();
        this.invalidFiles = new ArrayList<>();
        this.errMsg = new ArrayList<>();
    }
}
