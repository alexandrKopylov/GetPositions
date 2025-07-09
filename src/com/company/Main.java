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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Main {
    static String pathDXF = "c:\\Users\\alexx.STALMOST\\Desktop\\_DXF\\";
    // static Path pathDXF = Path.of("C:\\Users\\user\\Desktop\\dxf\\dxf\\dxf");


    static File fileCSV = new File("c:\\Users\\alexx.STALMOST\\Desktop\\ДеталиБК_все.csv");
    // static File fileCSV = new File("C:\\Users\\user\\Desktop\\dxf\\dxf\\ДеталиБК_все.csv");

    //  z:\DXF\
    static Path pathToDxfOnServer = Path.of("z:\\DXF\\");
     static JTextField textFieldKK;
    private static JTextField textFieldVozvrat;
    private static JTextField textFieldSteelGrade;
    private static JTextArea textArea;
    private static JTextArea textAreaPDF;
    private static JComboBox<String> cbThickness;
    private static JComboBox<String> cbMetalGrade;
     static JCheckBox checkbox;

    static String[] elements = {"  толщина  ", "1", "2", "2.5", "3", "4", "5", "6", "8", "10", "12", "14", "16", "18", "20", "22", "25", "28", "30"};
    static String[] elementsMarkGrade = {" марка стали ", "С235", "С245", "С255", "С345", "С355", "С390", "С440", "10Х2", "10Х3", "15Х2", "15Х3", "09Г2С", "Ст3"};
    private static List<String> listPoziciiPlusPodkroi = new ArrayList<>();
    static Set<String> identicalPozAndInv = new HashSet<>();


    public static void main(String[] args) {
        Main main = new Main();
        JPanel panel = new JPanel();

        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);

        JPanel panelTextFields = new JPanel();
        panelTextFields.setLayout(new FlowLayout());


       // JLabel flagName = new JLabel("name only poz");
        //panelTextFields.add(flagName);


        checkbox = new JCheckBox("name only poz",true);
        panelTextFields.add(checkbox);

        JLabel flagName2 = new JLabel("                    ");
        panelTextFields.add(flagName2);





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


       // JButton buttonStart = new JButton("START");
       // panelTextFields.add(buttonStart);


        JButton buttonPodkroi = new JButton("Start + P");
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

// создаем панел для 3ой  вкладки  возврат
        JPanel panelVozvrat = new JPanel();

        BoxLayout boxLayoutVozvrat = new BoxLayout(panelVozvrat, BoxLayout.Y_AXIS);
        panelVozvrat.setLayout(boxLayoutVozvrat);

        JPanel panelTextFieldsVozvrat = new JPanel();
        panelTextFieldsVozvrat.setLayout(new FlowLayout());
        textFieldVozvrat = new JTextField("возврат", 30);
        panelTextFieldsVozvrat.add(textFieldVozvrat);
        JButton buttonVozvrat = new JButton("Drowing");
        panelTextFieldsVozvrat.add(buttonVozvrat);

        panelVozvrat.add(panelTextFieldsVozvrat);


        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("вкладка", panel);
        tabbedPane.add("parsing PDF ", panelPDF);
        tabbedPane.add("возврат ", panelVozvrat);


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


        buttonVozvrat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getSource() == buttonVozvrat) {

                    String textVozvrat = textFieldVozvrat.getText();
                    String[] parametrs = parseTextVozvrat(textVozvrat);


                    try {
                        Files.writeString(
                                Paths.get("c:\\Users\\alexx.STALMOST\\Desktop\\vozvrat.dxf"),
                                sapog(parametrs[0], parametrs[1], parametrs[2], parametrs[3]) ,
                                Charset.forName("windows-1251")
                        );

                        JOptionPane.showMessageDialog(null, "ok");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
            }
            }
        });

        /*
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
*/

