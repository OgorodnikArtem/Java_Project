import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;


class archiver {
    private String fileName;
    private boolean isArchived;

    private archiver(Builder builder) {
        this.fileName = builder.fileName;
        this.isArchived = builder.isArchived;
    }

    public static class Builder {
        private String fileName;
        private boolean isArchived;

        public Builder(String fileName) {
            this.fileName = fileName;
        }

        public Builder isArchived(boolean isArchived) {
            this.isArchived = isArchived;
            return this;
        }

        public archiver build() {
            return new archiver(this);
        }
    }

    public String extractFiles() {
        if (isArchived) {
            String extractedFileName = "";
            try {
                if (fileName.endsWith(".zip")) {
                    ZipFile zipFile = new ZipFile(fileName);
                    Enumeration<? extends ZipEntry> entries = zipFile.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        InputStream stream = zipFile.getInputStream(entry);
                        extractedFileName = "extracted_" + entry.getName();
                        File extractedFile = new File(extractedFileName);
                        OutputStream outputStream = new FileOutputStream(extractedFile);
                        FileUtils.copyInputStreamToFile(stream, extractedFile);
                        outputStream.close();
                    }
                    zipFile.close();
                    return extractedFileName;
                } else if (fileName.endsWith(".rar")) {
                    Archive archive = new Archive(new File(fileName));
                    FileHeader fileHeader;
                    while ((fileHeader = archive.nextFileHeader()) != null) {
                        extractedFileName = "extracted_" + fileHeader.getFileNameString();
                        File extractedFile = new File(extractedFileName);
                        FileOutputStream fileOutputStream = new FileOutputStream(extractedFile);
                        archive.extractFile(fileHeader, fileOutputStream);
                        fileOutputStream.close();
                        return extractedFileName;
                    }
                    archive.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RarException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    public void archiveData(String sourceFileName) {
        try {
            FileOutputStream fos = new FileOutputStream("output.zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            File fileToZip = new File(sourceFileName);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
            zipOut.close();
            fos.close();
            System.out.println("File has been archived successfully to output.zip");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
