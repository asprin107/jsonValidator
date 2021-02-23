package json;

import domain.JsonReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface Validator {
    boolean setMetadata() throws FileNotFoundException;
    boolean isValid(File file) throws IOException;
    JsonReport check(File file) throws IOException;
}
