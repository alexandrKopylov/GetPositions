package com.company;

import com.company.logic.MultiValueHashMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Main {
    static String pathDXF = "c:\\Users\\alexx.STALMOST\\Desktop\\_DXF\\";
    // static Path pathDXF = Path.of("C:\\Users\\user\\Desktop\\dxf\\dxf\\dxf");


    static File fileCSV = new File("c:\\Users\\alexx.STALMOST\\Desktop\\ДеталиБК_все.csv");
    // static File fileCSV = new File("C:\\Users\\user\\Desktop\\dxf\\dxf\\ДеталиБК_все.csv");

    //  z:\DXF\
    static Path pathToDxfOnServer = Path.of("z:\\DXF\\");
    private static JTextField textFieldKK;
    private static JTextField textFieldSteelGrade;
    private static JTextArea textArea;
    private static JTextArea textAreaPDF;
    private static JComboBox<String> cbThickness;
    private static JComboBox<String> cbMetalGrade;

    static String[] elements = {"  толщина  ", "1", "2", "2.5", "3", "4", "5", "6", "8", "10", "12", "14", "16", "18", "20", "22", "25","28", "30"};
    static String[] elementsMarkGrade = {" марка стали ", "С235", "С245", "С255", "С345", "С355", "С390", "С440", "10Х2", "10Х3", "15Х2", "15Х3", "09Г2С", "Ст3"};
    private static List<String> listPoziciiPlusPodkroi;


    public static void main(String[] args) {
        Main main = new Main();
        JPanel panel = new JPanel();

        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);

        JPanel panelTextFields = new JPanel();
        panelTextFields.setLayout(new FlowLayout());

        textFieldKK = new JTextField("KK", 10);
        panelTextFields.add(textFieldKK);

        // JLabel pustishka = new JLabel("                      ");
        // panelTextFields.add(pustishka);

        // Модель данных списка
        DefaultComboBoxModel<String> cbModel = new DefaultComboBoxModel<>();
        for (String element : elements) {
            cbModel.addElement(element);
        }
        cbThickness = new JComboBox<>(cbModel);
        panelTextFields.add(cbThickness);

        // JLabel pustishka2 = new JLabel("                      ");
        // panelTextFields.add(pustishka2);

        DefaultComboBoxModel<String> cbModelMetalGrade = new DefaultComboBoxModel<>();
        for (String element : elementsMarkGrade) {
            cbModelMetalGrade.addElement(element);
        }
        cbMetalGrade = new JComboBox<>(cbModelMetalGrade);
        panelTextFields.add(cbMetalGrade);

        JLabel pustishka3 = new JLabel("                      ");
        panelTextFields.add(pustishka3);


        JButton buttonStart = new JButton("START");
        panelTextFields.add(buttonStart);


        JButton buttonPodkroi = new JButton("Подкрой");
        panelTextFields.add(buttonPodkroi);


        panel.add(panelTextFields);

        textArea = new JTextArea(100, 20);
        textArea.setFont(new Font("LucidaSans", Font.PLAIN, 16));
        final JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane);


