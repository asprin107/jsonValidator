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

    public JsonReport addReport(JsonReport report) {
        validFiles.addAll(report.validFiles);
        invalidFiles.addAll(report.invalidFiles);
        errMsg.addAll(report.errMsg);
        return this;
    }

    @Override
    public String toString() {
        return "JsonReport{" +
                "validFiles=" + validFiles +
                ", invalidFiles=" + invalidFiles +
                ", errMsg=" + errMsg +
                '}';
    }
}
