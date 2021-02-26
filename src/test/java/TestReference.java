import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestReference {

    @Test
    public void testReference() {
        TestClass ttt = new TestClass();
        editReferClass(ttt, "filename");
        editReferClass(ttt, "filename1");
        editReferClass(ttt, "filename2");

        System.out.println(ttt.toString());
    }

    private void editReferClass(TestClass tc, String text) {
        tc.fileNames.add(text);
    }

    private class TestClass {
        List<String> fileNames = new ArrayList<>();

        @Override
        public String toString() {
            return "TestClass{" +
                    "fileNames=" + fileNames +
                    '}';
        }
    }
}
