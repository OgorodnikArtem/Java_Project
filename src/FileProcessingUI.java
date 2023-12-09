import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class FileProcessingUI extends JFrame {

    private final JTextField fileNameField;

    JTextField outputFileNameField = new JTextField(10);

    JTextField outputFileFormatField = new JTextField(10);
    private final JTextArea outputArea;

    public FileProcessingUI() {
        setTitle("Обработка файла");
        setSize(2000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fileNameField = new JTextField(10);
        JButton processButton = new JButton("Обработать");
        JButton processButton_ = new JButton("Архивировать");
        JButton processButton_e = new JButton("Остановить");
        outputArea = new JTextArea(10, 10);


        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Имя файла: "));
        inputPanel.add(fileNameField);
        inputPanel.add(new JLabel("Имя выходного файла: "));
        inputPanel.add(outputFileNameField);
        inputPanel.add(new JLabel("Формат выходного файла: "));
        inputPanel.add(outputFileFormatField);
        inputPanel.add(processButton);
        inputPanel.add(processButton_);
        inputPanel.add(processButton_e);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processFile();
            }
        });

        processButton_.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                archiveFile();
                outputArea.setText("Файл архивирован");
            }
        });

        processButton_e.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }


    private void archiveFile() {
        String fileName = fileNameField.getText();
        boolean isArchived = isArchived(fileName);

        archiver archiver = new archiver.Builder(fileName)
                .isArchived(isArchived)
                .build();

        archiver.archiveData(fileName);
    }

    private void processFile() {
        String fileName = fileNameField.getText();
        String outputFileName = outputFileNameField.getText();
        String outputFileFormat = outputFileFormatField.getText();
        String outputFileName_ = outputFileName + "." + outputFileFormat;

        if (fileName.isEmpty() || outputFileName.isEmpty() || outputFileFormat.isEmpty()) {
            outputArea.setText("Пожалуйста, заполните все поля.");
            return;
        }
        boolean isArchived = isArchived(fileName);

        archiver archiver = new archiver.Builder(fileName)
                .isArchived(isArchived)
                .build();

        if (isArchived) {
            fileName = archiver.extractFiles();
        }

        StringBuilder data = new StringBuilder(readFile(fileName));

        if (isArchived) {
            File extractedFolder = new File("extracted_files");
            if (extractedFolder.exists()) {
                File[] extractedFiles = extractedFolder.listFiles();
                if (extractedFiles != null) {
                    for (File extractedFile : extractedFiles) {
                        if (extractedFile.isFile()) {
                            String extractedData = readFile(extractedFile.getPath());
                            data.append("\n").append(extractedData);
                        }
                    }
                }
            }
        }

        Parser Parser = new Parser.Builder(data.toString())
                .build();

        //Parser.parseAndProcessFile(fileName, outputFileName_);

        outputArea.setText(" Входящий файл : \n" + Parser.parseAndProcessFile(fileName, outputFileName_));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileProcessingUI ui = new FileProcessingUI();
            ui.setTitle("Графический UI");
            ui.setVisible(true);
        });

        try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Введите имя файла для консольного интерфейса: ");
            String consoleFileName = consoleReader.readLine();
            String outputfilename = consoleReader.readLine();

            boolean isArchived = isArchived(consoleFileName);

            archiver archiver = new archiver.Builder(consoleFileName)
                    .isArchived(isArchived)
                    .build();

            if (isArchived) {
                consoleFileName = archiver.extractFiles();
            }

            StringBuilder consoleData = new StringBuilder(readFile(consoleFileName));

            if (isArchived) {
                File extractedFolder = new File("extracted_files");
                if (extractedFolder.exists()) {
                    File[] extractedFiles = extractedFolder.listFiles();
                    if (extractedFiles != null) {
                        for (File extractedFile : extractedFiles) {
                            if (extractedFile.isFile()) {
                                String extractedData = readFile(extractedFile.getPath());
                                consoleData.append("\n").append(extractedData);
                            }
                        }
                    }
                }
            }

            Parser parser = new Parser.Builder(consoleData.toString())
                    .build();

            Parser.parseAndProcessFile(consoleFileName , outputfilename);

            System.out.println("Результат из консольного интерфейса: " + consoleData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}