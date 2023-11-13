import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class FileProcessingUI extends JFrame {

    private JTextField fileNameField;
    private JButton processButton;
    private JTextArea outputArea;

    public FileProcessingUI() {
        // Настройка основного окна
        setTitle("Обработка файла");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Создание компонентов UI
        fileNameField = new JTextField(20);
        processButton = new JButton("Обработать");
        outputArea = new JTextArea(10, 30);

        // Установка Layout Manager
        setLayout(new BorderLayout());

        // Панель для ввода имени файла
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Имя файла: "));
        inputPanel.add(fileNameField);
        inputPanel.add(processButton);

        // Добавление компонентов в окно
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Добавление обработчика события для кнопки
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processFile();
            }
        });
    }

    private void processFile() {
        String fileName = fileNameField.getText();
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
            if (extractedFolder.exists()) {
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
        }

        parser parser = new parser.Builder(data)
                .build();

        parser.parseAndProcessFile(fileName);

        // Вывод результата в текстовую область
        outputArea.setText("Файл обработан");
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
        // Запуск графического интерфейса в отдельном потоке
        SwingUtilities.invokeLater(() -> {
            FileProcessingUI ui = new FileProcessingUI();
            ui.setTitle("Графический UI");
            ui.setVisible(true);
        });

        // Запуск консольного интерфейса в основном потоке
        try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Введите имя файла для консольного интерфейса: ");
            String consoleFileName = consoleReader.readLine();

            boolean isArchived = isArchived(consoleFileName);

            archiver archiver = new archiver.Builder(consoleFileName)
                    .isArchived(isArchived)
                    .build();

            if (isArchived) {
                consoleFileName = archiver.extractFiles();
            }

            String consoleData = readFile(consoleFileName);

            if (isArchived) {
                File extractedFolder = new File("extracted_files");
                if (extractedFolder.exists()) {
                    File[] extractedFiles = extractedFolder.listFiles();
                    if (extractedFiles != null) {
                        for (File extractedFile : extractedFiles) {
                            if (extractedFile.isFile()) {
                                String extractedData = readFile(extractedFile.getPath());
                                consoleData += "\n" + extractedData;
                            }
                        }
                    }
                }
            }

            parser parser = new parser.Builder(consoleData)
                    .build();

            parser.parseAndProcessFile(consoleFileName);

            System.out.println("Результат из консольного интерфейса: " + consoleData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