// создаем панел для 2ой  вкладки
        JPanel panelPDF = new JPanel();
        BoxLayout boxLayout2 = new BoxLayout(panelPDF, BoxLayout.Y_AXIS);
        panelPDF.setLayout(boxLayout2);

        textAreaPDF = new JTextArea(100, 20);
        textAreaPDF.setFont(new Font("LucidaSans", Font.PLAIN, 16));
        final JScrollPane scrollPanePDF = new JScrollPane(textAreaPDF);
        scrollPanePDF.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanePDF.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        panelPDF.add(scrollPanePDF);


        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("вкладка", panel);
        tabbedPane.add("parsing PDF ", panelPDF);

        JFrame frame = new JFrame("получить геометрию из CSV- файла ");
        frame.setContentPane(tabbedPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLocation(500, 50);
        frame.setVisible(true);

        buttonPodkroi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getSource() == buttonPodkroi) {
                    try {
                        String thiknes = (String) cbThickness.getSelectedItem();
                        if (thiknes.equals("  толщина  ")) {
                            thiknes = "";
                        }

                        String gradeSteel = (String) cbMetalGrade.getSelectedItem();
                        if (gradeSteel.equals(" марка стали ")) {
                            gradeSteel = "09Г2С";
                        }

                        String textKK = textFieldKK.getText();
                        textArea.setText("");

                        Util util = new Util();
                        MultiValueHashMap<String, String> map = util.readCSVPlusPodkroi(fileCSV, thiknes, textArea, listPoziciiPlusPodkroi, textAreaPDF);
                        if (map.countOfValue() == 0) {
                            textArea.append("\n\n\n        фвйл ДеталиБК_все.csv НЕ ОБНОВЛЁН");
                            return;
                        }
                        util.savePozsPodkroi(map);
                        util.addPozPodkroiToList(map, listPoziciiPlusPodkroi);


                        //  FileWriter fileORD = new FileWriter("c:\\Users\\alexx.STALMOST\\Desktop\\_DXF\\fileORD.Ord", StandardCharsets.UTF_16LE, true);
                        // PrintWriter writer = new PrintWriter(FILE_PATH);
                        // writer.print("");
// other operations
                        // writer.close();


                        new FileWriter("c:\\Users\\alexx.STALMOST\\Desktop\\_DXF\\fileORD.Ord", false).close();


                        List<String> listFileName = util.getListFileName(pathDXF);

                        int countTotalPoz = map.countOfValue();
                        textArea.append("-".repeat(300) + "\n");
                        textArea.append("количество позиций в CSV файле : " + countTotalPoz + "\n");
                        textArea.append("\n");

                        Set<String> lisPozNeNashel = new HashSet<>();

                        lisPozNeNashel = util.getListPoz(map, pathDXF, textArea, gradeSteel, panel, textKK, textAreaPDF);

                        int neNashlos = lisPozNeNashel.size();


                        textArea.append("      нашлось файлов : " + (countTotalPoz - neNashlos) + "\n");
                        textArea.append("НЕ нашлось файлов : " + neNashlos + "\n");
                        int count = 0;
                        for (String str : lisPozNeNashel) {
                            textArea.append(++count + " ) " + str + "\n");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getSource() == buttonStart) {
                    try {
                        clearFolderDXF();


                        String thiknes = (String) cbThickness.getSelectedItem();
                        if (thiknes.equals("  толщина  ")) {
                            thiknes = "";
                        }

                        String gradeSteel = (String) cbMetalGrade.getSelectedItem();
                        if (gradeSteel.equals(" марка стали ")) {
                            gradeSteel = "09Г2С";
                        }

                        String textKK = textFieldKK.getText();


                        Util util = new Util();
                        listPoziciiPlusPodkroi = util.getListPozFromCSV(fileCSV);

                        textArea.setText("");
                        MultiValueHashMap<String, String> map = null;

                        map = util.readCSV(fileCSV, thiknes, textArea, textAreaPDF);

                        int countTotalPoz = map.countOfValue();
                        textArea.append("-".repeat(300) + "\n");
                        textArea.append("количество позиций в CSV файле : " + countTotalPoz + "\n");
                        textArea.append("\n");

                        int countSuccess = 0;
                        Set<String> lisPozNeNashel = new HashSet<>();

                        lisPozNeNashel = util.getListPoz(map, pathDXF, textArea, gradeSteel, panel, textKK, textAreaPDF);

                        int neNashlos = lisPozNeNashel.size();


                        textArea.append("      нашлось файлов : " + (countTotalPoz - neNashlos) + "\n");
                        textArea.append("НЕ нашлось файлов : " + neNashlos + "\n");

                        int count = 0;
                        for (String str : lisPozNeNashel) {
                            textArea.append(++count + " ) " + str + "\n");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


//
//        Util util = new Util();
//        MultiValueHashMap<String, String>  map = util.readCSV(fileCSV);
//        textArea.append("количество позиций в CSV файле : " + map.countOfValue() + "\n");
//        int countSuccess  = util.getListPoz(map, pathDXF);
//        textArea.append("      нашлось файлов : " + countSuccess + "\n");
//        textArea.append("НЕ нашлось файлов : "+ (map.countOfValue()-countSuccess)+ "\n");


    }

    private static void clearFolderDXF() {
        File directory = new File(pathDXF);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }


}
