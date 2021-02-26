import domain.JsonReport;
import json.JsonValidator;
import json.Validator;
import org.junit.Test;
import reader.JsonReader;
import reader.Reader;
import report.Writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestJsonValidator {
    @Test
    public void testFileIsJson() throws IOException {
        Reader reader = new JsonReader();
        reader.readMetaFile();

        Validator jsonValidator = new JsonValidator();
        System.out.println(jsonValidator.isValid(new File(reader.getMetaFilePath())));
    }

    @Test
    public void testReadMetaData() throws FileNotFoundException {
        Validator jsonValidator = new JsonValidator();
        System.out.println(jsonValidator.setMetadata());
        System.out.println();
    }

    @Test
    public void testJsonDataValid() throws IOException {
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

        Writer writer = new Writer();
        writer.write(report);

        System.out.println(report);
    }

    private void printCurrentProgress(int currNum, String currFile, int totFiles) throws IOException {
        System.out.println("In progress : " + +currNum + "/" + totFiles + " : " + currFile);
    }
}
