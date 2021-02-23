import reader.JsonReader;
import reader.Reader;

public class Main {
    public static void main(String[] args) {
        Reader reader = new JsonReader();
        reader.readFiles();
        reader.readMetaFile();
    }

}
