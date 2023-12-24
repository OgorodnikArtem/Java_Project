import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.desktop.SystemSleepEvent;
import java.io.File;

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
        assertTrue(result.contains("<math_operations>"));
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
        assertTrue(result.trim().startsWith("5 +4 ="));
    }


    @Test
    public void testEncryptFile() {
        String inputFile = "input.txt";
        String outputFile = "encrypted_test.txt";

        FileEncryptor.encryptFile(inputFile, outputFile);

        File encryptedFile = new File(outputFile);
        assertTrue(encryptedFile.exists());
        assertTrue(encryptedFile.length() > 0);

        // Clean up
        encryptedFile.delete();
    }


    @Test
    public void testDecryptFile() {
        String inputFile = "input.txt";
        String encryptedFile = "encrypted_test.txt";
        String outputFile = "decrypted_test.txt";

        FileEncryptor.encryptFile(inputFile, encryptedFile);
        FileEncryptor.decryptFile(encryptedFile, outputFile);

        File decryptedFile = new File(outputFile);
        assertTrue(decryptedFile.exists());
        assertTrue(decryptedFile.length() > 0);

        // Clean up
        decryptedFile.delete();
        new File(encryptedFile).delete();
    }









    private FrameFixture window;

    @Before
    public void setUp() {
        FileProcessingUI fileProcessingUI = new FileProcessingUI();
        window = new FrameFixture(fileProcessingUI);
        window.show();
    }

    @After
    public void tearDown() {
        window.cleanUp();
    }

    @Test
    public void testProcessFileWithAutoFill() throws InterruptedException {
        window.textBox("fileNameField").setText("input.txt");
        window.textBox("outputFileNameField").setText("output");
        window.textBox("outputFileFormatField").setText("txt");

        Thread.sleep(10000);

        window.button("processButton").click();

        assertEquals(" Входящий файл : ", window.textBox("outputArea").text());
    }
}