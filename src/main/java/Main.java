import domain.JsonReport;
import json.JsonValidator;
import json.Validator;
import reader.JsonReader;
import reader.Reader;
import report.Writer;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Reader reader = new JsonReader();
        reader.readMetaFile();
        reader.readFiles();

        Validator jsonValidator = new JsonValidator();
        jsonValidator.setMetadata();
        JsonReport report = new JsonReport();
        int totFiles = reader.getFiles().length;

        for(int i=0 ; i<totFiles ; i++) {
            File currFile = reader.getFiles()[i];
            // print progress
            printCurrentProgress(i+1, currFile.getName(), totFiles);
            // run test
            jsonValidator.check(currFile, report);
        }

        System.out.println("Validation Check Finished.");

        // file write
        Writer writer = new Writer();
        writer.write(report);
    }

    private static void printCurrentProgress(int currNum, String currFile, int totFiles) throws IOException {
        System.out.println("In progress : " + +currNum + "/" + totFiles + " : " + currFile);
    }

}
