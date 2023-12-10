import java.io.*;

public class FileEncryptor {

    private static String data;

    private FileEncryptor(FileEncryptor.Builder builder) {
        this.data = builder.data;
    }

    public static class Builder {
        private String data;

        public Builder(String data) {
            this.data = data;
        }

        public FileEncryptor build() {
            return new FileEncryptor(this);
        }
    }
    private static final int KEY = 123;


    public static void encryptFile(String inputFile, String outputFile) {
        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            int data;
            while ((data = fis.read()) != -1) {
                fos.write(data ^ KEY);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decryptFile(String inputFile, String outputFile) {
        encryptFile(inputFile, outputFile);
    }
}