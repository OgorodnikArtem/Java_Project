import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    public void testEncryptFile() {
        String inputFile = "input.txt";
        String outputFile = "en_input.txt";

        FileEncryptor.encryptFile(inputFile, outputFile);

        File encryptedFile = new File(outputFile);
        assertTrue(encryptedFile.exists());
        assertTrue(encryptedFile.length() > 0);

        encryptedFile.delete();
    }


    @Test
    public void testDecryptFile() {
        String inputFile = "input.txt";
        String encryptedFile = "en_input.txt";
        String outputFile = "de_en_input.txt";

        FileEncryptor.encryptFile(inputFile, encryptedFile);
        FileEncryptor.decryptFile(encryptedFile, outputFile);

        File decryptedFile = new File(outputFile);
        assertTrue(decryptedFile.exists());
        assertTrue(decryptedFile.length() > 0);

        decryptedFile.delete();
        new File(encryptedFile).delete();
    }

    @Test
    public void testEvaluateExpression_1() {
        String expression = "2 + 3 * 4";
        double result = Parser.evaluateExpression(expression);
        assertEquals(14.0, result, 0.001);
    }

    @Test
    public void testEvaluateExpression_2() {
        String expression = "1 * 2 * 4";
        double result = Parser.evaluateExpression(expression);
        assertEquals(8.0, result, 0.001);
    }

    @Test
    public void testEvaluateExpression_3() {
        String expression = "10 * 10 / 10";
        double result = Parser.evaluateExpression(expression);
        assertEquals(10.0, result, 0.001);
    }


    @Test
    public void testWriteXmlOutput_1() {
        Parser.Results[] results = {new Parser.Results(1, "2 + 3", 5.0)};
        String outputFileName = "output.xml";
        Parser.writeXmlOutput(results, outputFileName, 1);
    }

    @Test
    public void testWriteXmlOutput_2() {
        Parser.Results[] results = {new Parser.Results(1, "5 * 4", 20.0)};
        String outputFileName = "output.xml";
        Parser.writeXmlOutput(results, outputFileName, 1);
    }

    @Test
    public void testWriteXmlOutput_3() {
        Parser.Results[] results = {new Parser.Results(1, "4 + 5", 9.0)};
        String outputFileName = "output.xml";
        Parser.writeXmlOutput(results, outputFileName, 1);
    }

    @Test
    public void testWriteJsonOutput_1() {
        Parser.Results[] results = {new Parser.Results(1, "2 + 3", 5.0)};
        String outputFileName = "output.json";
        Parser.writeJsonOutput(results, outputFileName, 1);
    }

    @Test
    public void testWriteJsonOutput_2() {
        Parser.Results[] results = {new Parser.Results(1, "5 + 4", 9.0)};
        String outputFileName = "output.json";
        Parser.writeJsonOutput(results, outputFileName, 1);
    }

    @Test
    public void testWriteJsonOutput_3() {
        Parser.Results[] results = {new Parser.Results(1, "4 * 5", 20.0)};
        String outputFileName = "output.json";
        Parser.writeJsonOutput(results, outputFileName, 1);
    }

    @Test
    public void testWriteTxtOutput_1() {
        Parser.Results[] results = {new Parser.Results(1, "2 + 3", 5.0)};
        String outputFileName = "output.txt";
        Parser.writeTxtOutput(results, outputFileName, 1);
    }

    @Test
    public void testWriteTxtOutput_2() {
        Parser.Results[] results = {new Parser.Results(1, "5 + 4", 9.0)};
        String outputFileName = "output.txt";
        Parser.writeTxtOutput(results, outputFileName, 1);
    }

    @Test
    public void testWriteTxtOutput_3() {
        Parser.Results[] results = {new Parser.Results(1, "4 * 5", 20.0)};
        String outputFileName = "output.txt";
        Parser.writeTxtOutput(results, outputFileName, 1);
    }

    @Test
    public void testArchiveDataToZip() throws IOException {
        String sourceFileName = "test.txt";
        String outputFileName = "archived_test";
        createTestFile(sourceFileName);

        archiver archiver = new archiver.Builder(sourceFileName).isArchived(true).build();
        archiver.archiveData(sourceFileName, outputFileName);

        String archivedFileName = outputFileName + ".zip";
        assertTrue(Files.exists(Paths.get(archivedFileName)));

        cleanup(sourceFileName, archivedFileName);
    }

    private void createTestZipFile(String zipFileName, String contentFileName) throws IOException {
        try (java.util.zip.ZipOutputStream zipOut = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(zipFileName))) {
            java.io.File fileToZip = new java.io.File(contentFileName);
            java.io.FileInputStream fis = new java.io.FileInputStream(fileToZip);
            java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
    }

    private void createTestFile(String fileName) throws IOException {
        Files.write(Paths.get(fileName), "Test content".getBytes());
    }

    private void cleanup(String... fileNames) throws IOException {
        for (String fileName : fileNames) {
            Files.deleteIfExists(Paths.get(fileName));
        }
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
}