//
//        Util util = new Util();
//        MultiValueHashMap<String, String>  map = util.readCSV(fileCSV);
//        textArea.append("количество позиций в CSV файле : " + map.countOfValue() + "\n");
//        int countSuccess  = util.getListPoz(map, pathDXF);
//        textArea.append("      нашлось файлов : " + countSuccess + "\n");
//        textArea.append("НЕ нашлось файлов : "+ (map.countOfValue()-countSuccess)+ "\n");


    }

    private static String sapog(String razmerA, String razmerB, String razmerC, String razmerD) {

        String str = "  0\n" +
                "SECTION\n" +
                "  2\n" +
                "HEADER\n" +
                "  9\n" +
                "$ACADVER\n" +
                "  1\n" +
                "AC1009\n" +
                "  9\n" +
                "$INSBASE\n" +
                " 10\n" +
                "0.0\n" +
                " 20\n" +
                "0.0\n" +
                " 30\n" +
                "0.0\n" +
                "  9\n" +
                "$EXTMIN\n" +
                " 10\n" +
                "0.0\n" +
                " 20\n" +
                "0.0\n" +
                " 30\n" +
                "0.0\n" +
                "  9\n" +
                "$EXTMAX\n" +
                " 10\n" +
                "12040.0\n" +
                " 20\n" +
                "2500.0\n" +
                " 30\n" +
                "0.0\n" +
                "  9\n" +
                "$LIMMIN\n" +
                " 10\n" +
                "0.0\n" +
                " 20\n" +
                "0.0\n" +
                "  9\n" +
                "$LIMMAX\n" +
                " 10\n" +
                "12.0\n" +
                " 20\n" +
                "9.0\n" +
                "  9\n" +
                "$ORTHOMODE\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$REGENMODE\n" +
                " 70\n" +
                "     1\n" +
                "  9\n" +
                "$FILLMODE\n" +
                " 70\n" +
                "     1\n" +
                "  9\n" +
                "$QTEXTMODE\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$MIRRTEXT\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$DRAGMODE\n" +
                " 70\n" +
                "     2\n" +
                "  9\n" +
                "$LTSCALE\n" +
                " 40\n" +
                "1.0\n" +
                "  9\n" +
                "$OSMODE\n" +
                " 70\n" +
                "    53\n" +
                "  9\n" +
                "$ATTMODE\n" +
                " 70\n" +
                "     1\n" +
                "  9\n" +
                "$TEXTSIZE\n" +
                " 40\n" +
                "0.2\n" +
                "  9\n" +
                "$TRACEWID\n" +
                " 40\n" +
                "0.05\n" +
                "  9\n" +
                "$TEXTSTYLE\n" +
                "  7\n" +
                "STANDARD\n" +
                "  9\n" +
                "$CLAYER\n" +
                "  8\n" +
                "0\n" +
                "  9\n" +
                "$CELTYPE\n" +
                "  6\n" +
                "BYLAYER\n" +
                "  9\n" +
                "$CECOLOR\n" +
                " 62\n" +
                "   256\n" +
                "  9\n" +
                "$DIMSCALE\n" +
                " 40\n" +
                "1.0\n" +
                "  9\n" +
                "$DIMASZ\n" +
                " 40\n" +
                "0.18\n" +
                "  9\n" +
                "$DIMEXO\n" +
                " 40\n" +
                "0.0625\n" +
                "  9\n" +
                "$DIMDLI\n" +
                " 40\n" +
                "0.38\n" +
                "  9\n" +
                "$DIMRND\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$DIMDLE\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$DIMEXE\n" +
                " 40\n" +
                "0.18\n" +
                "  9\n" +
                "$DIMTP\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$DIMTM\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$DIMTXT\n" +
                " 40\n" +
                "0.18\n" +
                "  9\n" +
                "$DIMCEN\n" +
                " 40\n" +
                "0.09\n" +
                "  9\n" +
                "$DIMTSZ\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$DIMTOL\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$DIMLIM\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$DIMTIH\n" +
                " 70\n" +
                "     1\n" +
                "  9\n" +
                "$DIMTOH\n" +
                " 70\n" +
                "     1\n" +
                "  9\n" +
                "$DIMSE1\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$DIMSE2\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$DIMTAD\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$DIMZIN\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$DIMBLK\n" +
                "  1\n" +
                "\n" +
                "  9\n" +
                "$DIMASO\n" +
                " 70\n" +
                "     1\n" +
                "  9\n" +
                "$DIMSHO\n" +
                " 70\n" +
                "     1\n" +
                "  9\n" +
                "$DIMPOST\n" +
                "  1\n" +
                "\n" +
                "  9\n" +
                "$DIMAPOST\n" +
                "  1\n" +
                "\n" +
                "  9\n" +
                "$DIMALT\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$DIMALTD\n" +
                " 70\n" +
                "     2\n" +
                "  9\n" +
                "$DIMALTF\n" +
                " 40\n" +
                "25.3999999999999986\n" +
                "  9\n" +
                "$DIMLFAC\n" +
                " 40\n" +
                "1.0\n" +
                "  9\n" +
                "$DIMTOFL\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$DIMTVP\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$DIMTIX\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$DIMSOXD\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$DIMSAH\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$DIMBLK1\n" +
                "  1\n" +
                "\n" +
                "  9\n" +
                "$DIMBLK2\n" +
                "  1\n" +
                "\n" +
                "  9\n" +
                "$DIMSTYLE\n" +
                "  2\n" +
                "STANDARD\n" +
                "  9\n" +
                "$DIMCLRD\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$DIMCLRE\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$DIMCLRT\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$DIMTFAC\n" +
                " 40\n" +
                "1.0\n" +
                "  9\n" +
                "$DIMGAP\n" +
                " 40\n" +
                "0.09\n" +
                "  9\n" +
                "$LUNITS\n" +
                " 70\n" +
                "     2\n" +
                "  9\n" +
                "$LUPREC\n" +
                " 70\n" +
                "     4\n" +
                "  9\n" +
                "$SKETCHINC\n" +
                " 40\n" +
                "0.1\n" +
                "  9\n" +
                "$FILLETRAD\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$AUNITS\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$AUPREC\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$MENU\n" +
                "  1\n" +
                ".\n" +
                "  9\n" +
                "$ELEVATION\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$PELEVATION\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$THICKNESS\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$LIMCHECK\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$BLIPMODE\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$CHAMFERA\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$CHAMFERB\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$SKPOLY\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$TDCREATE\n" +
                " 40\n" +
                "2460563.3669791668653488\n" +
                "  9\n" +
                "$TDUPDATE\n" +
                " 40\n" +
                "2460563.3681828710250556\n" +
                "  9\n" +
                "$TDINDWG\n" +
                " 40\n" +
                "0.0012037037\n" +
                "  9\n" +
                "$TDUSRTIMER\n" +
                " 40\n" +
                "0.0012037037\n" +
                "  9\n" +
                "$USRTIMER\n" +
                " 70\n" +
                "     1\n" +
                "  9\n" +
                "$ANGBASE\n" +
                " 50\n" +
                "0.0\n" +
                "  9\n" +
                "$ANGDIR\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$PDMODE\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$PDSIZE\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$PLINEWID\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$COORDS\n" +
                " 70\n" +
                "     1\n" +
                "  9\n" +
                "$SPLFRAME\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$SPLINETYPE\n" +
                " 70\n" +
                "     6\n" +
                "  9\n" +
                "$SPLINESEGS\n" +
                " 70\n" +
                "     8\n" +
                "  9\n" +
                "$ATTDIA\n" +
                " 70\n" +
                "     1\n" +
                "  9\n" +
                "$ATTREQ\n" +
                " 70\n" +
                "     1\n" +
                "  9\n" +
                "$HANDLING\n" +
                " 70\n" +
                "     1\n" +
                "  9\n" +
                "$HANDSEED\n" +
                "  5\n" +
                "299\n" +
                "  9\n" +
                "$SURFTAB1\n" +
                " 70\n" +
                "     6\n" +
                "  9\n" +
                "$SURFTAB2\n" +
                " 70\n" +
                "     6\n" +
                "  9\n" +
                "$SURFTYPE\n" +
                " 70\n" +
                "     6\n" +
                "  9\n" +
                "$SURFU\n" +
                " 70\n" +
                "     6\n" +
                "  9\n" +
                "$SURFV\n" +
                " 70\n" +
                "     6\n" +
                "  9\n" +
                "$UCSNAME\n" +
                "  2\n" +
                "\n" +
                "  9\n" +
                "$UCSORG\n" +
                " 10\n" +
                "0.0\n" +
                " 20\n" +
                "0.0\n" +
                " 30\n" +
                "0.0\n" +
                "  9\n" +
                "$UCSXDIR\n" +
                " 10\n" +
                "1.0\n" +
                " 20\n" +
                "0.0\n" +
                " 30\n" +
                "0.0\n" +
                "  9\n" +
                "$UCSYDIR\n" +
                " 10\n" +
                "0.0\n" +
                " 20\n" +
                "1.0\n" +
                " 30\n" +
                "0.0\n" +
                "  9\n" +
                "$PUCSNAME\n" +
                "  2\n" +
                "\n" +
                "  9\n" +
                "$PUCSORG\n" +
                " 10\n" +
                "0.0\n" +
                " 20\n" +
                "0.0\n" +
                " 30\n" +
                "0.0\n" +
                "  9\n" +
                "$PUCSXDIR\n" +
                " 10\n" +
                "1.0\n" +
                " 20\n" +
                "0.0\n" +
                " 30\n" +
                "0.0\n" +
                "  9\n" +
                "$PUCSYDIR\n" +
                " 10\n" +
                "0.0\n" +
                " 20\n" +
                "1.0\n" +
                " 30\n" +
                "0.0\n" +
                "  9\n" +
                "$USERI1\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$USERI2\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$USERI3\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$USERI4\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$USERI5\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$USERR1\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$USERR2\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$USERR3\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$USERR4\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$USERR5\n" +
                " 40\n" +
                "0.0\n" +
                "  9\n" +
                "$WORLDVIEW\n" +
                " 70\n" +
                "     1\n" +
                "  9\n" +
                "$SHADEDGE\n" +
                " 70\n" +
                "     3\n" +
                "  9\n" +
                "$SHADEDIF\n" +
                " 70\n" +
                "    70\n" +
                "  9\n" +
                "$TILEMODE\n" +
                " 70\n" +
                "     1\n" +
                "  9\n" +
                "$MAXACTVP\n" +
                " 70\n" +
                "    64\n" +
                "  9\n" +
                "$PLIMCHECK\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$PEXTMIN\n" +
                " 10\n" +
                "0.0\n" +
                " 20\n" +
                "0.0\n" +
                " 30\n" +
                "0.0\n" +
                "  9\n" +
                "$PEXTMAX\n" +
                " 10\n" +
                "0.0\n" +
                " 20\n" +
                "0.0\n" +
                " 30\n" +
                "0.0\n" +
                "  9\n" +
                "$PLIMMIN\n" +
                " 10\n" +
                "0.0\n" +
                " 20\n" +
                "0.0\n" +
                "  9\n" +
                "$PLIMMAX\n" +
                " 10\n" +
                "12.0\n" +
                " 20\n" +
                "9.0\n" +
                "  9\n" +
                "$UNITMODE\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$VISRETAIN\n" +
                " 70\n" +
                "     1\n" +
                "  9\n" +
                "$PLINEGEN\n" +
                " 70\n" +
                "     0\n" +
                "  9\n" +
                "$PSLTSCALE\n" +
                " 70\n" +
                "     1\n" +
                "  0\n" +
                "ENDSEC\n" +
                "  0\n" +
                "SECTION\n" +
                "  2\n" +
                "TABLES\n" +
                "  0\n" +
                "TABLE\n" +
                "  2\n" +
                "VPORT\n" +
                " 70\n" +
                "     1\n" +
                "  0\n" +
                "VPORT\n" +
                "  2\n" +
                "*ACTIVE\n" +
                " 70\n" +
                "     0\n" +
                " 10\n" +
                "0.0\n" +
                " 20\n" +
                "0.0\n" +
                " 11\n" +
                "1.0\n" +
                " 21\n" +
                "1.0\n" +
                " 12\n" +
                "6019.9999999999990905\n" +
                " 22\n" +
                "1250.0\n" +
                " 13\n" +
                "0.0\n" +
                " 23\n" +
                "0.0\n" +
                " 14\n" +
                "0.5\n" +
                " 24\n" +
                "0.5\n" +
                " 15\n" +
                "0.5\n" +
                " 25\n" +
                "0.5\n" +
                " 16\n" +
                "0.0\n" +
                " 26\n" +
                "0.0\n" +
                " 36\n" +
                "1.0\n" +
                " 17\n" +
                "0.0\n" +
                " 27\n" +
                "0.0\n" +
                " 37\n" +
                "0.0\n" +
                " 40\n" +
                "9592.5100374345456657\n" +
                " 41\n" +
                "1.6803185437997721\n" +
                " 42\n" +
                "50.0\n" +
                " 43\n" +
                "0.0\n" +
                " 44\n" +
                "0.0\n" +
                " 50\n" +
                "0.0\n" +
                " 51\n" +
                "0.0\n" +
                " 71\n" +
                "     0\n" +
                " 72\n" +
                "  1000\n" +
                " 73\n" +
                "     1\n" +
                " 74\n" +
                "     3\n" +
                " 75\n" +
                "     0\n" +
                " 76\n" +
                "     0\n" +
                " 77\n" +
                "     0\n" +
                " 78\n" +
                "     0\n" +
                "  0\n" +
                "ENDTAB\n" +
                "  0\n" +
                "TABLE\n" +
                "  2\n" +
                "LTYPE\n" +
                " 70\n" +
                "     1\n" +
                "  0\n" +
                "LTYPE\n" +
                "  2\n" +
                "CONTINUOUS\n" +
                " 70\n" +
                "     0\n" +
                "  3\n" +
                "Solid line\n" +
                " 72\n" +
                "    65\n" +
                " 73\n" +
                "     0\n" +
                " 40\n" +
                "0.0\n" +
                "  0\n" +
                "ENDTAB\n" +
                "  0\n" +
                "TABLE\n" +
                "  2\n" +
                "LAYER\n" +
                " 70\n" +
                "     1\n" +
                "  0\n" +
                "LAYER\n" +
                "  2\n" +
                "0\n" +
                " 70\n" +
                "     0\n" +
                " 62\n" +
                "     7\n" +
                "  6\n" +
                "CONTINUOUS\n" +
                "  0\n" +
                "ENDTAB\n" +
                "  0\n" +
                "TABLE\n" +
                "  2\n" +
                "STYLE\n" +
                " 70\n" +
                "     2\n" +
                "  0\n" +
                "STYLE\n" +
                "  2\n" +
                "STANDARD\n" +
                " 70\n" +
                "     0\n" +
                " 40\n" +
                "0.0\n" +
                " 41\n" +
                "1.0\n" +
                " 50\n" +
                "0.0\n" +
                " 71\n" +
                "     0\n" +
                " 42\n" +
                "0.2\n" +
                "  3\n" +
                "txt\n" +
                "  4\n" +
                "\n" +
                "  0\n" +
                "STYLE\n" +
                "  2\n" +
                "АННОТАТИВНЫЙ\n" +                  //  "АННОТАТИВНЫЙ\n" +
                " 70\n" +
                "     0\n" +
                " 40\n" +
                "0.0\n" +
                " 41\n" +
                "1.0\n" +
                " 50\n" +
                "0.0\n" +
                " 71\n" +
                "     0\n" +
                " 42\n" +
                "0.2\n" +
                "  3\n" +
                "txt\n" +
                "  4\n" +
                "\n" +
                "  0\n" +
                "ENDTAB\n" +
                "  0\n" +
                "TABLE\n" +
                "  2\n" +
                "VIEW\n" +
                " 70\n" +
                "     0\n" +
                "  0\n" +
                "ENDTAB\n" +
                "  0\n" +
                "TABLE\n" +
                "  2\n" +
                "UCS\n" +
                " 70\n" +
                "     0\n" +
                "  0\n" +
                "ENDTAB\n" +
                "  0\n" +
                "TABLE\n" +
                "  2\n" +
                "APPID\n" +
                " 70\n" +
                "     7\n" +
                "  0\n" +
                "APPID\n" +
                "  2\n" +
                "ACAD\n" +
                " 70\n" +
                "     0\n" +
                "  0\n" +
                "APPID\n" +
                "  2\n" +
                "ACADANNOPO\n" +
                " 70\n" +
                "     0\n" +
                "  0\n" +
                "APPID\n" +
                "  2\n" +
                "ACADANNOTATIVE\n" +
                " 70\n" +
                "     0\n" +
                "  0\n" +
                "APPID\n" +
                "  2\n" +
                "ACAD_DSTYLE_DIMJAG\n" +
                " 70\n" +
                "     0\n" +
                "  0\n" +
                "APPID\n" +
                "  2\n" +
                "ACAD_DSTYLE_DIMTALN\n" +
                " 70\n" +
                "     0\n" +
                "  0\n" +
                "APPID\n" +
                "  2\n" +
                "ACAD_MLEADERVER\n" +
                " 70\n" +
                "     0\n" +
                "  0\n" +
                "APPID\n" +
                "  2\n" +
                "ACAD_NAV_VCDISPLAY\n" +
                " 70\n" +
                "     0\n" +
                "  0\n" +
                "ENDTAB\n" +
                "  0\n" +
                "TABLE\n" +
                "  2\n" +
                "DIMSTYLE\n" +
                " 70\n" +
                "     2\n" +
                "  0\n" +
                "DIMSTYLE\n" +
                "  2\n" +
                "STANDARD\n" +
                " 70\n" +
                "     0\n" +
                "  3\n" +
                "\n" +
                "  4\n" +
                "\n" +
                "  5\n" +
                "\n" +
                "  6\n" +
                "\n" +
                "  7\n" +
                "\n" +
                " 40\n" +
                "1.0\n" +
                " 41\n" +
                "0.18\n" +
                " 42\n" +
                "0.0625\n" +
                " 43\n" +
                "0.38\n" +
                " 44\n" +
                "0.18\n" +
                " 45\n" +
                "0.0\n" +
                " 46\n" +
                "0.0\n" +
                " 47\n" +
                "0.0\n" +
                " 48\n" +
                "0.0\n" +
                "140\n" +
                "0.18\n" +
                "141\n" +
                "0.09\n" +
                "142\n" +
                "0.0\n" +
                "143\n" +
                "25.3999999999999986\n" +
                "144\n" +
                "1.0\n" +
                "145\n" +
                "0.0\n" +
                "146\n" +
                "1.0\n" +
                "147\n" +
                "0.09\n" +
                " 71\n" +
                "     0\n" +
                " 72\n" +
                "     0\n" +
                " 73\n" +
                "     1\n" +
                " 74\n" +
                "     1\n" +
                " 75\n" +
                "     0\n" +
                " 76\n" +
                "     0\n" +
                " 77\n" +
                "     0\n" +
                " 78\n" +
                "     0\n" +
                "170\n" +
                "     0\n" +
                "171\n" +
                "     2\n" +
                "172\n" +
                "     0\n" +
                "173\n" +
                "     0\n" +
                "174\n" +
                "     0\n" +
                "175\n" +
                "     0\n" +
                "176\n" +
                "     0\n" +
                "177\n" +
                "     0\n" +
                "178\n" +
                "     0\n" +
                "  0\n" +
                "DIMSTYLE\n" +
                "  2\n" +
                "АННОТАТИВНЫЙ\n" +                    // "АННОТАТИВНЫЙ\n" +
                " 70\n" +
                "     0\n" +
                "  3\n" +
                "\n" +
                "  4\n" +
                "\n" +
                "  5\n" +
                "\n" +
                "  6\n" +
                "\n" +
                "  7\n" +
                "\n" +
                " 40\n" +
                "0.0\n" +
                " 41\n" +
                "0.18\n" +
                " 42\n" +
                "0.0625\n" +
                " 43\n" +
                "0.38\n" +
                " 44\n" +
                "0.18\n" +
                " 45\n" +
                "0.0\n" +
                " 46\n" +
                "0.0\n" +
                " 47\n" +
                "0.0\n" +
                " 48\n" +
                "0.0\n" +
                "140\n" +
                "0.18\n" +
                "141\n" +
                "0.09\n" +
                "142\n" +
                "0.0\n" +
                "143\n" +
                "25.3999999999999986\n" +
                "144\n" +
                "1.0\n" +
                "145\n" +
                "0.0\n" +
                "146\n" +
                "1.0\n" +
                "147\n" +
                "0.09\n" +
                " 71\n" +
                "     0\n" +
                " 72\n" +
                "     0\n" +
                " 73\n" +
                "     1\n" +
                " 74\n" +
                "     1\n" +
                " 75\n" +
                "     0\n" +
                " 76\n" +
                "     0\n" +
                " 77\n" +
                "     0\n" +
                " 78\n" +
                "     0\n" +
                "170\n" +
                "     0\n" +
                "171\n" +
                "     2\n" +
                "172\n" +
                "     0\n" +
                "173\n" +
                "     0\n" +
                "174\n" +
                "     0\n" +
                "175\n" +
                "     0\n" +
                "176\n" +
                "     0\n" +
                "177\n" +
                "     0\n" +
                "178\n" +
                "     0\n" +
                "  0\n" +
                "ENDTAB\n" +
                "  0\n" +
                "ENDSEC\n" +
                "  0\n" +
                "SECTION\n" +
                "  2\n" +
                "BLOCKS\n" +
                "  0\n" +
                "BLOCK\n" +
                "  8\n" +
                "0\n" +
                "  2\n" +
                "$MODEL_SPACE\n" +
                " 70\n" +
                "     0\n" +
                " 10\n" +
                "0.0\n" +
                " 20\n" +
                "0.0\n" +
                " 30\n" +
                "0.0\n" +
                "  3\n" +
                "$MODEL_SPACE\n" +
                "  1\n" +
                "\n" +
                "  0\n" +
                "ENDBLK\n" +
                "  5\n" +
                "21\n" +
                "  8\n" +
                "0\n" +
                "  0\n" +
                "BLOCK\n" +
                " 67\n" +
                "     1\n" +
                "  8\n" +
                "0\n" +
                "  2\n" +
                "$PAPER_SPACE\n" +
                " 70\n" +
                "     0\n" +
                " 10\n" +
                "0.0\n" +
                " 20\n" +
                "0.0\n" +
                " 30\n" +
                "0.0\n" +
                "  3\n" +
                "$PAPER_SPACE\n" +
                "  1\n" +
                "\n" +
                "  0\n" +
                "ENDBLK\n" +
                "  5\n" +
                "5B\n" +
                " 67\n" +
                "     1\n" +
                "  8\n" +
                "0\n" +
                "  0\n" +
                "ENDSEC\n" +
                "  0\n" +
                "SECTION\n" +
                "  2\n" +
                "ENTITIES\n" +
                "  0\n";


        String str2 = "POLYLINE\n" +
                "  5\n" +
                "23F\n" +
                "  8\n" +
                "0\n" +
                " 66\n" +
                "     1\n" +
                " 10\n" +
                "0.0\n" +
                " 20\n" +
                "0.0\n" +
                " 30\n" +
                "0.0\n" +
                " 70\n" +
                "     1\n" +
                "  0\n" +
                "VERTEX\n" +
                "  5\n" +
                "292\n" +
                "  8\n" +
                "0\n" +
                " 10\n" +
                "0.0\n" +
                " 20\n" +
                "0.0\n" +
                " 30\n" +
                "0.0\n" +
                "  0\n" +
                "VERTEX\n" +
                "  5\n" +
                "293\n" +
                "  8\n" +
                "0\n" +
                " 10\n" +
                razmerB +           //B
                ".0\n" +
                " 20\n" +
                "0.0\n" +
                " 30\n" +
                "0.0\n" +
                "  0\n" +
                "VERTEX\n" +
                "  5\n" +
                "294\n" +
                "  8\n" +
                "0\n" +
                " 10\n" +
                razmerB +               //B
                ".0\n" +
                " 20\n" +
                razmerD +              //D
                ".0\n" +
                " 30\n" +
                "0.0\n" +
                "  0\n" +
                "VERTEX\n" +
                "  5\n" +
                "295\n" +
                "  8\n" +
                "0\n" +
                " 10\n" +
                razmerC +          //C
                ".0\n" +
                " 20\n" +
                razmerD +       //D
                ".0\n" +
                " 30\n" +
                "0.0\n" +
                "  0\n" +
                "VERTEX\n" +
                "  5\n" +
                "296\n" +
                "  8\n" +
                "0\n" +
                " 10\n" +
                razmerC +          //C
                ".0\n" +
                " 20\n" +
                razmerA +      //A
                ".0\n" +
                " 30\n" +
                "0.0\n" +
                "  0\n" +
                "VERTEX\n" +
                "  5\n" +
                "297\n" +
                "  8\n" +
                "0\n" +
                " 10\n" +
                "0.0\n" +
                " 20\n" +
                razmerA +       //A
                ".0\n" +
                " 30\n" +
                "0.0\n" +
                "  0\n" +
                "SEQEND\n" +
                "  5\n" +
                "298\n" +
                "  8\n" +
                "0\n" +
                "  0\n" +
                "ENDSEC\n" +
                "  0\n" +
                "EOF\n";
        return str + str2;


    }

    private static String[] parseTextVozvrat(String textVozvrat) {

        String[] masParametrs = new String[6];
        int chisloParametrs = -1;
        int dlinnaText = textVozvrat.length();
        String chislo = "";
        boolean numberExist = false;
        boolean beginNumber = false;
        int start = 0;
        int end = 0;

        for (int i = 0; i < textVozvrat.length(); i++) {
            char simvol = textVozvrat.charAt(i);
            if (Character.isDigit(simvol)) {
                if (!beginNumber) {
                    beginNumber = true;
                    start = i;
                    numberExist = true;
                }
            } else {
                beginNumber = false;
                end = i;
            }

            if (dlinnaText - 1 == i) {
                chislo = textVozvrat.substring(start, i + 1);
                masParametrs[++chisloParametrs] = chislo;
            }

            if (numberExist && !beginNumber) {
                numberExist = false;
                chislo = textVozvrat.substring(start, end);
                masParametrs[++chisloParametrs] = chislo;
            }
        }


        return masParametrs;
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
