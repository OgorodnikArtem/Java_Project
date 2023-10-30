import java.io.*;












public class Main {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String fileName = "";

        try {
            System.out.print("Введите имя файла: ");
            fileName = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean isArchived = isArchived(fileName);
        archiver archiver = new archiver.Builder(fileName)
                .isArchived(isArchived)
                .build();

        if (isArchived) {
           fileName = archiver.extractFiles();
        }


        String data = readFile(fileName);

        if (isArchived) {
            File extractedFolder = new File("extracted_files");
            File[] extractedFiles = extractedFolder.listFiles();
            if (extractedFiles != null) {
                for (File extractedFile : extractedFiles) {
                    if (extractedFile.isFile()) {
                        String extractedData = readFile(extractedFile.getPath());
                        data += "\n" + extractedData;
                    }
                }
            }
        }




        parser parser = new parser.Builder(data)
                .build();

        parser.parseAndProcessFile(fileName);

    }

    private static boolean isArchived(String fileName) {
        return fileName.endsWith(".zip") || fileName.endsWith(".rar");
    }

    private static String readFile(String fileName) {
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();
    }
}