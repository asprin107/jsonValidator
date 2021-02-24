import domain.JsonReport;
import json.JsonValidator;
import json.Validator;
import org.junit.Test;
import reader.JsonReader;
import reader.Reader;

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
        for(File file : reader.getFiles()) {
            report.addReport(jsonValidator.check(file, report));
        }

        System.out.println(report);
    }
}
