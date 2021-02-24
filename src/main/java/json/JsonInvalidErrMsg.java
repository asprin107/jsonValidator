package json;

public class JsonInvalidErrMsg {
    private String fileName;
    private String errMsg;

    public JsonInvalidErrMsg setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public JsonInvalidErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
