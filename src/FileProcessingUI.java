import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.sound.sampled.*;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class FileProcessingUI extends JFrame {

    private static Clip clip;

    static int n = 0;

    final JTextField fileNameField;

    JTextField outputFileNameField = new JTextField(10);

    JTextField outputFileFormatField = new JTextField(10);

    static JTextArea outputArea;

    public FileProcessingUI() {

        setTitle("Обработка файла");
        setSize(2000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fileNameField = new JTextField(10);
        JButton processButton = new JButton("Обработать");
        JButton processButton_ = new JButton("Архивировать");
        JButton processButton_encryptor = new JButton("Зашифровать");
        JButton processButton_decryptor = new JButton("Расшифровать");
        JButton processButton_e = new JButton("Остановить");
        JButton processButton_O = new JButton("Тесты возможностей UI");
        outputArea = new JTextArea(20, 10);


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
        inputPanel.add(processButton_encryptor);
        inputPanel.add(processButton_decryptor);
        inputPanel.add(processButton_e);
        inputPanel.add(processButton_O);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);




        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color randomColor = new Color(
                        (int) (Math.random() * 256),
                        (int) (Math.random() * 256),
                        (int) (Math.random() * 256)
                );


                Color randomColor_ = new Color(
                        (int) (Math.random() * 256),
                        (int) (Math.random() * 256),
                        (int) (Math.random() * 256)
                );

                inputPanel.setBackground(randomColor);

                outputArea.setBackground(randomColor_);
            }
        });


        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processFile();
            }
        });

        processButton_encryptor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EncryptFile();
            }
        });

        processButton_decryptor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DecryptFile();
            }
        });

        processButton_.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                archiveFile(outputFileNameField.getText());
                outputArea.setText("Файл архивирован");
            }
        });

        processButton_e.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        processButton_O.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String filepath_ = "D:\\Java\\Java_Project\\7b7f.gif";

                displayGif(inputPanel , filepath_);

                if(n == 0){
                    stopSound();
                    playSound("D:\\Java\\Java_Project\\rick-astley-never-gonna-give-you-up.wav" , 0);
                    n++;
                }else if(n == 1){
                    stopSound();
                    playSound("D:\\Java\\Java_Project\\Mr President – Coco Jamboo.wav" , 57300);
                    n++;
                }else if(n == 2){
                    stopSound();
                    playSound("D:\\Java\\Java_Project\\homie_-_bezumno-mozhno-byt-pervym.wav" , 90000);
                    n = 0;
                }
                timer.start();

            }
        });



    }


    public static void playSound(String soundFilePath, int startFromMillis) {
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.setMicrosecondPosition(startFromMillis * 1000);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopSound() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }


    public static void displayGif(JPanel panel, String gifFilePath) {

        try {
            ImageIcon icon = new ImageIcon("D:\\Java\\Java_Project\\7b7f.gif");
            JLabel label = new JLabel(icon);

            panel.removeAll();
            panel.add(label);
            JButton processButton_e = new JButton("Остановить");
            panel.add(processButton_e);

            JButton processButton_O = new JButton("БОЛЬШЕ МУЗЫКИ");
            panel.add(processButton_O);

            processButton_O.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String filepath_ = "D:\\Java\\Java_Project\\Aq.gif";

                    displayGif(panel , filepath_);


                    if(n == 0){
                        stopSound();
                        playSound("D:\\Java\\Java_Project\\rick-astley-never-gonna-give-you-up.wav" , 0);
                        n++;
                    }else if(n == 1){
                        stopSound();
                        playSound("D:\\Java\\Java_Project\\Mr President – Coco Jamboo.wav" , 57300);
                        n++;
                    }else if(n == 2){
                        stopSound();
                        playSound("D:\\Java\\Java_Project\\homie_-_bezumno-mozhno-byt-pervym.wav" , 90000);
                        n++;
                    }else if(n == 3){
                        stopSound();
                        playSound("D:\\Java\\Java_Project\\Bury The Light (Mission 20).wav" , 0);
                        n = 0;
                    }

                    timer.start();

                }


                Timer timer = new Timer(5000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Color randomColor = new Color(
                                (int) (Math.random() * 256),
                                (int) (Math.random() * 256),
                                (int) (Math.random() * 256)
                        );


                        Color randomColor_ = new Color(
                                (int) (Math.random() * 256),
                                (int) (Math.random() * 256),
                                (int) (Math.random() * 256)
                        );

                        panel.setBackground(randomColor);

                        outputArea.setBackground(randomColor_);
                    }
                });
            });


            processButton_e.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            panel.revalidate();
            panel.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void EncryptFile() {
        String fileName = fileNameField.getText();
        FileEncryptor fileEncryptor = new FileEncryptor.Builder(fileName)
                .build();

        fileEncryptor.encryptFile(fileName, "en_" + fileName);
        outputArea.setText("Файл успешно зашифрован .");
    }




    private void DecryptFile() {
        String fileName = fileNameField.getText();
        FileEncryptor fileDecryptor = new FileEncryptor.Builder(fileName)
                .build();

        fileDecryptor.decryptFile(fileName , "de_" + fileName);
        outputArea.setText("Файл успешно расшифрован .");

    }

    void archiveFile(String outputFileName) {
        String fileName = fileNameField.getText();
        boolean isArchived = isArchived(fileName);

        archiver archiver = new archiver.Builder(fileName)
                .isArchived(isArchived)
                .build();

        archiver.archiveData(fileName , outputFileName);
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

        //System.out.println(fileName);

        //Parser.parseAndProcessFile(fileName, outputFileName_);

        outputArea.setText(" Входящий файл : \n" + Parser.parseAndProcessFile(fileName, outputFileName_));
    }

    static boolean isArchived(String fileName) {
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