import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class FileProcessingUI extends JFrame {

    private final JTextField fileNameField;

    private final JTextArea outputArea;

    public FileProcessingUI() {
        // Настройка основного окна
        setTitle("Обработка файла");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Создание компонентов UI
        fileNameField = new JTextField(20);
        JButton processButton = new JButton("Обработать");
        JButton processButton_ = new JButton("Архивировать");
        JButton processButton_e = new JButton("Остановить");
        outputArea = new JTextArea(10, 30);


        // Установка Layout Manager
        setLayout(new BorderLayout());

        // Панель для ввода имени файла
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Имя файла: "));
        inputPanel.add(fileNameField);
        inputPanel.add(processButton);
        inputPanel.add(processButton_);
        inputPanel.add(processButton_e);

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

        Parser.parseAndProcessFile(fileName);

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

            Parser.parseAndProcessFile(consoleFileName);

            System.out.println("Результат из консольного интерфейса: " + consoleData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
