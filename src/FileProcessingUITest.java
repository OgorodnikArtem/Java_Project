import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.*;

public class FileProcessingUITest {

    @Test
    public void testIsArchivedWithZipExtension() {
        String zipFileName = "example.zip";
        assertTrue(FileProcessingUI.isArchived(zipFileName));
    }

    @Test
    public void testIsArchivedWithOtherExtension() {
        String txtFileName = "example.txt";
        assertFalse(FileProcessingUI.isArchived(txtFileName));
    }

    @Test
    public void testIsArchivedWithEmptyFileName() {
        assertFalse(FileProcessingUI.isArchived(""));
    }




    @Test
    public void testParseAndProcessFileWithXmlFile() {
        String result = Parser.parseAndProcessFile("input.xml", "output.txt");
        assertNotNull(result);
        assertTrue(result.contains("<arithmeticOperations>"));
    }

    @Test
    public void testParseAndProcessFileWithJsonFile() {
        String result = Parser.parseAndProcessFile("input_.json", "output.txt");
        assertNotNull(result);
        assertTrue(result.contains("\"tasks\": ["));
    }

    @Test
    public void testParseAndProcessFileWithTxtFile() {
        String result = Parser.parseAndProcessFile("input.txt", "output.txt");
        assertNotNull(result);
        assertTrue(result.trim().startsWith("\"number\":"));
    }
}
