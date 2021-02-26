package report;

import domain.JsonReport;
import json.JsonInvalidErrMsg;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Writer {

    private final String WINDOWS_SEPARATOR = "\\";
    private final String LINUX_MAC_SEPARATOR = "/";

    private final String CURR_DIR = System.getProperty("user.dir");
    private final String SUCCESS_FILE = "성공.txt";
    private final String FAIL_FILE = "실패.txt";

    public void write(JsonReport report) {
        System.out.println(CURR_DIR + WINDOWS_SEPARATOR + SUCCESS_FILE);
        writeReport(new File(CURR_DIR + WINDOWS_SEPARATOR + SUCCESS_FILE), "성공 파일 리스트", report, false);
        writeReport(new File(CURR_DIR + WINDOWS_SEPARATOR + FAIL_FILE), "실패 파일 리스트", report, true);
    }

    private void writeReport(File file, String title, JsonReport report, boolean needReason) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write("========================================\n");
            writer.write(title+"\n");
            writer.write("----------------------------------------\n");
            writeReportContent(report, writer, needReason);
            writer.write("========================================\n");
        } catch (IOException e) {
            // TODO report write to file error
            e.printStackTrace();
        }
    }

    private void writeReportContent(JsonReport report, BufferedWriter writer, boolean needReason) throws IOException {
        int i = 1;
        if (needReason) {
            for(JsonInvalidErrMsg record : report.errMsg) {
                writer.write((i++) + ". " + record.getFileName() + " : " + record.getErrMsg() + "\n");
            }
        } else {
            for(String record : report.validFiles) {
                writer.write((i++) + ". " + record + "\n");
            }
        }
    }
}
