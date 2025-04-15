package com.company;

import com.company.logic.MultiValueHashMap;
import com.company.logic.Point2D;
import com.company.logic.Rectangle;
import com.company.logic.TypeMark;
import com.company.modify.ModifyDXF;
import com.company.readPDF.ReadPDF;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.company.Main.identicalPozAndInv;

public class Util {
    private String pathDXF;
    private String serverBazaDXF = "z:\\DXF\\";
    private String pathMosinString = "\\\\nts2dc\\Мосин\\";
    private String fileCashPath = "c:\\Users\\alexx.STALMOST\\Desktop\\cashPath.txt";
    private JTextArea textArea;
    private JTextArea textAreaPDF;
    private JPanel panel;
    private String zakaz;
    private String inv;
    private String poz;
    private int kolvoPoz;
    private String textKK;
    private String gradeSteel;
    private String gabaritCSV;
    private boolean hasSpaces;
    private boolean searchPozFromMosin;
    private int result = 0;
    private int summaPoz = 0;
    private int lenghtDownLines = 0;
    private int lenghtUpLines = 0;
    private Integer countNeNashel = 0;
    private Integer countNashel = 0;


    private String markDefault;
    private double markDefault_X;
    private double markDefault_Y;
    private String stringEntityText;
    private double markDefaultHeight;

    private List<String> entityies = new LinkedList<>();
    private List<Point2D> listPointInsidePoz = new LinkedList<>();
    private MultiValueHashMap<String, String> fileNotFaundOnZakaz = new MultiValueHashMap<>();

    private Map<String, String> delListOnInv = new HashMap<>();
   // private Set<String> identicalPozAndInv = new HashSet<>();
    FileWriter fileORD = new FileWriter("c:\\Users\\alexx.STALMOST\\Desktop\\_DXF\\fileORD.Ord", StandardCharsets.UTF_16LE, true);
    Set<String> listPozNeNashel = new TreeSet<>();

    //PDF parser
    static final String pathToFolderPDF = "c:\\WorkFolder\\ReadOnly\\";
    static final Map<String, String> mapParsingPDF = new HashMap<>();


    public Util() throws IOException {
        //&&
    }


    public MultiValueHashMap<String, String> readCSVPlusPodkroi(File file, String thikness, JTextArea textArea, List<String> listPoziciiPlusPodkroi, JTextArea textAreaPDF) throws IOException {

        this.textAreaPDF = textAreaPDF;
        MultiValueHashMap<String, String> map = new MultiValueHashMap<>();
        List<String> listNamePozicii = new ArrayList<>();

        String[] mas = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String inv = "";
            String poz = "";
            String tolshina;


            while ((line = br.readLine()) != null) {
                if (line.equals("\t\t\t\t\t")
                        || line.equals("\t\t\t\t")
                        || line.equals("")
                        || line.equals("Инв.\tОбозначение\tКол.Т\tКол.Н\tГабариты\t")
                        || line.equals("Инв.\tОбозначение\tКол.Т\tКол.Н\tГабариты")) {
                    continue;
                }

                if (isLineInList(line, listPoziciiPlusPodkroi)) {
                    continue;
                }


                mas = line.split("\t");
                inv = mas[0];                         //.replace(".", "");                 //удаляем точку в инвентарном

                // poz = mas[1];

                if (thikness.equals("")) {             // нечего не ввели
                    map.put(inv, line);
                    textArea.append(line + "\n");
                    if (mas[1].contains("_")) {
                        listNamePozicii.add(mas[1].split("_")[0]);
                    }
                } else {
                    tolshina = mas[4].split("x")[0];
                    if (thikness.equals(tolshina)) {
                        map.put(inv, line);
                        textArea.append(line + "\n");
                        textArea.update(textArea.getGraphics());
                        // panel.revalidate();
                        if (mas[1].contains("_")) {
                            listNamePozicii.add(mas[1].split("_")[0]);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (map.countOfValue() != 0) {
            readPDF(listNamePozicii  /*, textAreaPDF*/);
        }
        return map;
    }

    private boolean isLineInList(String line, List<String> listPoziciiPlusPodkroi) {
        boolean result = false;
        for (String str : listPoziciiPlusPodkroi) {
            if (line.equals(str)) {
                result = true;
                break;
            }
        }
        return result;
    }


    public MultiValueHashMap<String, String> readCSV(File file, String thikness, JTextArea textArea, JTextArea textAreaPDF) throws IOException {

        MultiValueHashMap<String, String> map = new MultiValueHashMap<>();
        List<String> listNamePozicii = new ArrayList<>();


        String[] mas = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String inv = "";
            String poz = "";
            String tolshina;


            while ((line = br.readLine()) != null) {
                if (line.equals("\t\t\t\t\t")
                        || line.equals("\t\t\t\t")
                        || line.equals("")
                        || line.equals("Инв.\tОбозначение\tКол.Т\tКол.Н\tГабариты\t")
                        || line.equals("Инв.\tОбозначение\tКол.Т\tКол.Н\tГабариты")) {
                    continue;
                }
                mas = line.split("\t");
                inv = mas[0];                         //.replace(".", "");                 //удаляем точку в инвентарном

                // poz = mas[1];

                if (thikness.equals("")) {             // нечего не ввели
                    map.put(inv, line);
                    textArea.append(line + "\n");
                    if (mas[1].contains("_")) {
                        listNamePozicii.add(mas[1].split("_")[0]);
                    }

                } else {
                    tolshina = mas[4].split("x")[0];
                    if (thikness.equals(tolshina)) {
                        map.put(inv, line);
                        textArea.append(line + "\n");
                        textArea.update(textArea.getGraphics());
                        // panel.revalidate();
                        if (mas[1].contains("_")) {
                            listNamePozicii.add(mas[1].split("_")[0]);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        readPDF(listNamePozicii   /* ,textAreaPDF*/);

        return map;
    }

    private void readPDF(List<String> listNamePozicii   /*, JTextArea textAreaPDF*/) throws IOException {

        ReadPDF readPDF = new ReadPDF();
        List<String> listPDF = readPDF.searchPDF(pathToFolderPDF);
        String[] strMas;


        for (String str : listPDF) {
            String pathFilePDF = pathToFolderPDF + str;
            strMas = readPDF.readPDFtoString(pathFilePDF);
            // readPDF.serchInTextAndAddToMap(strMas, mapParsingPDF, ss);
            readPDF.serchInTextAndAddToMap2(strMas, mapParsingPDF, pathFilePDF, listNamePozicii, textAreaPDF);

            System.out.println("----------------------------------------------------------------------");
        }
        // readPDF.fileRename(pathToDxfFiles, keys);
    }


//        1917-22-КМД1-DP3.4	МД-34046_02258           CSV
//        1917-22-КМД1-DP34.mД-34046L_02258.dxf          DXF
//      1518913_1917-22-КМД1-DP3.4.mД-34046L.dxf          serv
//       1917-22-КМД1-DP34.mД-34046L.dxf                 search3

    public Set<String> getListPoz(MultiValueHashMap<String, String> mapa, String pathDXF, JTextArea textArea, String gradeSteel, JPanel panel, String textKK, JTextArea textAreaPDF) throws IOException {
        this.textArea = textArea;
        this.gradeSteel = gradeSteel;
        this.pathDXF = pathDXF;
        this.panel = panel;
        this.textKK = textKK;


        int countPozciii = 0;
        Set<String> set = mapa.kSet();
        for (String key : set) {
            System.out.println(key);
            List<String> listValue = mapa.get(key);
            for (String value : listValue) {
                String[] mas = value.split("\t");
                countPozciii += Integer.parseInt(mas[2]) + Integer.parseInt(mas[3]);
            }
        }


        fileORD.write("\uFEFF");
        fileORD.write("@M=0 @T=1.000000" + System.lineSeparator());
        fileORD.close();

        textAreaPDF.append("\n");
        textAreaPDF.append("********************* Parsing PDF *************************\n");
        mapParsingPDF.forEach((key, value) -> textAreaPDF.append(key + " = " + value + "\n"));
        textAreaPDF.append("mapParsingPDF.size() = " + mapParsingPDF.size() + "\n");
        textAreaPDF.append("\n");

        //   Set<String> set = mapa.kSet();
        File file;


        Map<String, String> fileNameAndCod = new HashMap<>();

        textArea.append("\n\n\n" + "*".repeat(300) + "\n");
        textArea.append("*".repeat(100) + "  ищем файлы прикрепленные к АРМу  " + "*".repeat(100) + "\n");
        textArea.append("*".repeat(300) + "\n");

        for (String inventar : set) {
            this.inv = inventar;

            file = new File(serverBazaDXF + inv);
            boolean folderExists = file.exists();

            if (!folderExists) {
                textArea.append("\nпапки не существует = " + file + "\n");
            } else {
                File[] masFiles = file.listFiles();
                String[] stringCodAndFileName;
                for (int i = 0; i < masFiles.length; i++) {                                     //        cod          fileName
                    stringCodAndFileName = masFiles[i].getName().split("_");           // format  417737_КСМ-РД-2-КМД1.2.06.p6F6
                    fileNameAndCod.put(stringCodAndFileName[1], stringCodAndFileName[0]);
                }
            }
            List<String> listValue = mapa.get(inv);

            for (String value : listValue) {
                boolean isFound = false;
                String[] mas = value.split("\t");

                poz = mas[1].split("_")[0];
                zakaz = mas[1].split("_")[1];
                gabaritCSV = mas[4];
                kolvoPoz = Integer.parseInt(mas[2]) + Integer.parseInt(mas[3]);             //2  b 3
                String search = null;
                String cod = null;

                if (folderExists) {
                    if (inventar.equals("-")) {
                        String pozz = poz.replace("-", "").replace(".", "");
                        search = pozz + "L.dxf";
                        cod = fileNameAndCod.get(search);
                        if (!(cod == null)) {
                            isFound = true;
                            copyFileInDXF(new File(serverBazaDXF + inv + "\\" + cod + "_" + search), pozz);
                            result++;
                        }
                    }
                    if (!isFound) {
                        search = inv + ".m" + poz + "L.dxf";
                        cod = fileNameAndCod.get(search);
                        if (!(cod == null)) {
                            isFound = true;
                            copyFileInDXF(new File(serverBazaDXF + inv + "\\" + cod + "_" + search), ".m" + poz);
                            result++;
                        }
                    }
                    if (!isFound) {
                        search = inv + ".p" + poz + "L.dxf";
                        cod = fileNameAndCod.get(search);
                        if (!(cod == null)) {
                            isFound = true;
                            copyFileInDXF(new File(serverBazaDXF + inv + "\\" + cod + "_" + search), ".p" + poz);
                            result++;
                        }
                    }

                    if (!isFound) {
                        search = inv + ".p" + inv + "." + poz + "L.dxf";
                        cod = fileNameAndCod.get(search);
                        if (!(cod == null)) {
                            isFound = true;
                            copyFileInDXF(new File(serverBazaDXF + inv + "\\" + cod + "_" + search), ".p" + poz);
                            result++;
                        }
                    }


                    if (!isFound) {
                        search = inv + ".m" + poz.substring(1) + "L.dxf";
                        cod = fileNameAndCod.get(search);
                        if (!(cod == null)) {
                            isFound = true;
                            copyFileInDXF(new File(serverBazaDXF + inv + "\\" + cod + "_" + search), ".m" + poz.substring(1));
                            result++;
                        }
                    }
                    if (!isFound) {
                        search = inv + ".m" + inv + "." + poz + "L.dxf";
                        cod = fileNameAndCod.get(search);
                        if (!(cod == null)) {
                            isFound = true;
                            copyFileInDXF(new File(serverBazaDXF + inv + "\\" + cod + "_" + search), ".m" + inv + "." + poz);
                            result++;
                        }
                    }
                }

                if (!isFound) {            // тогда  ищем файл  у Мосина
                    fileNotFaundOnZakaz.put(zakaz, poz + "_" + inv + "_" + gabaritCSV + "_" + kolvoPoz);
                    // fileNotFaundOnInv.put(inv, poz + "_" + zakaz + "_" + gabaritCSV + "_" + kolvoPoz);

                    //  todo мапа  fileNotFaundOnInv


                    delListOnInv.put(zakaz + "_" + poz, inv + "_" + gabaritCSV);


                    // TODO
                    // подумать  где обнулять  мапы
                }
            }
            fileNameAndCod.clear();
        }


        if (fileNotFaundOnZakaz.countOfValue() > 0 /*|| fileNotFaundOnInv.countOfValue() > 0   */) {
            textArea.append("\n\n\n" + "*".repeat(300) + "\n");
            textArea.append("*".repeat(100) + "  ищем файлы в папке Mosin  " + "*".repeat(100) + "\n");
            textArea.append("*".repeat(300) + "\n");

            //  printFindPoz(textArea);
            searchFileOnMosin();
        }
        fileORD.close();


        String str = String.format("kolvoCSV = %s  kolvoORD = %s", countPozciii, summaPoz);
        textArea.append("\n");
        textArea.append(str + "\n");
        textArea.append("\n");

        return listPozNeNashel;
    }

    private void printFindPoz() {
        Set<String> setZakaz = fileNotFaundOnZakaz.kSet();
        int count = 0;
        for (String key : setZakaz) {
            textArea.append(key + "\n");
            List<String> listValue = fileNotFaundOnZakaz.get(key);
            for (String value : listValue) {
                textArea.append("\t\t" + value + "\n");
            }
        }
        textArea.append("List file which we need find. Total size =  " + fileNotFaundOnZakaz.countOfValue() + "\n");
        textArea.append("\n\n");
    }

    private void searchFileOnMosin() throws IOException {
        // проверяем есть ли позиции с одинаковыми inv и  poz
        // если да то дабавляем ее в    identicalPozAndInv

        //  checkIdentical();

        FileWriter writer = new FileWriter(fileCashPath, true);
        searchPozFromZakaz(fileNotFaundOnZakaz, writer, delListOnInv, "заказ");

    }

//    private void checkIdentical() {
//
//
//        /* проверяет позмции с одинаковым инвентарным
//        и с одиновым позиция но с разным заказом
//
//                 */
//        Set<String> checkPoz = new HashSet<>();
//        String inv;
//        String poz;
//        Set<String> zakazPlusPozSet = delListOnInv.keySet();
//        for (String zakazPlusPoz : zakazPlusPozSet) {
//            inv = delListOnInv.get(zakazPlusPoz);
//            poz = zakazPlusPoz.split("_")[1];
//            if (!checkPoz.add(poz)) {
//                identicalPozAndInv.add(poz);
//            }
//        }
//    }


    /// todo searchPozFromZakaz
    private void searchPozFromZakaz(MultiValueHashMap<String, String> fileNotFaundOnZakaz, FileWriter writer, Map<String, String> delListOnInv, String zak) throws IOException {

        // int countNashel = 0;
        printFindPoz();

        Path pathMosin = Path.of(pathMosinString);
        Set<String> setZakaz = fileNotFaundOnZakaz.kSet();

        for (String strZakaz : setZakaz) {
            if (zak.equals("заказ")) {
                zakaz = strZakaz;
            } else {
                inv = strZakaz;
            }

            Map<String, Path> zakazPathMap = searchInCash();

            Path pathFolderZakaz = zakazPathMap.get(strZakaz);
            if (pathFolderZakaz == null) {

                String finalStrInvOrZakaz = strZakaz;
                List<Path> streamPath = Files.walk(pathMosin, 2, FileVisitOption.FOLLOW_LINKS)
                        .filter(Files::isDirectory)
                        .filter(x -> x.toFile().getName().contains(finalStrInvOrZakaz))
                        .collect(Collectors.toList());


                if (streamPath.size() == 0 && !Character.isDigit(strZakaz.charAt(strZakaz.length() - 1))) {
                    // RUS - >  LAT
                    String strZakazLeterChange = strZakaz.replace('А', 'A').replace('В', 'B').replace('С', 'C').replace('Е', 'E');

                    streamPath = Files.walk(pathMosin, 2, FileVisitOption.FOLLOW_LINKS)
                            .filter(Files::isDirectory)
                            .filter(x -> x.toFile().getName().contains(strZakazLeterChange))
                            .collect(Collectors.toList());
                }


                if (streamPath.size() > 1) {
                    int dlinnaStokiZakaz = strZakaz.length();
                    for (Path path : streamPath) {
                        String str = path.toString().trim();
                        int dlinnaStroki = str.length();
                        if (str.indexOf(strZakaz) + dlinnaStokiZakaz == dlinnaStroki) {
                            pathFolderZakaz = path;
                            break;
                        }
                    }
                } else if (streamPath.size() == 1) {
                    pathFolderZakaz = streamPath.get(0);
                } else {
                    boolean isLastCharIsDigitInZakaz = Character.isDigit(strZakaz.charAt(strZakaz.length() - 1));
                    if (!isLastCharIsDigitInZakaz) {
                        String strInvOrZakazWithoutLastChar = strZakaz.substring(0, strZakaz.length() - 1);

                        streamPath = Files.walk(pathMosin, 2, FileVisitOption.FOLLOW_LINKS)
                                .filter(Files::isDirectory)
                                .filter(x -> x.toFile().getName().contains(strInvOrZakazWithoutLastChar))
                                .collect(Collectors.toList());
                        if (streamPath.size() > 0) {
                            pathFolderZakaz = streamPath.get(0);
                        }
                        if (streamPath.size() == 0) {

                            streamPath = Files.walk(pathMosin, 2, FileVisitOption.FOLLOW_LINKS)
                                    .filter(Files::isDirectory)
                                    .filter(x -> x.toFile().getName().contains(inv))
                                    .collect(Collectors.toList());

                            if (streamPath.size() > 0) {
                                pathFolderZakaz = streamPath.get(0);
                            } else {

                                textArea.append("\nнет папки с  заказом " + strZakaz + "  у мосина");
                                List<String> vivod = fileNotFaundOnZakaz.get(strZakaz);
                                for (String sss : vivod) {
                                    textArea.append("\n\t" + ++countNeNashel + " ) " + sss.split("_")[0] + "    не нашел  \n");
                                }
                                continue;
                            }


                        }
                    } else {
                        streamPath = Files.walk(pathMosin, 2, FileVisitOption.FOLLOW_LINKS)
                                .filter(Files::isDirectory)
                                .filter(x -> x.toFile().getName().contains(inv))
                                .collect(Collectors.toList());

                        if (streamPath.size() > 0) {
                            pathFolderZakaz = streamPath.get(0);
                        } else {

                            textArea.append("\nнет папки с  заказом " + strZakaz + "  у мосина");
                            List<String> vivod = fileNotFaundOnZakaz.get(strZakaz);
                            for (String sss : vivod) {
                                textArea.append("\n\t" + ++countNeNashel + " ) " + sss.split("_")[0] + "    не нашел  \n");
                            }
                            continue;
                        }
                    }
                }
            }

            List<String> listPoz = fileNotFaundOnZakaz.get(strZakaz);
            Path pathFolderPoz = null;
            Path pathFilePoz = null;
            String nameFilePoz = null;


            //  определяем папку для кеша
            String pozStr = listPoz.get(0).split("_")[0];
            String finalPozStr = pozStr;
            List<Path> pozPathsList = Files.walk(pathFolderZakaz, 4, FileVisitOption.FOLLOW_LINKS)
                    .filter(Files::isRegularFile)
                    .filter(x -> x.toFile().getName().endsWith(".dxf"))
                    .filter(x -> x.toFile().getName().contains(finalPozStr))                             //equalsIgnoreCase(pozStr + ".dxf"))
                    .collect(Collectors.toList());


            boolean pozModify = false;                                              //  delete first char 'M'
            String search = null;

            if (pozPathsList.size() == 0) {
                search = modifyPozForSearch(pozStr);

                String finalSearch = search;
                pozPathsList = Files.walk(pathFolderZakaz, 4, FileVisitOption.FOLLOW_LINKS)
                        .filter(Files::isRegularFile)
                        .filter(x -> x.toFile().getName().endsWith(".dxf"))
                        .filter(x -> x.toFile().getName().contains(finalSearch))                             //equalsIgnoreCase(pozStr + ".dxf"))
                        .collect(Collectors.toList());
                if (pozPathsList.size() >= 1) {
                    pozModify = true;
                }
            }
            if (pozModify) {
                pozStr = modifyPozForSearch(pozStr);
            }

            boolean pozModify2 = false;                                                  //  change point(.) on  underscore(_)

            if (pozPathsList.size() == 0) {
                search = pozStr;
                search = search.replace(".", "_");

                String finalSearch1 = search;
                pozPathsList = Files.walk(pathFolderZakaz, 4, FileVisitOption.FOLLOW_LINKS)
                        .filter(Files::isRegularFile)
                        .filter(x -> x.toFile().getName().endsWith(".dxf"))
                        .filter(x -> x.toFile().getName().contains(finalSearch1))                             //equalsIgnoreCase(pozStr + ".dxf"))
                        .collect(Collectors.toList());

                if (pozPathsList.size() >= 1) {
                    pozModify2 = true;
                }
            }
            if (pozModify2) {
                pozStr = pozStr.replace(".", "_");
            }


            if (pozPathsList.size() >= 1) {
                pathFilePoz = checkExactMatch(pozStr, pozPathsList);
                pathFolderPoz = pathFilePoz.getParent();
                boolean isLastCharIsDigit = Character.isDigit(pathFolderPoz.toString().charAt(pathFolderPoz.toString().length() - 1));
                //   boolean isInvInListNoCash = ChekIsInvInListNoCash();
//                    if (!isLastCharIsDigit && !isInvInListNoCash) {
//                        writer.write(pathFolderPoz + "\n");
//                        writer.close();
//                    }


                fastMethod(pathFolderPoz, listPoz, strZakaz, zak);


                if (listPoz.size() != 0) {
                    METKA:
                    for (File file : pathFolderPoz.toFile().listFiles()) {
                        Iterator<String> it = listPoz.iterator();
                        while (it.hasNext()) {
                            razbiraemNaChasti(zak, it);

                            if (pozModify2) {

                                String sss = poz.replace(".", "_");
                                if (conteins(file.getName().replace(".dxf", ""), sss)) {
                                    copyFileToDxfFolder(delListOnInv, strZakaz, file, it, zak);
                                    if (listPoz.size() != 0) {
                                        break;
                                    } else {
                                        break METKA;
                                    }
                                }


                            } else if (pozModify) {
                                String sss = modifyPozForSearch(poz);
                                if (conteins(file.getName().replace(".dxf", ""), sss)) {
                                    copyFileToDxfFolder(delListOnInv, strZakaz, file, it, zak);
                                    if (listPoz.size() != 0) {
                                        break;
                                    } else {
                                        break METKA;
                                    }
                                }
                            } else {
                                if (conteins(file.getName().replace(".dxf", ""), poz)) {
                                    copyFileToDxfFolder(delListOnInv, strZakaz, file, it, zak);
                                    if (listPoz.size() != 0) {
                                        break;
                                    } else {
                                        break METKA;
                                    }
                                }
                            }
                        }
                    }
                    if (listPoz.size() != 0) {
                        for (String str : listPoz) {
                            str = str.split("_")[0];
                            listPozNeNashel.add(str);
                        }
                    }

                }
            } else {


                String codPoz = null;
                Iterator<String> it = listPoz.iterator();
                while (it.hasNext()) {
                    razbiraemNaChasti(zak, it);
                    codPoz = mapParsingPDF.get(poz);
                    if (codPoz == null) {
                        textArea.append(" нет файла " + poz + " = " + codPoz + " в папкe " + pathFolderPoz + "\n");
                        continue;
                    }

                    if (pathFolderPoz == null) {
                        String finalCodPoz = codPoz;

                        //  pozPathsList
                        /*  List<Path>*/
                        pozPathsList = Files.walk(pathFolderZakaz, 4, FileVisitOption.FOLLOW_LINKS)
                                .filter(Files::isRegularFile)
                                .filter(x -> x.toFile().getName().endsWith(".dxf"))
                                .filter(x -> x.toFile().getName().contains(finalCodPoz))                             //equalsIgnoreCase(pozStr + ".dxf"))
                                .collect(Collectors.toList());
                        //.findFirst();

                        if (pozPathsList.size() >= 1) {


                            pathFilePoz = checkExactMatch(codPoz, pozPathsList);
                            // pathFilePoz =  pozPathsList.get();


                            pathFolderPoz = pathFilePoz.getParent();
                            // nameFilePoz = pathFilePoz.toFile().getName();

//                                boolean isLastCharIsDigit = Character.isDigit(pathFolderPoz.toString().charAt(pathFolderPoz.toString().length() - 1));
//                                if (!isLastCharIsDigit) {
//                                    writer.write(pathFolderPoz + "\n");
//                                    writer.close();
//                                }
                        }
                    }
                    File file = new File(pathFolderPoz.toString() + "\\" + codPoz + ".dxf");
                    if (!file.exists()) {
                        textArea.append(" нет файла " + poz + " = " + codPoz + " в папкe " + pathFolderPoz + "\n");
                        continue;
                    }
                    copyFileToDxfFolder(delListOnInv, strZakaz, file, it, zak);
                    mapParsingPDF.remove(poz, codPoz);
                }
            }
            // }     // esli papka s zakazom ne sushestvuet
        }      // setZakaz
    }

    private String modifyPozForSearch(String poz) {
        String firstChar = poz.substring(0, 1);
        if (firstChar.equalsIgnoreCase("M") || firstChar.equalsIgnoreCase("М")) {
            String ostatok = poz.substring(1);
            String stroka = firstChar + "-" + ostatok;
            return stroka;
        }
        return poz;
    }

    private Path checkExactMatch(String pozStr, List<Path> pozPathsList) {
        List<Path> result = new ArrayList<>();

        if (pozPathsList.size() == 1) {
            result = pozPathsList;

        } else {
            for (Path path : pozPathsList) {
                String pathStr = path.toString();
                if (pathStr.contains("round") || pathStr.contains("clean")) {
                    continue;
                }
                int index = pathStr.indexOf(pozStr);
                boolean isDigitBeforePozStr = Character.isDigit(pathStr.charAt(index - 1));
                index += pozStr.length();
                boolean isDigitAfterPozStr = Character.isDigit(pathStr.charAt(index));
                if (isDigitBeforePozStr || isDigitAfterPozStr) {
                    continue;
                } else {
                    result.add(path);
                }
            }
        }
        if (result.size() == 0) {
            for (Path path : pozPathsList) {
                String pathStr = path.toString();
//                if (pathStr.contains("round") || pathStr.contains("clean")) {
//                    continue;
//                }
                int index = pathStr.indexOf(pozStr);
                boolean isDigitBeforePozStr = Character.isDigit(pathStr.charAt(index - 1));
                index += pozStr.length();
                boolean isDigitAfterPozStr = Character.isDigit(pathStr.charAt(index));
                if (isDigitBeforePozStr || isDigitAfterPozStr) {
                    continue;
                } else {
                    result.add(path);
                }
            }
        }
        if (result.size() == 1) {
            return result.get(0);
        } else if (result.size() > 1) {


            // Path path = null;


            Path path = result.get(0);
            for (Path tmpPath : result) {
                try {
                    if (((FileTime) Files.getAttribute(tmpPath, "creationTime")).compareTo(((FileTime) Files.getAttribute(path, "creationTime"))) > 0) {
                        path = tmpPath;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            textArea.append("find several (" + result.size() + ") files " + pozStr + " in  zakaz  " + zakaz + "\n");

            // result.stream().forEach(x -> textArea.append("\t" + x + "\n"));  // textArea.append("\t\t" + value + "\n");
            for (Path pathVivod : result) {
                textArea.append("\t" + pathVivod + "__");
                FileTime ft = null;
                try {
                    ft = (FileTime) Files.getAttribute(pathVivod, "creationTime");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                textArea.append(formatDateTime(ft) + "\n");
            }

            FileTime fileTime = null;
            try {
                fileTime = (FileTime) Files.getAttribute(path, "creationTime");
            } catch (IOException e) {
                e.printStackTrace();
            }
            textArea.append("\t\t take from folder   " + formatDateTime(fileTime) + "\n");
            return path;


        } else {
            return null;
        }
    }


    public static String formatDateTime(FileTime fileTime) {

        LocalDateTime localDateTime = fileTime
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return localDateTime.format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm"));
    }


    private boolean ChekIsInvInListNoCash() throws IOException {
        boolean result = false;
        List<String> listPath = Files.readAllLines(Paths.get(fileCashPath));
        for (String str : listPath) {
            if (str.trim().equals("--" + zakaz + "--")) {
                result = true;
            }
        }
        return result;
    }


    ///todo copyFileToDxfFolder  ищем у мосина
    private void copyFileToDxfFolder(Map<String, String> delListOnInv, String strInvOrZakaz, File file, Iterator<String> it, String zakOrInv) throws IOException {
        File fileInFolderDXF;

//
        if (identicalPozAndInv.add(poz)) {
            fileInFolderDXF = new File(pathDXF + poz + ".dxf");
        } else {
            fileInFolderDXF = new File(pathDXF + poz + "_" + zakaz + ".dxf");
        }

        if (!fileInFolderDXF.exists()) {
            Files.copy(file.toPath(), fileInFolderDXF.toPath());
        }
        addToFileORD(fileInFolderDXF.getName());
        result++;
        searchPozFromMosin = true;


        clearDXF(fileInFolderDXF.toString());
        prepareMark(fileInFolderDXF.toString());
        it.remove();
    }

    private void clearDXF(String pathFileInDxfFolder) throws IOException {
        ModifyDXF modifyDXF = new ModifyDXF();

        String stroka = modifyDXF.readFileInString(pathFileInDxfFolder);
        stroka = stroka.replace(" ", "");
        hasSpaces = stroka.contains(" ");
        String[] masStrok = modifyDXF.breakStringOnThreeParts(stroka);
        List<String> entityies = modifyDXF.breakMidlePartOnEntityes(masStrok[1]);

        proverkaPazov(entityies);


        //  удаляем весь текст
        entityies.removeIf(s -> s.contains("0\r\nTEXT\r\n8"));

        //  удаляем все линии  на слое 18
        entityies.removeIf(s -> s.contains("0\r\nLINE\r\n8\r\n18\r\n"));

        //  удаляем все линии  на слое 22
        entityies.removeIf(s -> s.contains("0\r\nLINE\r\n8\r\n22\r\n"));

        //  удаляем все линии  на слое 14
        entityies.removeIf(s -> s.contains("0\r\nLINE\r\n8\r\n14\r\n"));

        //  удаляем все линии  на слое 27
        entityies.removeIf(s -> s.contains("0\r\nLINE\r\n8\r\n27\r\n"));

        //  удаляем все линии  на слое 30
        entityies.removeIf(s -> s.contains("0\r\nLINE\r\n8\r\n30\r\n"));

        List<Point2D> listPoint = new LinkedList<>();
        //  меняем все круги из полилиний на обычные круги
        List<String> listCircle = modifyDXF.modifyPolylineInCircle(hasSpaces, entityies, listPoint);

        // у всех примитивов делаем  слой 0 цвет 7  тип линии CONTINUOUS
        String ent = "";
        String str = "";
        String before = "";
        String after = "\r\n8\r\n0\r\n6\r\nCONTINUOUS\r\n62\r\n7";

        for (int j = 0; j < entityies.size(); j++) {
            if (entityies.get(j).length() > 0) {
                str = entityies.get(j);
                int beginLayer = str.indexOf("\r\n8\r\n");
                int beginColor = str.indexOf("\r\n62\r\n");
                beginColor = beginColor + 6;
                int endColor = str.indexOf("\r\n", beginColor);
                before = str.substring(beginLayer, endColor);

                ent = entityies.get(j);
                ent = ent.replace(before, after);


                //   System.out.println(before);


//            int beginTypeLine = str.indexOf("\r\n6\r\n", beginLayer);
//            layer = str.substring(beginLayer,beginTypeLine).replace("\r\n8\r\n","");
//            int beginColor = str.indexOf("\r\n62\r\n", beginTypeLine);
//            typeLine = str.substring(beginTypeLine, beginColor).replace("\r\n6\r\n","");
//            int endColor = str.indexOf("\r\n",  ( beginColor + 6));
//            color = str.substring(beginColor, endColor).replace("62\r\n","").trim();


                //            ent = entityies.get(j);
                //   ent = ent.replace("\r\n8\r\nCUT\r\n", "\r\n8\r\n0\r\n");
                //  ent = ent.replace("\r\n8\r\nLAYOUT\r\n", "\r\n8\r\n0\r\n");

                entityies.set(j, ent);
            }
        }

        PrintWriter writer = new PrintWriter(pathFileInDxfFolder);
        writer.print("");
        writer.close();

        PrintWriter writerNewData = new PrintWriter(pathFileInDxfFolder);
        writerNewData.print(masStrok[0]);

        for (String s : entityies) {
            writerNewData.print(s);
        }

        for (String s : listCircle) {
            writerNewData.print(s);
        }

        writerNewData.print(masStrok[2]);
        writerNewData.close();


    }

    private void proverkaPazov(List<String> entityies) {
        Rectangle gabaritRectangle = null;
        List<Point2D> pointsKonturList = new LinkedList<>();
        for (String poly : entityies) {
            if (poly.contains("0\r\nPOLYLINE\r\n")) {
                String[] masVertex = poly.split("VERTEX");
                for (int i = 1; i < masVertex.length; i++) {
                    int beginX = masVertex[i].indexOf("\r\n10\r\n");
                    int beginY = masVertex[i].indexOf("\r\n20\r\n", beginX);
                    String strX = masVertex[i].substring(beginX, beginY).replace("\r\n10\r\n", "");
                    if (masVertex[i].contains("\r\n30\r\n")) {
                        int beginZ = masVertex[i].indexOf("\r\n30\r\n", beginY);
                        String strY = masVertex[i].substring(beginY, beginZ).replace("\r\n20\r\n", "");
                        pointsKonturList.add(new Point2D(Double.parseDouble(strX), Double.parseDouble(strY)));
                    } else if (masVertex[i].contains("\r\n42\r\n")) {
                        int beginZ = masVertex[i].indexOf("\r\n42\r\n", beginY);
                        String strY = masVertex[i].substring(beginY, beginZ).replace("\r\n20\r\n", "");
                        pointsKonturList.add(new Point2D(Double.parseDouble(strX), Double.parseDouble(strY)));
                    } else {
                        int beginZ = masVertex[i].indexOf("\r\n0\r\n", beginY);
                        String strY = masVertex[i].substring(beginY, beginZ).replace("\r\n20\r\n", "");
                        pointsKonturList.add(new Point2D(Double.parseDouble(strX), Double.parseDouble(strY)));
                    }
                }
            }
        }


        gabaritRectangle = stroimPaz(pointsKonturList);
        pointsKonturList.clear();

        //все  пазы  в отдельный список

        List<String> listPolyline = new LinkedList<>();
        for (String poly : entityies) {
            if (poly.contains("0\r\nPOLYLINE\r\n")) {
                listPolyline.add(poly);
            }
        }


        for (String poly : listPolyline) {
            String[] masVertex = poly.split("VERTEX");
            for (int i = 1; i < masVertex.length; i++) {
                int beginX = masVertex[i].indexOf("\r\n10\r\n");
                int beginY = masVertex[i].indexOf("\r\n20\r\n", beginX);
                String strX = masVertex[i].substring(beginX, beginY).replace("\r\n10\r\n", "");
                if (masVertex[i].contains("\r\n30\r\n")) {
                    int beginZ = masVertex[i].indexOf("\r\n30\r\n", beginY);
                    String strY = masVertex[i].substring(beginY, beginZ).replace("\r\n20\r\n", "");
                    pointsKonturList.add(new Point2D(Double.parseDouble(strX), Double.parseDouble(strY)));
                } else if (masVertex[i].contains("\r\n42\r\n")) {
                    int beginZ = masVertex[i].indexOf("\r\n42\r\n", beginY);
                    String strY = masVertex[i].substring(beginY, beginZ).replace("\r\n20\r\n", "");
                    pointsKonturList.add(new Point2D(Double.parseDouble(strX), Double.parseDouble(strY)));
                } else {
                    int beginZ = masVertex[i].indexOf("\r\n0\r\n", beginY);
                    String strY = masVertex[i].substring(beginY, beginZ).replace("\r\n20\r\n", "");
                    pointsKonturList.add(new Point2D(Double.parseDouble(strX), Double.parseDouble(strY)));
                }
            }

            Rectangle paz = stroimPaz(pointsKonturList);
            if (!paz.equals(gabaritRectangle)) {
                deleteCircleInPaz(paz, entityies);
            }
            pointsKonturList.clear();
        }
    }

    private void deleteCircleInPaz(Rectangle paz, List<String> entityies) {
        Iterator<String> it = entityies.iterator();
        while (it.hasNext()) {
            String ent = it.next();
            if (ent.contains("\r\nCIRCLE\r\n")) {
                int beginX = ent.indexOf("\r\n10\r\n");
                int beginY = ent.indexOf("\r\n20\r\n", beginX);
                String strX = ent.substring(beginX, beginY).replace("\r\n10\r\n", "");
                if (ent.contains("\r\n30\r\n")) {
                    int beginZ = ent.indexOf("\r\n30\r\n", beginY);
                    String strY = ent.substring(beginY, beginZ).replace("\r\n20\r\n", "");
                    boolean cirleInPaz = proverkaCircleInPaz(paz, Double.parseDouble(strX), Double.parseDouble(strY));
                    if (cirleInPaz) {
                        it.remove();
                    }
                } else {
                    int beginZ = ent.indexOf("\r\n40\r\n", beginY);
                    String strY = ent.substring(beginY, beginZ).replace("\r\n20\r\n", "");
                    boolean cirleInPaz = proverkaCircleInPaz(paz, Double.parseDouble(strX), Double.parseDouble(strY));
                    if (cirleInPaz) {
                        it.remove();
                    }
                }
            }
        }
    }

    private boolean proverkaCircleInPaz(Rectangle paz, double centrCircleX, double centrCircleY) {
        boolean result = false;
        if (centrCircleX >= paz.getPointMin().getX() &&
                centrCircleX <= paz.getPointMax().getX() &&
                centrCircleY >= paz.getPointMin().getY() &&
                centrCircleY <= paz.getPointMax().getY()) {
            result = true;
        }
        return result;
    }


    private Rectangle stroimPaz(List<Point2D> pointsKonturList) {

        double Xmin = pointsKonturList.get(0).getX();
        double Xmax = pointsKonturList.get(0).getX();
        double Ymin = pointsKonturList.get(0).getY();
        double Ymax = pointsKonturList.get(0).getY();

        for (Point2D point2D : pointsKonturList) {
            if (point2D.getX() < Xmin) {
                Xmin = point2D.getX();
            }
            if (point2D.getX() > Xmax) {
                Xmax = point2D.getX();
            }
            if (point2D.getY() > Ymax) {
                Ymax = point2D.getY();
            }
            if (point2D.getY() < Ymin) {
                Ymin = point2D.getY();
            }
        }

        return new Rectangle(new Point2D(Xmin, Ymin), new Point2D(Xmax, Ymax));
    }

//    private void deletePozFromMapfileNotFaundOnInv(Map<String, String> delListOnInv, String strInvOrZakaz) {
//        if (delListOnInv != null) {
//            String invent = delListOnInv.get(strInvOrZakaz + "_" + poz).split("_")[0];
//            fileNotFaundOnInv.remove(invent, poz + "_" + zakaz + "_" + gabaritCSV + "_" + kolvoPoz);
//        }
//    }

    private Map<String, Path> searchInCash() throws IOException {
        List<String> listPathFolder = Files.readAllLines(Paths.get(fileCashPath));
        Map<String, Path> listFindPath = listPathFolder.stream()
                .map(x -> x.split("="))
                .collect(Collectors.toMap(x -> x[0], x -> Path.of(x[1])));
        return listFindPath;
    }


    private boolean check(String line, String strInvOrZakaz) {
        boolean res = false;
        List<String> list = new LinkedList<>();
        list.add(" " + strInvOrZakaz + "\\");
        list.add(" " + strInvOrZakaz + "-");
        list.add("-" + strInvOrZakaz + "\\");
        list.add("\\" + strInvOrZakaz + "\\");
        list.add(" " + strInvOrZakaz + " ");
        //    String search6 = strInvOrZakaz + "\\";
        list.add(" " + strInvOrZakaz + ",");
        list.add("--" + strInvOrZakaz + "--");
        list.add("--" + strInvOrZakaz);

        for (String str : list) {
            if (line.contains(str)) {
                res = true;
            }
        }
        return res;
    }

    private void razbiraemNaChasti(String zakOrInv, Iterator<String> it) {
        String stroka = it.next();
        if (zakOrInv.equals("заказ")) {
            poz = stroka.split("_")[0];
            inv = stroka.split("_")[1];
            gabaritCSV = stroka.split("_")[2];
            kolvoPoz = Integer.parseInt(stroka.split("_")[3]);
        } else {
            poz = stroka.split("_")[0];
            zakaz = stroka.split("_")[1];
            gabaritCSV = stroka.split("_")[2];
            kolvoPoz = Integer.parseInt(stroka.split("_")[3]);
        }
    }

    private void fastMethod(Path path, List<String> listPoz, String strInvOrZakaz, String zakOrInv) throws IOException {
        Iterator<String> it = listPoz.iterator();
        boolean isFound = false;
        while (it.hasNext()) {
            razbiraemNaChasti(zakOrInv, it);
            isFound = false;
            File file = new File(path.toString() + "\\" + poz + ".dxf");
            if (Files.exists(file.toPath())) {
                copyFileToDxfFolder(delListOnInv, strInvOrZakaz, file, it, zakOrInv);
                isFound = true;
            }

            if (!isFound) {
                String codPoz = null;
                // Iterator<String> itt = listPoz.iterator();

                // while (itt.hasNext()) {
                //   razbiraemNaChasti(zakOrInv, itt);
                Set<String> keySet = mapParsingPDF.keySet();
                for (String keyMap : keySet) {
                    if (keyMap.contains(poz)) {
                        codPoz = mapParsingPDF.get(keyMap);
                        if (codPoz.equals("")) {
                            continue;
                        }

                        File pathFilePoz = new File(path + "\\" + codPoz + ".dxf");
                        if (!pathFilePoz.exists()) {
                            // textArea.append("позиция (" + poz + ") = " + codPoz + " не найдена\n");
                            continue;
                        }

                        copyFileToDxfFolder(delListOnInv, strInvOrZakaz, pathFilePoz, it, zakOrInv);
                        mapParsingPDF.remove(keyMap, codPoz);
                        break;
                    }
                }

            }
        }
    }

    private boolean conteins(String str, String poz) {
        boolean res = false;
        boolean contein = str.contains(poz);
        if (!contein) {
            String firstChar = poz.substring(0);
            if (firstChar.equalsIgnoreCase("M") || firstChar.equalsIgnoreCase("М")) {
                String ostatok = poz.substring(1, poz.length() - 1);
                String search = firstChar + "-" + ostatok;
                if (str.contains(search)) {
                    return true;
                }
            }

            return false;
        }
        int lengthPoz = poz.length();
        int lengthStr = str.length();

        if (contein && (lengthPoz == lengthStr)) {
            return true;
        }

        if (lengthStr > lengthPoz) {
            char ch = str.charAt(lengthPoz);

            if (contein && !Character.isDigit(ch)) {
                return true;
            }
        }
        return res;
    }

    private List<Path> find(Path startPath, String extension) throws IOException {
        List<Path> matches = new ArrayList<>();

        Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                if (file.toFile().isDirectory() && file.toString().contains(zakaz)) {
                    matches.add(file);
                }
                return FileVisitResult.TERMINATE;
            }

//            @Override
//            public FileVisitResult visitFileFailed(Path file, IOException exc) {
//                return FileVisitResult.CONTINUE;
//            }
        });
        return matches;
    }

    public List<Path> searchPDF(String path) {
        List<Path> listPathPDF;
        try {
            listPathPDF = Files.walk(Path.of(path))
                    .filter(Files::isRegularFile)
                    .filter(x -> x.toFile().getName().endsWith(".pdf"))
                    //  .map(x -> x.getFileName())
                    // .map(x -> x.toString())
                    //   .map(x -> x.replace(".dxf", ""))
                    .collect(Collectors.toList());
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        return listPathPDF;
    }

    private void copyFileInDXF(File file, String srokaSearch) throws IOException {
        String stringAbsolutPathToFileInDXF = "";


        if (!inv.equals("-")) {
            if (countCharInString(srokaSearch, '.') == 1) {
                stringAbsolutPathToFileInDXF = pathDXF + inv.replace(".", "") + srokaSearch.replace(".", "") + "L" + "_" + zakaz + ".dxf";
            } else {
                stringAbsolutPathToFileInDXF = pathDXF + inv.replace(".", "") + srokaSearch.substring(1) + "L" + "_" + zakaz + ".dxf";
            }
        } else {
            stringAbsolutPathToFileInDXF = pathDXF + srokaSearch + "L.dxf";
        }


        File fileInFolderDXF = new File(stringAbsolutPathToFileInDXF);
        if (!fileInFolderDXF.exists()) {
            try {
                Files.copy(file.toPath(), fileInFolderDXF.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        addToFileORD(fileInFolderDXF.getName());
        if (!inv.equals("-")) {
            prepareMark(stringAbsolutPathToFileInDXF);                                            //  устанавливаем маркировку
        }
    }

    private void addToFileORD(String name) throws IOException {
        String strKK = textKK.replace("/", "_");

        if (!strKK.contains("_")) {
            int currentYear = Year.now().getValue() % 100;
            strKK = strKK.concat("_").concat(String.valueOf(currentYear));
        }

        name = name.replace(".dxf", ".dft");
        String path = strKK + "\\" + name;
        String tolshina = gabaritCSV.split("x")[0];
        String result = String.format("\"-\"       \"\\\\NTS2DC\\Users\\OGT\\BAZA\\Autonest\\%s\"       %s       1       @M=0       @T=%s       @PartDirection=15       @DueDate=1733425200" + System.lineSeparator(), path, kolvoPoz, tolshina);
        FileWriter fileORD = new FileWriter("c:\\Users\\alexx.STALMOST\\Desktop\\_DXF\\fileORD.Ord", StandardCharsets.UTF_16LE, true);
        fileORD.write(result);
        fileORD.close();
        summaPoz += kolvoPoz;

    }


    ///todo  prepareMark  общий
    private void prepareMark(String stringAbsolutPathToFileInDXF) {
        printPozShapka();
        String stroka = null;
        stroka = readFileInString(stringAbsolutPathToFileInDXF);
        stroka = stroka.replace(" ", "");
        hasSpaces = stroka.contains(" ");

        String[] masStrok = breakStringOnThreeParts(stroka);
        breakMidlePartOnEntityes(masStrok[1]);

        double[] gabaritPoz = getGabaritPosition();
        checkGabarit(gabaritPoz);

        if (!searchPozFromMosin) {

            int handler = 0;
            handler = getPropertyDefaultMark();
            if (handler != -1) {
                char lastCharInString = markDefault.charAt(markDefault.length() - 1);
                if (lastCharInString == ';') {
                    setMarkOnlyPoz(gabaritPoz, handler);
                } else if (lastCharInString == '_') {
                    setMarkHorizon(gabaritPoz, handler);
                } else {
                    setMarkVertical(gabaritPoz, handler);
                }
                List<String> listCircle = new LinkedList<>();         //  заглушка для метода
                try {
                    fileSave(new File(stringAbsolutPathToFileInDXF), masStrok, listCircle);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                textArea.append(poz + " нет default маркировки\n");
            }
        } else {

            opredelyaemSposobMark(gabaritPoz);

            List<String> listCircle = new LinkedList<>();         //  заглушка для метода
            try {
                fileSave(new File(stringAbsolutPathToFileInDXF), masStrok, listCircle);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void printPozShapka() {
        System.out.println("#".repeat(300));
        System.out.println("#".repeat(300));
        System.out.println("#####################################     " + poz + "       ######################################");
        System.out.println("#".repeat(300));
        System.out.println("#".repeat(300));
    }

    private void opredelyaemSposobMark(double[] gabaritPoz) {

        double Xmin = gabaritPoz[0];
        double Xmax = gabaritPoz[1];
        double Ymin = gabaritPoz[2];
        double Ymax = gabaritPoz[3];

        int shirinaPoz = (int) Math.abs(Ymax - Ymin);
        int dlinnaPoz = (int) Math.abs(Xmax - Xmin);

        boolean markaStolbom = false;
        if (shirinaPoz > dlinnaPoz) {
            markaStolbom = true;
        }

        boolean markirovkaStay = false;

        int height = 20;
        boolean flag = true;
        Rectangle ramkaMark;
        while (height > 10 && flag) {
            ramkaMark = defineGabaritRamkiMark(height, TypeMark.HORIZONTAL);

            if (dlinnaPoz > ramkaMark.getDlinna() && shirinaPoz > ramkaMark.getShirina()) {
                markirovkaStay = findPlaceForMark(gabaritPoz, markaStolbom, ramkaMark, height, TypeMark.HORIZONTAL);
            }
            if (markirovkaStay) {
                flag = false;
            } else {
                ramkaMark = defineGabaritRamkiMark(height, TypeMark.VERTICAL);
                if (dlinnaPoz > ramkaMark.getDlinna() && shirinaPoz > ramkaMark.getShirina()) {
                    markirovkaStay = findPlaceForMark(gabaritPoz, markaStolbom, ramkaMark, height, TypeMark.VERTICAL);
                }
                if (markirovkaStay) {
                    flag = false;
                }
            }
            height--;
        }

        if (!markirovkaStay) {

            height = 20;
            flag = true;

            while (height > 10 && flag) {
                ramkaMark = defineGabaritRamkiMark(height, TypeMark.ONLY_POZ);

                if (dlinnaPoz > ramkaMark.getDlinna() && shirinaPoz > ramkaMark.getShirina()) {
                    markirovkaStay = findPlaceForMark(gabaritPoz, markaStolbom, ramkaMark, height, TypeMark.ONLY_POZ);
                }
                if (markirovkaStay) {
                    flag = false;
                }
                height--;
            }
        }

        if (!markirovkaStay) {
            // System.out.println(zakaz + " " + poz + " маркировка  НЕ ПОСТАВЛЕНА  ");
            textArea.append("\tмаркировка  НЕ ПОСТАВЛЕНА  (" + gabaritCSV + ")\n");
        }
    }

    private Rectangle defineGabaritRamkiMark(int height, TypeMark typeMark) {
        double lenghtZakaz = getLenghtStr(zakaz) * 10;
        double lenghtGradeSteel = getLenghtStr(gradeSteel) * 10;
        double lenghtPoz = getLenghtStr(poz) * height;
        Rectangle ramkaMark = null;
        if (typeMark.equals(TypeMark.VERTICAL)) {
            double maxDlinnaMark = Math.max(lenghtPoz, Math.max(lenghtZakaz, lenghtGradeSteel));
            double maxShirinaMark = 10 + 5 + 10 + 5 + height;
            ramkaMark = new Rectangle(new Point2D(0, 0), new Point2D(maxDlinnaMark, maxShirinaMark));
        }
        if (typeMark.equals(TypeMark.HORIZONTAL)) {
            double maxDlinnaMark = getLenghtStr(poz) * height + 10 * (getLenghtStr(" " + zakaz + " " + gradeSteel));
            double maxShirinaMark = height;
            ramkaMark = new Rectangle(new Point2D(0, 0), new Point2D(maxDlinnaMark, maxShirinaMark));
        }
        if (typeMark.equals(TypeMark.ONLY_POZ)) {
            double maxDlinnaMark = getLenghtStr(poz) * height;
            double maxShirinaMark = height;
            ramkaMark = new Rectangle(new Point2D(0, 0), new Point2D(maxDlinnaMark, maxShirinaMark));
        }

        return ramkaMark;
    }


    private void checkGabarit(double[] gabaritPoz) {
        int shirinaCSV = Integer.parseInt(gabaritCSV.split("x")[1]);
        int dlinnaСSV = Integer.parseInt(gabaritCSV.split("x")[2]);
        if (shirinaCSV > dlinnaСSV) {
            int tmp = shirinaCSV;
            shirinaCSV = dlinnaСSV;
            dlinnaСSV = tmp;
        }
        double Xmin = gabaritPoz[0];
        double Xmax = gabaritPoz[1];
        double Ymin = gabaritPoz[2];
        double Ymax = gabaritPoz[3];

        double shirinaPoz = Math.abs(Ymax - Ymin);
        double dlinnaPoz = Math.abs(Xmax - Xmin);
        if (shirinaPoz > dlinnaPoz) {
            double tmp = shirinaPoz;
            shirinaPoz = dlinnaPoz;
            dlinnaPoz = tmp;
        }
        int deltaX = Math.abs((int) (dlinnaPoz - dlinnaСSV));
        int deltaY = Math.abs((int) (shirinaPoz - shirinaCSV));

        double dX = Math.abs(dlinnaPoz - dlinnaСSV);
        double dY = Math.abs(shirinaPoz - shirinaCSV);
        // textArea.append( zakaz +"_" + poz + "    (" + deltaY + " ; " + deltaX + " )                        (" + dY + " ; " + dX + " )\n")

        if (dY == 0 && dX == 0) {
            textArea.append(++countNashel + " )         " + zakaz + " _ " + poz + "\n");
        } else {
            textArea.append(++countNashel + " )         " + zakaz + " _ " + poz + "     (" + dY + " ; " + dX + " )\n");
        }

        panel.revalidate();


    }

    private void setMarkVertical(double[] gabaritPoz, int handler) {
        double lenghtMark = getLenghtStr(poz) * markDefaultHeight;                            // 10  высота символа на DefaultMark
        double markDefaultCenterX = markDefault_X + (lenghtMark / 2);
        double markDefaultCenterY = markDefault_Y + markDefaultHeight / 2;

        double Xmin = gabaritPoz[0];
        double Xmax = gabaritPoz[1];
        double Ymin = gabaritPoz[2];
        double Ymax = gabaritPoz[3];

        double pointMarkX;
        double pointMarkY;
        double XminRamka;
        double XmaxRamka;
        double YminRamka;
        double YmaxRamka;


        double lenghtZakaz = getLenghtStr(zakaz) * 10;
        double lenghtGradeSteel = getLenghtStr(gradeSteel) * 10;
        double maxZakazAndGradeSteel = Math.max(lenghtZakaz, lenghtGradeSteel);
        double lenghtPoz;
        double totalMaxLenght;
        double totalMaxHeight;

        for (int markHeight = 20; markHeight > 8; markHeight--) {                       //  уменьшаем высоту текста
            lenghtPoz = getLenghtStr(poz) * markHeight;
            totalMaxLenght = Math.max(maxZakazAndGradeSteel, lenghtPoz);
            totalMaxHeight = 30 + markHeight;                                          //30 = 10 + 5 + 10 +5    10-высота zakaz и gradeSteel и  5-зазор между ними

            pointMarkX = markDefaultCenterX - (totalMaxLenght / 2);
            pointMarkY = markDefaultCenterY - (totalMaxHeight / 2);

            XminRamka = pointMarkX;
            XmaxRamka = pointMarkX + totalMaxLenght;
            YminRamka = pointMarkY;
            YmaxRamka = pointMarkY + totalMaxHeight;
            boolean tochkaVRamke = false;

            for (Point2D pnt : listPointInsidePoz) {

                if (pnt.getX() > XminRamka && pnt.getX() < XmaxRamka && pnt.getY() > YminRamka && pnt.getY() < YmaxRamka) {
                    tochkaVRamke = true;
                    break;
                }
            }
            if (!tochkaVRamke && ((Xmin < XminRamka) && (Xmax > XmaxRamka) && (Ymin < YminRamka) && (Ymax > YmaxRamka))) {
                double markX = markDefaultCenterX - (lenghtPoz / 2);
                double markY = YminRamka + 30;
                entityies.add(generateTextEntity(poz, handler, markX, markY, markHeight));

                markX = markDefaultCenterX - (lenghtZakaz / 2);
                markY = YminRamka + 15;
                entityies.add(generateTextEntity(zakaz, ++handler, markX, markY, 10));

                markX = markDefaultCenterX - (lenghtGradeSteel / 2);
                markY = YminRamka;
                entityies.add(generateTextEntity(gradeSteel, ++handler, markX, markY, 10));
                break;
            }
        }


    }

    private void setMarkHorizon(double[] gabaritPoz, int handler) {
        // double lenghtMark = getLenghtStringMark(poz) * 10 + getLenghtStringMark("_") * 10;    // высота символа на DefaultMark
        double markDefaultCenterY = markDefault_Y + 5;

        double Xmin = gabaritPoz[0];
        double Xmax = gabaritPoz[1];
        double Ymin = gabaritPoz[2];
        double Ymax = gabaritPoz[3];

        double pointMarkX;
        double pointMarkY;
        double XminRamka;
        double XmaxRamka;
        double YminRamka;
        double YmaxRamka;


        for (int markHeight = 20; markHeight > 8; markHeight--) {                       //  уменьшаем высоту текста
            double len = markHeight * getLenghtStr(poz);
            double lendthZakazPlusGradeSteel = 10 * (getLenghtStr(" " + zakaz + " " + gradeSteel));

            len += lendthZakazPlusGradeSteel;
            if (markDefault_X + len >= Xmax) {
                continue;
            }
            // pointMarkX = markDefaultCenterX - (len / 2);
            pointMarkY = markDefaultCenterY - (markHeight / 2);

            XminRamka = markDefault_X;
            XmaxRamka = markDefault_X + len;
            YminRamka = pointMarkY;
            YmaxRamka = pointMarkY + markHeight;
            boolean tochkaVRamke = false;
            for (Point2D pnt : listPointInsidePoz) {

                if (pnt.getX() > XminRamka && pnt.getX() < XmaxRamka && pnt.getY() > YminRamka && pnt.getY() < YmaxRamka) {
                    tochkaVRamke = true;
                    break;
                }
            }
            if (!tochkaVRamke && ((Xmin < XminRamka) && (Xmax > XmaxRamka) && (Ymin < YminRamka) && (Ymax > YmaxRamka))) {
                entityies.add(generateTextEntity(poz, handler, XminRamka, YminRamka, markHeight));
                double pointBeginZakaz = markDefault_X + markHeight * getLenghtStr(poz);
                entityies.add(generateTextEntity(" " + zakaz + " " + gradeSteel, ++handler, pointBeginZakaz, YminRamka, 10));
                break;
            }
        }


    }

    private void setMarkOnlyPoz(double[] gabaritPoz, int handler) {
        double lenghtMark = getLenghtStr(poz) * 10 + getLenghtStr(";") * 10;    // высота символа на DefaultMark
        double markDefaultCenterX = markDefault_X + (lenghtMark / 2);
        double markDefaultCenterY = markDefault_Y + 5;

        double Xmin = gabaritPoz[0];
        double Xmax = gabaritPoz[1];
        double Ymin = gabaritPoz[2];
        double Ymax = gabaritPoz[3];

        double pointMarkX;
        double pointMarkY;
        double XminRamka;
        double XmaxRamka;
        double YminRamka;
        double YmaxRamka;


        for (int markHeight = 20; markHeight > 8; markHeight--) {                       //  уменьшаем высоту текста
            double len = getLenghtStr(poz) * markHeight;
            pointMarkX = markDefaultCenterX - (len / 2);
            pointMarkY = markDefaultCenterY - (markHeight / 2);

            XminRamka = pointMarkX;
            XmaxRamka = pointMarkX + len;
            YminRamka = pointMarkY;
            YmaxRamka = pointMarkY + markHeight;
            boolean tochkaVRamke = false;
            for (Point2D pnt : listPointInsidePoz) {

                if (pnt.getX() > XminRamka && pnt.getX() < XmaxRamka && pnt.getY() > YminRamka && pnt.getY() < YmaxRamka) {
                    tochkaVRamke = true;
                    break;
                }
            }
            if (!tochkaVRamke && ((Xmin < XminRamka) && (Xmax > XmaxRamka) && (Ymin < YminRamka) && (Ymax > YmaxRamka))) {
                entityies.add(generateTextEntity(poz, handler, XminRamka, YminRamka, markHeight));
                break;
            }
        }
    }

    private int getPropertyDefaultMark() {

        for (String elem : entityies) {
            if (elem.contains("0\r\nTEXT\r\n")) {
                stringEntityText = elem;
                entityies.remove(elem);
                break;
            }
        }

        if (hasSpaces) {
            if (stringEntityText != null) {
                int beginHanler = stringEntityText.indexOf("0\r\nTEXT\r\n  5\r\n");
                int endHanler = stringEntityText.indexOf("\r\n  8\r\n", beginHanler);
                String textHandler = stringEntityText.substring(beginHanler, endHanler).replace("0\r\nTEXT\r\n  5\r\n", "");
                int handler = Integer.parseInt(textHandler, 16);
                // handler1++;
                //int handler2 = handler1 + 1;
                int beginX = stringEntityText.indexOf("\r\n 10\r\n");
                int beginY = stringEntityText.indexOf("\r\n 20\r\n", beginX);
                String strX = stringEntityText.substring(beginX, beginY).replace("\r\n 10\r\n", "");
                int beginZ = stringEntityText.indexOf("\r\n 30\r\n", beginY);
                String strY = stringEntityText.substring(beginY, beginZ).replace("\r\n 20\r\n", "");
                markDefault_X = Double.parseDouble(strX);
                markDefault_Y = Double.parseDouble(strY);
                int beginText = stringEntityText.indexOf("\r\n  1\r\n", beginZ);
                int endText = stringEntityText.indexOf("\r\n", beginText + 7);
                markDefault = stringEntityText.substring(beginText, endText).replace("\r\n  1\r\n", "");
                int beginHHeightDefoultMark = stringEntityText.indexOf("\r\n 40\r\n");
                int endHeightDefoultMark = stringEntityText.indexOf("\r\n  1\r\n", beginHHeightDefoultMark);
                markDefaultHeight = Double.parseDouble(stringEntityText.substring(beginHHeightDefoultMark, endHeightDefoultMark).replace("\r\n 40\r\n", ""));
                return handler;
            } else {
                return -1;
            }
        } else {
            if (stringEntityText != null) {
                int beginHanler = stringEntityText.indexOf("0\r\nTEXT\r\n5\r\n");
                int endHanler = stringEntityText.indexOf("\r\n8\r\n", beginHanler);
                String textHandler = stringEntityText.substring(beginHanler, endHanler).replace("0\r\nTEXT\r\n5\r\n", "");
                int handler = Integer.parseInt(textHandler, 16);
                // handler1++;
                //int handler2 = handler1 + 1;
                int beginX = stringEntityText.indexOf("\r\n10\r\n");
                int beginY = stringEntityText.indexOf("\r\n20\r\n", beginX);
                String strX = stringEntityText.substring(beginX, beginY).replace("\r\n10\r\n", "");
                int beginZ = stringEntityText.indexOf("\r\n30\r\n", beginY);
                String strY = stringEntityText.substring(beginY, beginZ).replace("\r\n20\r\n", "");
                markDefault_X = Double.parseDouble(strX);
                markDefault_Y = Double.parseDouble(strY);
                int beginText = stringEntityText.indexOf("\r\n1\r\n", beginZ);
                int endText = stringEntityText.indexOf("\r\n", beginText + 7);
                markDefault = stringEntityText.substring(beginText, endText).replace("\r\n1\r\n", "");
                int beginHHeightDefoultMark = stringEntityText.indexOf("\r\n40\r\n");
                int endHeightDefoultMark = stringEntityText.indexOf("\r\n1\r\n", beginHHeightDefoultMark);
                markDefaultHeight = Double.parseDouble(stringEntityText.substring(beginHHeightDefoultMark, endHeightDefoultMark).replace("\r\n40\r\n", ""));
                return handler;
            } else {
                return -1;
            }
        }

    }

    private String generateTextEntity(String mark, int handler, double textX, double textY, double markHeight) {
        String strHandler = Integer.toHexString(handler).toUpperCase();

        String result = "  0\n" +
                "TEXT\n" +
                "  5\n" +
                strHandler + "\n" +
                "  8\n" +
                "2\n" +
                " 62\n" +
                "     6\n" +
                " 10\n" +
                textX + "\n" +
                " 20\n" +
                textY + "\n" +
                " 30\n" +
                "0.0\n" +
                " 40\n" +
                markHeight + "\n" +
                "  1\n" +
                mark +
                "\n";

        return result;
    }

    private void fileSave(File fileInFolderDXF, String[] masStrok, List<String> listCircle) throws IOException {

        PrintWriter writerNewData = new PrintWriter(fileInFolderDXF, Charset.forName("windows-1251"));
        writerNewData.print("");

        writerNewData.print(masStrok[0]);

        for (String s : entityies) {
            writerNewData.print(s);
        }

        for (String s : listCircle) {
            writerNewData.print(s);
        }
        writerNewData.print(masStrok[2]);
        writerNewData.close();

        entityies.clear();
        listPointInsidePoz.clear();
    }

    private int countCharInString(String str, char simbol) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == simbol) {
                count++;
            }
        }
        return count;
    }

    public File[] getMassivFile(String path) {
        File directory = new File(path);
        return directory.listFiles();
    }

    public String readFileInString(String path) {

        //  String result = null;
        Charset encoding = Charset.forName("Windows-1251");
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded, encoding);

//        List<String> listStrok = null;
//        StringBuilder stb = new StringBuilder();
//        try {
//             result =  Files.readString(Paths.get(path)  );      //, StandardCharsets.US_ASCII);
//           // listStrok =   Files.readAllLines(Paths.get(path));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        for (String str : listStrok){
//            stb.append(str);
//        }


        // return result; //stb.toString();
    }

    public String[] breakStringOnThreeParts(String stroka) {
        String head = stroka.substring(0, stroka.indexOf("ENTITIES") + 10);      //    headFiles = headFiles.concat("ENTITIES\r\n");
        String str2 = stroka.substring(stroka.indexOf("ENTITIES") + 10);
        String entities = str2.substring(0, str2.indexOf("ENDSEC") - 3);
        String footer = str2.substring(str2.indexOf("ENDSEC") - 3, str2.length());

        String[] masStrok = new String[3];
        masStrok[0] = head;
        masStrok[1] = entities;
        masStrok[2] = footer;


        if (masStrok[0].contains("0\r\nTABLE\r\n2\r\nLAYER\r\n")) {
            boolean hasLayerName2WithoutSpsce = masStrok[0].contains("0\r\nLAYER\r\n2\r\n2\r\n");
            if (!hasLayerName2WithoutSpsce) {
                int index = masStrok[0].indexOf("0\r\nTABLE\r\n2\r\nLAYER\r\n");
                int index2 = masStrok[0].indexOf("0\r\nENDTAB", index);
                String str11 = masStrok[0].substring(0, index2);
                String str22 = masStrok[0].substring(index2);
                String newLayer = "0\r\n" +
                        "LAYER\r\n" +
                        "2\r\n" +
                        "2\r\n" +   //  имя слоя
                        "70\r\n" +
                        "0\r\n" +
                        "62\r\n" +
                        "6\r\n" +    //  цвет слоя
                        "6\r\n" +
                        "CONTINUOUS\r\n";
                String result = "";
                result = result.concat(str11).concat(newLayer).concat(str22);
                masStrok[0] = result;
            }
        }


        return masStrok;
    }

    public void breakMidlePartOnEntityes(String stringEntities) {

        while (stringEntities.contains("0\r\nPOLYLINE")) {
            int beginPoly = stringEntities.indexOf("0\r\nPOLYLINE");
            int endPoly = stringEntities.indexOf("SEQEND\r\n", beginPoly) + 8;
            if (endPoly == -1) {
                endPoly = stringEntities.length();
            }
            int lenght = stringEntities.length();
            if (lenght != endPoly) {
                char simvol = stringEntities.charAt(endPoly);
                ///  смотрим  символ
                // String test = stringEntities.substring(endPoly - 10, endPoly + 10);
                // System.out.println("Simvol =" + simvol + "  poz = " + poz);
                // System.out.println("Text = " + test);
                if (simvol == '5') {
                    int endPoly2 = stringEntities.indexOf("\r\n8\r\n", endPoly + 1) + 5;
                    endPoly = stringEntities.indexOf("\r\n", endPoly2) + 2;
                }
                String newEnt = stringEntities.substring(beginPoly, endPoly);
                entityies.add(newEnt);
                stringEntities = stringEntities.replace(newEnt, "");
            } else {
                String newEnt = stringEntities.substring(beginPoly, endPoly);
                entityies.add(newEnt);
                stringEntities = stringEntities.replace(newEnt, "");
            }
        }

        // округляем размеры габарита
        List<String> newListEntityes = new ArrayList<>();
        for (String ent : entityies) {
            if (ent.contains("POLYLINE")) {
                String[] masVertex = ent.split("VERTEX\r\n");
                for (int i = 1; i < masVertex.length; i++) {
                    int beginX = masVertex[i].indexOf("\r\n10\r\n");
                    int beginY = masVertex[i].indexOf("\r\n20\r\n", beginX);
                    String strX = masVertex[i].substring(beginX, beginY).replace("\r\n10\r\n", "");
                    double x = (double) Math.round(Double.parseDouble(strX));

                    if (masVertex[i].contains("\r\n30\r\n")) {
                        int beginZ = masVertex[i].indexOf("\r\n30\r\n", beginY);
                        String strY = masVertex[i].substring(beginY, beginZ).replace("\r\n20\r\n", "").trim();
                        double y = (double) Math.round(Double.parseDouble(strY));
                        masVertex[i] = masVertex[i].replace(strX, String.valueOf(x));
                        masVertex[i] = masVertex[i].replace(strY, String.valueOf(y));
                    } else if (masVertex[i].contains("\r\n42\r\n")) {

                        int beginZ = masVertex[i].indexOf("\r\n42\r\n", beginY);
                        String strY = masVertex[i].substring(beginY, beginZ);
                        strY = strY.replace("\r\n20\r\n", "").trim();
                        double y = (double) Math.round(Double.parseDouble(strY));
                        masVertex[i] = masVertex[i].replace(strX, String.valueOf(x));
                        masVertex[i] = masVertex[i].replace(strY, String.valueOf(y));

                    } else {
                        int beginZ = masVertex[i].indexOf("\r\n0\r\n", beginY);
                        String strY = masVertex[i].substring(beginY, beginZ);
                        strY = strY.replace("\r\n20\r\n", "").trim();
                        double y = (double) Math.round(Double.parseDouble(strY));
                        masVertex[i] = masVertex[i].replace(strX, String.valueOf(x));
                        masVertex[i] = masVertex[i].replace(strY, String.valueOf(y));
                    }
                    // pointsKonturList.add(new Point2D(Double.parseDouble(strX), Double.parseDouble(strY)));
                }
                StringJoiner joiner = new StringJoiner("VERTEX\r\n");
                for (String vertex : masVertex) {
                    joiner.add(vertex);
                }

                String newEnt = joiner.toString();         //  stringEntities.substring(beginPoly, endPoly2);
                newListEntityes.add(newEnt);
                //  stringEntities = stringEntities.replace(newEnt, "");
            } else {
                newListEntityes.add(ent);
            }
        }

        entityies = newListEntityes;


        if (stringEntities.contains("0\r\nTEXT")) {
            int beginTEXT = stringEntities.indexOf("0\r\nTEXT");
            int endTEXT = stringEntities.indexOf("\r\n73\r\n", beginTEXT) + 6;

            if (endTEXT == 5) {
                endTEXT = stringEntities.indexOf("\r\n1\r\n", beginTEXT) + 5;
            }

            int endTEXT2 = stringEntities.indexOf("\r\n", endTEXT) + 2;
            String newEnt = stringEntities.substring(beginTEXT, endTEXT2);
            entityies.add(newEnt);
            stringEntities = stringEntities.replace(newEnt, "");
        }

        // Удаляем DIMENSION
//            while (stringEntities.contains("0\r\nDIMENSION")) {
//                int beginPoly = stringEntities.indexOf("0\r\nDIMENSION");
//                int endPoly = stringEntities.indexOf("\r\n13\r\n", beginPoly);
//                if (endPoly == -1) {
//                    endPoly = stringEntities.indexOf("0\r\n", beginPoly + 5);
//                    if (endPoly == -1) {
//                        endPoly = stringEntities.length();
//                    }
//                    String newEnt = stringEntities.substring(beginPoly, endPoly);
//                    stringEntities = stringEntities.replace(newEnt, "");
//                    continue;
//                }
//
//                int endPoly2 = stringEntities.indexOf("0\r\n", endPoly);
//
//                if (endPoly2 == -1) {
//                    endPoly2 = stringEntities.length();
//                }
//                String newEnt = stringEntities.substring(beginPoly, endPoly2);
//                stringEntities = stringEntities.replace(newEnt, "");
//            }


        while (stringEntities.contains("0\r\nCIRCLE\r\n")) {
            int beginCir = stringEntities.indexOf("0\r\nCIRCLE\r\n");
            int endCir = stringEntities.indexOf("\r\n40\r\n", beginCir) + 6;
            int endCir2 = stringEntities.indexOf("\r\n", endCir) + 2;

            String newEnt = stringEntities.substring(beginCir, endCir2);
            entityies.add(newEnt);
            stringEntities = stringEntities.replace(newEnt, "");
        }

        vivodNaConsoleEntityies();
    }

    private void vivodNaConsoleEntityies() {

        // vivod  entityies  v konsole
        System.out.println();
        for (String str : entityies) {

            String sss = str.replace("\r\n", " ");
            System.out.println(sss);
        }
        System.out.println();
    }


    public List<String> modifyPolylineInCircle(/*List<String> entityies, List<Point2D> listPoint*/) {

        List<String> listCircle = new LinkedList<>();
        Iterator<String> iter2 = entityies.iterator();
        while (iter2.hasNext()) {

            String poly = iter2.next();
            if (poly.contains("0\r\nPOLYLINE\r\n8\r\n18\r\n") || poly.contains("0\r\nPOLYLINE\r\n8\r\n22\r\n") || poly.contains("0\r\nPOLYLINE\r\n8\r\n14\r\n")) {      //  удаляем все линии  на слое 18  и на слое 22
                List<Point2D> point2DList = new LinkedList<>();
                String[] masVertex = poly.split("VERTEX");
                for (int i = 1; i < masVertex.length; i++) {
                    int beginX = masVertex[i].indexOf("\r\n10\r\n");
                    int beginY = masVertex[i].indexOf("\r\n20\r\n", beginX);
                    String strX = masVertex[i].substring(beginX, beginY).replace("\r\n10\r\n", "");
                    int beginZ = masVertex[i].indexOf("\r\n30\r\n", beginY);
                    String strY = masVertex[i].substring(beginY, beginZ).replace("\r\n20\r\n", "");
                    point2DList.add(new Point2D(Double.parseDouble(strX), Double.parseDouble(strY)));
                }
                //
                double centrX;
                double centrY;
                double radius;
                if (point2DList.get(0).getX() == point2DList.get(1).getX()) {
                    centrX = point2DList.get(0).getX();
                } else {
                    throw new RuntimeException("coordinate X for Circle not defined");
                }

                if (point2DList.get(0).getY() < point2DList.get(1).getY()) {
                    centrY = point2DList.get(0).getY() + (point2DList.get(1).getY() - point2DList.get(0).getY()) / 2;
                    radius = (point2DList.get(1).getY() - point2DList.get(0).getY()) / 2;

                } else {
                    centrY = point2DList.get(1).getY() + (point2DList.get(0).getY() - point2DList.get(1).getY()) / 2;
                    radius = (point2DList.get(0).getY() - point2DList.get(1).getY()) / 2;
                }

                String res = "  0\nCIRCLE\n" +
                        "  5\n" +
                        "CC\n" +
                        "  8\n" +
                        "0\n" +
                        " 10\n" +
                        centrX + "\n" +
                        " 20\n" +
                        centrY + "\n" +
                        " 30\n" +
                        "0.0\n" +
                        " 40\n" +
                        radius + "\n";


                listCircle.add(res);

                circleTo5Points(centrX, centrY, radius);

                iter2.remove();
            }
        }

        return listCircle;
    }

    public double[] getGabaritPosition() {

        int breakLinesLenght = 10;                                        //  переменая говорит на какие отрезки будем разбивать линию

        List<List<Point2D>> listKontur = new LinkedList<>();

        for (String poly : entityies) {
            if (poly.contains("0\r\nPOLYLINE\r\n")) {

                List<Point2D> point2DList = new LinkedList<>();
                String[] masVertex = poly.split("VERTEX");

                for (int i = 1; i < masVertex.length; i++) {
                    int beginX = masVertex[i].indexOf("\r\n10\r\n");
                    int beginY = masVertex[i].indexOf("\r\n20\r\n", beginX);
                    String strX = masVertex[i].substring(beginX, beginY).replace("\r\n10\r\n", "");
                    if (masVertex[i].contains("\r\n30\r\n")) {
                        int beginZ = masVertex[i].indexOf("\r\n30\r\n", beginY);
                        String strY = masVertex[i].substring(beginY, beginZ).replace("\r\n20\r\n", "");
                        point2DList.add(new Point2D(Double.parseDouble(strX), Double.parseDouble(strY)));
                    } else if (masVertex[i].contains("\r\n42\r\n")) {
                        int beginZ = masVertex[i].indexOf("\r\n42\r\n", beginY);
                        String strY = masVertex[i].substring(beginY, beginZ).replace("\r\n20\r\n", "");
                        point2DList.add(new Point2D(Double.parseDouble(strX), Double.parseDouble(strY)));
                    } else {
                        int beginZ = masVertex[i].indexOf("\r\n0\r\n", beginY);
                        String strY = masVertex[i].substring(beginY, beginZ).replace("\r\n20\r\n", "");
                        point2DList.add(new Point2D(Double.parseDouble(strX), Double.parseDouble(strY)));
                    }
                }
                listKontur.add(point2DList);
            }
        }

        // todo do circle
        if (listKontur.size() == 0) {
            getPointsFromCircle();
            double Xmin = listPointInsidePoz.get(0).getX();
            double Xmax = listPointInsidePoz.get(0).getX();
            double Ymin = listPointInsidePoz.get(0).getY();
            double Ymax = listPointInsidePoz.get(0).getY();

            for (Point2D point2D : listPointInsidePoz) {
                if (point2D.getX() < Xmin) {
                    Xmin = point2D.getX();
                }
                if (point2D.getX() > Xmax) {
                    Xmax = point2D.getX();
                }
                if (point2D.getY() > Ymax) {
                    Ymax = point2D.getY();
                }
                if (point2D.getY() < Ymin) {
                    Ymin = point2D.getY();
                }
            }
            double[] res = new double[4];
            res[0] = Xmin;
            res[1] = Xmax;
            res[2] = Ymin;
            res[3] = Ymax;
            return res;
        }


        double Xmin = listKontur.get(0).get(0).getX();
        double Xmax = listKontur.get(0).get(0).getX();
        double Ymin = listKontur.get(0).get(0).getY();
        double Ymax = listKontur.get(0).get(0).getY();

        for (List<Point2D> pointsKonturList : listKontur) {
            for (Point2D point2D : pointsKonturList) {
                if (point2D.getX() < Xmin) {
                    Xmin = point2D.getX();
                }
                if (point2D.getX() > Xmax) {
                    Xmax = point2D.getX();
                }
                if (point2D.getY() > Ymax) {
                    Ymax = point2D.getY();
                }
                if (point2D.getY() < Ymin) {
                    Ymin = point2D.getY();
                }
            }
        }
        double[] res = new double[4];
        res[0] = Xmin;
        res[1] = Xmax;
        res[2] = Ymin;
        res[3] = Ymax;


        for (List<Point2D> pointsKonturList : listKontur) {
            Point2D pointBeginLine = pointsKonturList.get(0);
            for (int i = 1; i < pointsKonturList.size(); i++) {
                Point2D pointEndLine = pointsKonturList.get(i);
                if (!isGabaritLine(pointBeginLine, pointEndLine, Xmin, Xmax, Ymin, Ymax)) {
                    //  System.out.println(pointBeginLine + " and " + pointEndLine + " no gabarit line");
                    breakNoGabaritLineOnPoints(pointEndLine, pointBeginLine, breakLinesLenght);
                    //  System.out.println();
                } else {
                    //  System.out.println(pointBeginLine + " and " + pointEndLine + " === gabarit line");
                    //  System.out.println();
                    //   boolean isLineGorizontGabarit = (beginLine.getY() == endLine.getY()) && (beginLine.getY() == Ymin || beginLine.getY() == Ymax);
                    getLenghtDownAndUpLines(Ymin, Ymax, pointBeginLine, pointEndLine);
                }
                pointBeginLine = pointEndLine;
            }

            // линия между конечной и нулевой точками в списке point2DList
            pointBeginLine = pointsKonturList.get(pointsKonturList.size() - 1);
            Point2D endLine = pointsKonturList.get(0);
            if (!isGabaritLine(pointBeginLine, endLine, Xmin, Xmax, Ymin, Ymax)) {
                // System.out.println(pointBeginLine + " and " + endLine + " no gabarit line");
                breakNoGabaritLineOnPoints(endLine, pointBeginLine, breakLinesLenght);
                //System.out.println();
            } else {
                // System.out.println(pointBeginLine + " and " + endLine + " === gabarit line");
                //System.out.println();

                getLenghtDownAndUpLines(Ymin, Ymax, pointBeginLine, endLine);
            }
        }
        getPointsFromCircle();
        return res;
    }

    private void getLenghtDownAndUpLines(double ymin, double ymax, Point2D pointBeginLine, Point2D pointEndLine) {
        if ((pointBeginLine.getY() == pointEndLine.getY()) && (pointBeginLine.getY() == ymin)) {
            lenghtDownLines = lenghtDownLines + (int) Math.sqrt((pointEndLine.getX() - pointBeginLine.getX()) * (pointEndLine.getX() - pointBeginLine.getX()));
        }
        if ((pointBeginLine.getY() == pointEndLine.getY()) && (pointBeginLine.getY() == ymax)) {
            lenghtUpLines = lenghtUpLines + (int) Math.sqrt((pointEndLine.getX() - pointBeginLine.getX()) * (pointEndLine.getX() - pointBeginLine.getX()));
        }
    }

    private void circleTo5Points(double centrX, double centrY, double radius) {
        listPointInsidePoz.add(new Point2D(centrX - radius, centrY - radius));
        listPointInsidePoz.add(new Point2D(centrX + radius, centrY + radius));
        listPointInsidePoz.add(new Point2D(centrX - radius, centrY + radius));
        listPointInsidePoz.add(new Point2D(centrX + radius, centrY - radius));
        listPointInsidePoz.add(new Point2D(centrX, centrY));
    }

    private void getPointsFromCircle() {
//        if (hasSpaces) {
//            for (String ent : entityies) {
//                if (ent.contains("\r\nCIRCLE\r\n")) {
//                    int beginX = ent.indexOf("\r\n 10\r\n");
//                    int beginY = ent.indexOf("\r\n 20\r\n", beginX);
//                    String strX = ent.substring(beginX, beginY).replace("\r\n 10\r\n", "");
//                    int beginZ = ent.indexOf("\r\n 30\r\n", beginY);
//                    String strY = ent.substring(beginY, beginZ).replace("\r\n 20\r\n", "");
//                    int beginRadius = ent.indexOf("\r\n 40\r\n", beginZ);
//                    int endRadius = ent.length();
//                    String strRadius = ent.substring(beginRadius, endRadius).replace("\r\n 40\r\n", "");
//                    circleTo5Points(Double.parseDouble(strX), Double.parseDouble(strY), Double.parseDouble(strRadius));
//                }
//            }
//        } else {
        for (String ent : entityies) {
            if (ent.contains("\r\nCIRCLE\r\n")) {
                int beginX = ent.indexOf("\r\n10\r\n");
                int beginY = ent.indexOf("\r\n20\r\n", beginX);
                String strX = ent.substring(beginX, beginY).replace("\r\n10\r\n", "");
                if (ent.contains("\r\n30\r\n")) {
                    int beginZ = ent.indexOf("\r\n30\r\n", beginY);
                    String strY = ent.substring(beginY, beginZ).replace("\r\n20\r\n", "");
                    int beginRadius = ent.indexOf("\r\n40\r\n", beginZ);
                    int endRadius = ent.length();
                    String strRadius = ent.substring(beginRadius, endRadius).replace("\r\n40\r\n", "");
                    circleTo5Points(Double.parseDouble(strX), Double.parseDouble(strY), Double.parseDouble(strRadius));
                } else {
                    int beginZ = ent.indexOf("\r\n40\r\n", beginY);
                    String strY = ent.substring(beginY, beginZ).replace("\r\n20\r\n", "");
                    int endRadius = ent.length();
                    String strRadius = ent.substring(beginZ, endRadius).replace("\r\n40\r\n", "");
                    circleTo5Points(Double.parseDouble(strX), Double.parseDouble(strY), Double.parseDouble(strRadius));
                }

            }
        }
    }

    // }

    public boolean findPlaceForMark(double[] gabaritPoz, boolean markaStolbom, Rectangle ramkaMark, int height, TypeMark typeMark) {

        double Xmin = gabaritPoz[0];
        double Xmax = gabaritPoz[1];
        double Ymin = gabaritPoz[2];
        double Ymax = gabaritPoz[3];

        int shirinaPoz = (int) Math.abs(Ymax - Ymin);
        int dlinnaPoz = (int) Math.abs(Xmax - Xmin);

        //  находим центр детали
        int Xcntr = (int) (Xmin + (Xmax - Xmin) / 2);
        int Ycntr = (int) (Ymin + (Ymax - Ymin) / 2);
        System.out.printf(" центр детали (%s,%s)", Xcntr, Ycntr);
        System.out.println();


        //    рамка ширина = 40   рамка длинна = 90                               рамка  40 х 90
        int XminRamka = (int) (Xcntr - ramkaMark.getDlinna() / 2);
        int XmaxRamka = (int) (Xcntr + ramkaMark.getDlinna() / 2);
        int YminRamka = (int) (Ycntr - ramkaMark.getShirina() / 2);
        int YmaxRamka = (int) (Ycntr + ramkaMark.getShirina() / 2);
        int XminRamkaCentr = XminRamka;
        int XmaxRamkaCentr = XmaxRamka;
        int YminRamkaCentr = YminRamka;
        int YmaxRamkaCentr = YmaxRamka;

        int shiftCoordinateX = dlinnaPoz / 100;                         //10;
        if (shiftCoordinateX == 0) {
            shiftCoordinateX = 1;
        }
        int shiftCoordinateY = shirinaPoz / 100;                         //10;
        if (shiftCoordinateY == 0) {
            shiftCoordinateY = 1;
        }
        System.out.printf(" Рамка (40х90)  min(%s,%s) max(%s,%s)", XminRamka, YminRamka, XmaxRamka, YmaxRamka);
        System.out.println();
        // главный цикл

        boolean findPlace = false;
        boolean flag = true;

        if (typeMark.equals(TypeMark.ONLY_POZ)) {
            //    flag = true;
            XminRamka = XminRamkaCentr;
            XmaxRamka = XmaxRamkaCentr;
            YminRamka = (int) Ymin;
            YmaxRamka = YminRamka + (int) ramkaMark.getShirina();


            // рамка пошла вверх
            while (flag) {
                boolean tochkaVRamke = false;
                if (YmaxRamka < Ymax) {
                    for (Point2D pnt : listPointInsidePoz) {
                        if (pnt.getX() > XminRamka && pnt.getX() < XmaxRamka && pnt.getY() > YminRamka && pnt.getY() < YmaxRamka) {
                            tochkaVRamke = true;
                            break;
                        }
                    }
                } else {
                    break;
                }

                if (tochkaVRamke) {
                    YminRamka = YminRamka + shiftCoordinateY;
                    YmaxRamka = YmaxRamka + shiftCoordinateY;
                } else {
                    findPlace = true;
                    maxBoundRamka(XminRamka, YminRamka, XmaxRamka, YmaxRamka, gabaritPoz, shiftCoordinateY * 3, ramkaMark, height, typeMark);
                    flag = false;
                }
            }

        } else {
            //private int lenghtDownLines = 0;
            //private int lenghtUpLines = 0;
            if (lenghtDownLines == lenghtUpLines) {
                // XminRamka = XminRamkaCentr;
                //XmaxRamka = XmaxRamkaCentr;
                //YminRamka = YminRamkaCentr;
                //YmaxRamka = YmaxRamkaCentr;
                findPlace = ramkaGoUp(gabaritPoz, ramkaMark, height, typeMark, Ymax, XminRamka, XmaxRamka, YminRamka, YmaxRamka, shiftCoordinateY);


                if (!findPlace) {
                    // XminRamka = XminRamkaCentr;
                    // XmaxRamka = XmaxRamkaCentr;
                    //YminRamka = YminRamkaCentr;
                    //YmaxRamka = YmaxRamkaCentr;
                    findPlace = ramkaGoDown(gabaritPoz, ramkaMark, height, typeMark, Ymin, XminRamka, XmaxRamka, YminRamka, YmaxRamka, shiftCoordinateY);
                }


                if (!findPlace) {
                    // XminRamka = XminRamkaCentr;
                    // XmaxRamka = XmaxRamkaCentr;
                    //YminRamka = YminRamkaCentr;
                    //YmaxRamka = YmaxRamkaCentr;
                    findPlace = ramkaGoLeft(gabaritPoz, ramkaMark, height, typeMark, Xmin, XminRamka, XmaxRamka, YminRamka, YmaxRamka, shiftCoordinateX);
                }

                if (!findPlace) {
                    // XminRamka = XminRamkaCentr;
                    //XmaxRamka = XmaxRamkaCentr;
                    //YminRamka = YminRamkaCentr;
                    //YmaxRamka = YmaxRamkaCentr;
                    findPlace = ramkaGoRight(gabaritPoz, ramkaMark, height, typeMark, Xmax, XminRamka, XmaxRamka, YminRamka, YmaxRamka, shiftCoordinateX);
                }
            } else if (lenghtDownLines > lenghtUpLines) {

                XminRamka = (int) (Xcntr - ramkaMark.getDlinna() / 2);
                XmaxRamka = (int) (Xcntr + ramkaMark.getDlinna() / 2);
                YminRamka = (int) (Ycntr - ramkaMark.getShirina() / 2);
                YmaxRamka = (int) (Ycntr + ramkaMark.getShirina() / 2);


                findPlace = checkMarkInCentrPozition(gabaritPoz, height, typeMark, XminRamka, XmaxRamka, ramkaMark, YminRamka, YmaxRamka, shiftCoordinateY);


                if (!findPlace) {
                    YminRamka = (int) Ymin;
                    YmaxRamka = YminRamka + (int) ramkaMark.getShirina();

                    findPlace = ramkaGoUp(gabaritPoz, ramkaMark, height, typeMark, Ymax, XminRamka, XmaxRamka, YminRamka, YmaxRamka, shiftCoordinateY);
                }
                if (!findPlace) {
                    findPlace = ramkaGoLeft(gabaritPoz, ramkaMark, height, typeMark, Xmin, XminRamka, XmaxRamka, YminRamka, YmaxRamka, shiftCoordinateX);
                }
                if (!findPlace) {
                    findPlace = ramkaGoRight(gabaritPoz, ramkaMark, height, typeMark, Xmax, XminRamka, XmaxRamka, YminRamka, YmaxRamka, shiftCoordinateX);
                }

            } else {

                XminRamka = (int) (Xcntr - ramkaMark.getDlinna() / 2);
                XmaxRamka = (int) (Xcntr + ramkaMark.getDlinna() / 2);
                YminRamka = (int) (Ycntr - ramkaMark.getShirina() / 2);
                YmaxRamka = (int) (Ycntr + ramkaMark.getShirina() / 2);


                findPlace = checkMarkInCentrPozition(gabaritPoz, height, typeMark, XminRamka, XmaxRamka, ramkaMark, YminRamka, YmaxRamka, shiftCoordinateY);

                if (!findPlace) {
                    YmaxRamka = (int) Ymax;
                    YminRamka = YmaxRamka - (int) ramkaMark.getShirina();
                    findPlace = ramkaGoDown(gabaritPoz, ramkaMark, height, typeMark, Ymax, XminRamka, XmaxRamka, YminRamka, YmaxRamka, shiftCoordinateY);
                }
                if (!findPlace) {
                    findPlace = ramkaGoLeft(gabaritPoz, ramkaMark, height, typeMark, Xmin, XminRamka, XmaxRamka, YminRamka, YmaxRamka, shiftCoordinateX);
                }
                if (!findPlace) {
                    findPlace = ramkaGoRight(gabaritPoz, ramkaMark, height, typeMark, Xmax, XminRamka, XmaxRamka, YminRamka, YmaxRamka, shiftCoordinateX);
                }

            }
        }

        lenghtDownLines = 0;
        lenghtUpLines = 0;
        return findPlace;
    }

    private boolean checkMarkInCentrPozition(double[] gabaritPoz, int height, TypeMark typeMark, int xminRamka, int xmaxRamka, Rectangle ramkaMark, int yminRamka, int ymaxRamka, int shiftCoordinateY) {
        boolean findPlace = false;
        boolean tochkaVRamke = false;
        for (Point2D pnt : listPointInsidePoz) {
            if (pnt.getX() > xminRamka && pnt.getX() < xmaxRamka && pnt.getY() > yminRamka && pnt.getY() < ymaxRamka) {
                tochkaVRamke = true;
                break;
            }
        }
        if (!tochkaVRamke) {
            maxBoundRamka(xminRamka, yminRamka, xmaxRamka, ymaxRamka, gabaritPoz, shiftCoordinateY * 3, ramkaMark, height, typeMark);
            findPlace = true;
        }
        return findPlace;
    }

    private boolean ramkaGoRight(double[] gabaritPoz, Rectangle ramkaMark, int height, TypeMark typeMark, double xmax, int xminRamka, int xmaxRamka, int yminRamka, int ymaxRamka, int shiftCoordinateX) {
        // рамка пошла в право

        boolean findPlace = false;
        boolean flag = true;
        while (flag) {
            boolean tochkaVRamke = false;
            if (xmaxRamka < xmax) {
                for (Point2D pnt : listPointInsidePoz) {
                    if (pnt.getX() > xminRamka && pnt.getX() < xmaxRamka && pnt.getY() > yminRamka && pnt.getY() < ymaxRamka) {
                        tochkaVRamke = true;
                        break;
                    }
                }
            } else {
                // flag = false;
                break;
            }

            if (tochkaVRamke) {
                xminRamka = xminRamka + shiftCoordinateX;
                xmaxRamka = xmaxRamka + shiftCoordinateX;
            } else {
                findPlace = true;
                maxBoundRamka(xminRamka, yminRamka, xmaxRamka, ymaxRamka, gabaritPoz, shiftCoordinateX * 3, ramkaMark, height, typeMark);
                flag = false;
            }
        }
        return findPlace;
    }

    private boolean ramkaGoLeft(double[] gabaritPoz, Rectangle ramkaMark, int height, TypeMark typeMark, double xmin, int xminRamka, int xmaxRamka, int yminRamka, int ymaxRamka, int shiftCoordinateX) {
        boolean findPlace = false;
        boolean flag = true;
        while (flag) {
            boolean tochkaVRamke = false;
            if (xminRamka > xmin) {
                for (Point2D pnt : listPointInsidePoz) {
                    if (pnt.getX() > xminRamka && pnt.getX() < xmaxRamka && pnt.getY() > yminRamka && pnt.getY() < ymaxRamka) {
                        tochkaVRamke = true;
                        break;
                    }
                }
            } else {
                // flag = false;
                break;
            }

            if (tochkaVRamke) {
                xminRamka = xminRamka - shiftCoordinateX;
                xmaxRamka = xmaxRamka - shiftCoordinateX;
            } else {
                findPlace = true;
                maxBoundRamka(xminRamka, yminRamka, xmaxRamka, ymaxRamka, gabaritPoz, shiftCoordinateX * 3, ramkaMark, height, typeMark);
                flag = false;
            }
        }
        return findPlace;
    }

    private boolean ramkaGoDown(double[] gabaritPoz, Rectangle ramkaMark, int height, TypeMark typeMark, double ymin, int xminRamka, int xmaxRamka, int yminRamka, int ymaxRamka, int shiftCoordinateY) {
        boolean findPlace = false;
        boolean flag = true;
        while (flag) {
            boolean tochkaVRamke = false;
            if (yminRamka > ymin) {
                for (Point2D pnt : listPointInsidePoz) {
                    if (pnt.getX() > xminRamka && pnt.getX() < xmaxRamka && pnt.getY() > yminRamka && pnt.getY() < ymaxRamka) {
                        tochkaVRamke = true;
                        break;
                    }
                }
            } else {
                // flag = false;
                break;
            }

            if (tochkaVRamke) {
                yminRamka = yminRamka - shiftCoordinateY;
                ymaxRamka = ymaxRamka - shiftCoordinateY;
            } else {
                findPlace = true;
                maxBoundRamka(xminRamka, yminRamka, xmaxRamka, ymaxRamka, gabaritPoz, shiftCoordinateY * 3, ramkaMark, height, typeMark);
                flag = false;
            }
        }
        return findPlace;
    }

    private boolean ramkaGoUp(double[] gabaritPoz, Rectangle ramkaMark, int height, TypeMark typeMark, double ymax, int xminRamka, int xmaxRamka, int yminRamka, int ymaxRamka, int shiftCoordinateY) {
        // рамка пошла вверх
        boolean findPlace = false;
        boolean flag;
        flag = true;
        while (flag) {
            boolean tochkaVRamke = false;
            if (ymaxRamka < ymax) {
                for (Point2D pnt : listPointInsidePoz) {
                    if (pnt.getX() > xminRamka && pnt.getX() < xmaxRamka && pnt.getY() > yminRamka && pnt.getY() < ymaxRamka) {
                        tochkaVRamke = true;
                        break;
                    }
                }
            } else {
                break;
            }

            if (tochkaVRamke) {
                yminRamka = yminRamka + shiftCoordinateY;
                ymaxRamka = ymaxRamka + shiftCoordinateY;
            } else {
                findPlace = true;
                maxBoundRamka(xminRamka, yminRamka, xmaxRamka, ymaxRamka, gabaritPoz, shiftCoordinateY * 3, ramkaMark, height, typeMark);
                flag = false;
            }
        }
        return findPlace;
    }

    private void maxBoundRamka(int XminRamka, int YminRamka, int XmaxRamka, int YmaxRamka, double[] res,
                               int shiftCoordinate, Rectangle ramkaMark, int height, TypeMark typeMark) {

        height--;
        double Xmin = res[0];
        double Xmax = res[1];
        double Ymin = res[2];
        double Ymax = res[3];
        int maxBound = 500;

        // ищем нижнюю границу
        System.out.println("   ищем нижнюю границу рамки");

        int beginBound = 0;
        boolean uslovie = true;
        boolean tochkaVRamke = false;
        while ((YminRamka > Ymin) && uslovie && (beginBound < maxBound)) {
            for (Point2D pnt : listPointInsidePoz) {
                if (pnt.getX() > XminRamka && pnt.getX() < XmaxRamka && pnt.getY() > YminRamka && pnt.getY() < YmaxRamka) {
                    tochkaVRamke = true;
                    break;
                }
            }
            if (tochkaVRamke) {
                uslovie = false;
            } else {
                YminRamka = YminRamka - shiftCoordinate;
                beginBound += shiftCoordinate;
            }
            System.out.printf(" Рамка (40х90)  min(%s,%s) max(%s,%s)", XminRamka, YminRamka, XmaxRamka, YmaxRamka);
            System.out.println();
        }
        if (YminRamka < Ymin || tochkaVRamke) {
            YminRamka = YminRamka + shiftCoordinate;
        }

        // ищем нижнюю границу
        System.out.println("   ищем верхнею границу рамки");
        beginBound = 0;
        uslovie = true;
        tochkaVRamke = false;
        while ((YmaxRamka < Ymax) && uslovie && (beginBound < maxBound)) {
            for (Point2D pnt : listPointInsidePoz) {
                if (pnt.getX() > XminRamka && pnt.getX() < XmaxRamka && pnt.getY() > YminRamka && pnt.getY() < YmaxRamka) {
                    tochkaVRamke = true;
                    break;
                }
            }
            if (tochkaVRamke) {
                uslovie = false;
            } else {
                YmaxRamka = YmaxRamka + shiftCoordinate;
                beginBound += shiftCoordinate;
            }
            System.out.printf(" Рамка (40х90)  min(%s,%s) max(%s,%s)", XminRamka, YminRamka, XmaxRamka, YmaxRamka);
            System.out.println();
        }
        if (YmaxRamka > Ymax || tochkaVRamke) {
            YmaxRamka = YmaxRamka - shiftCoordinate;
        }

        // ищем левую границу
        System.out.println("   ищем левую границу рамки");
        beginBound = 0;
        uslovie = true;
        tochkaVRamke = false;
        while ((XminRamka > Xmin) && uslovie && (beginBound < maxBound)) {
            for (Point2D pnt : listPointInsidePoz) {
                if (pnt.getX() > XminRamka && pnt.getX() < XmaxRamka && pnt.getY() > YminRamka && pnt.getY() < YmaxRamka) {
                    tochkaVRamke = true;
                    break;
                }
            }
            if (tochkaVRamke) {
                uslovie = false;
            } else {
                XminRamka = XminRamka - shiftCoordinate;
                beginBound += shiftCoordinate;
            }
            System.out.printf(" Рамка (40х90)  min(%s,%s) max(%s,%s)", XminRamka, YminRamka, XmaxRamka, YmaxRamka);
            System.out.println();
        }
        if (XminRamka < Xmin || tochkaVRamke) {
            XminRamka = XminRamka + shiftCoordinate;
        }

        // ищем левую границу
        System.out.println("   ищем правую границу рамки");
        beginBound = 0;
        uslovie = true;
        tochkaVRamke = false;
        while ((XmaxRamka < Xmax) && uslovie && (beginBound < maxBound)) {
            for (Point2D pnt : listPointInsidePoz) {
                if (pnt.getX() > XminRamka && pnt.getX() < XmaxRamka && pnt.getY() > YminRamka && pnt.getY() < YmaxRamka) {
                    tochkaVRamke = true;
                    break;
                }
            }
            if (tochkaVRamke) {
                uslovie = false;
            } else {
                XmaxRamka = XmaxRamka + shiftCoordinate;
                beginBound += shiftCoordinate;
            }
            System.out.printf(" Рамка (40х90)  min(%s,%s) max(%s,%s)", XminRamka, YminRamka, XmaxRamka, YmaxRamka);
            System.out.println();
        }
        if (XmaxRamka > Xmax || tochkaVRamke) {
            XmaxRamka = XmaxRamka - shiftCoordinate;
        }
        System.out.println("----------------------------------------");
        System.out.printf(" результат  min(%s,%s) max(%s,%s)\n", XminRamka, YminRamka, XmaxRamka, YmaxRamka);

        int pointCentrXnewRamki = XminRamka + (XmaxRamka - XminRamka) / 2;
        int pointCentrYnewRamki = YminRamka + (YmaxRamka - YminRamka) / 2;
        System.out.printf("centr (%s,%s)\n", pointCentrXnewRamki, pointCentrYnewRamki);

        //  String markirovka = "164-MD-145";
        //  String zakaz = "0286645";
        // String steelGrade = "09г2с";
        //  (int XminRamka, int YminRamka, int XmaxRamka, int YmaxRamka, double[] res, int shiftCoordinate, Rectangle ramkaMark, int height, TypeMark typeMark)
        if (typeMark.equals(TypeMark.VERTICAL)) {
            addThreeTextOnEntyties(pointCentrXnewRamki, pointCentrYnewRamki, height);
        } else if (typeMark.equals(TypeMark.HORIZONTAL)) {
            addTwoTextOnEntyties(pointCentrXnewRamki, pointCentrYnewRamki, height);
        } else if (typeMark.equals(TypeMark.ONLY_POZ)) {
            addOneTextOnEntyties(pointCentrXnewRamki, pointCentrYnewRamki, height);
        }


    }

    private void addOneTextOnEntyties(int pointCentrXnewRamki, int pointCentrYnewRamki, int height) {
        // int heightRanki = height/2;
        double lenghtPoz = getLenghtStr(poz) * height;


        double pointBeginTextX = pointCentrXnewRamki - lenghtPoz / 2;
        double pointBeginTextY = pointCentrYnewRamki - height / 2;
        String newText = "  0\n" +
                "TEXT\n" +
                "  5\n" +
                "447\n" +
                "  8\n" +
                "2\n" +
                " 62\n" +
                "6\n" +
                " 10\n" +
                pointBeginTextX + "\n" +
                " 20\n" +
                pointBeginTextY + "\n" +
                " 30\n" +
                "0.0\n" +
                " 40\n" +
                height + ".0\n" +
                "  1\n" +
                poz + "\n" +
                " 11\n" +
                pointBeginTextX + "\n" +
                " 21\n" +
                pointBeginTextY + "\n" +
                " 31\n" +
                "0.0\n";
        entityies.add(newText);

    }

    private void addTwoTextOnEntyties(int pointCentrXnewRamki, int pointCentrYnewRamki, int height) {

        //  double maxDlinnaMark = getLenghtStr(poz) * height + 10 * (getLenghtStr(" " + zakaz + " " + gradeSteel));
        double lenghtPoz = getLenghtStr(poz) * height + 10 * (getLenghtStr(" " + zakaz + " " + gradeSteel));


        double pointBeginTextX = pointCentrXnewRamki - lenghtPoz / 2;
        double pointBeginTextY = pointCentrYnewRamki - (double) height / 2;
        String newText = "  0\n" +
                "TEXT\n" +
                "  5\n" +
                "447\n" +
                "  8\n" +
                "2\n" +
                " 62\n" +
                "6\n" +
                " 10\n" +
                pointBeginTextX + "\n" +
                " 20\n" +
                pointBeginTextY + "\n" +
                " 30\n" +
                "0.0\n" +
                " 40\n" +
                height + ".0\n" +
                "  1\n" +
                poz + "\n" +
                " 11\n" +
                pointBeginTextX + "\n" +
                " 21\n" +
                pointBeginTextY + "\n" +
                " 31\n" +
                "0.0\n";
        entityies.add(newText);

        //  lenghtPoz = getLenghtStr(zakaz);

        pointBeginTextX = pointBeginTextX + getLenghtStr(poz) * height;
        //  pointBeginTextY = pointCentrYnewRamki - 10;
        String str = " " + zakaz + " " + gradeSteel;
        String newTextZakaz = "  0\n" +
                "TEXT\n" +
                "  5\n" +
                "448\n" +
                "  8\n" +
                "2\n" +
                " 62\n" +
                "6\n" +
                " 10\n" +
                pointBeginTextX + "\n" +
                " 20\n" +
                pointBeginTextY + "\n" +
                " 30\n" +
                "0.0\n" +
                " 40\n" +
                "10.0\n" +
                "  1\n" +
                str + "\n" +
                " 11\n" +
                pointBeginTextX + "\n" +
                " 21\n" +
                pointBeginTextY + "\n" +
                " 31\n" +
                "0.0\n";
        entityies.add(newTextZakaz);

    }

    private void addThreeTextOnEntyties(int pointCentrXnewRamki, int pointCentrYnewRamki, int height) {

        int heightRanki = 10 + 5 + 10 + 5 + height;
        double lenghtPoz = getLenghtStr(poz) * height;


        double pointBeginTextX = pointCentrXnewRamki - lenghtPoz / 2;
        // double pointBeginTextY = pointCentrYnewRamki + 5;
        double pointBeginTextY = pointCentrYnewRamki + (heightRanki / 2) - height;
        String newText = "  0\n" +
                "TEXT\n" +
                "  5\n" +
                "447\n" +
                "  8\n" +
                "2\n" +
                " 62\n" +
                "6\n" +
                " 10\n" +
                pointBeginTextX + "\n" +
                " 20\n" +
                pointBeginTextY + "\n" +
                " 30\n" +
                "0.0\n" +
                " 40\n" +
                height + ".0\n" +
                "  1\n" +
                poz + "\n" +
                " 11\n" +
                pointBeginTextX + "\n" +
                " 21\n" +
                pointBeginTextY + "\n" +
                " 31\n" +
                "0.0\n";
        entityies.add(newText);

        lenghtPoz = getLenghtStr(zakaz) * 10;
        pointBeginTextX = pointCentrXnewRamki - lenghtPoz / 2;
        pointBeginTextY = pointBeginTextY - 15;
        String newTextZakaz = "  0\n" +
                "TEXT\n" +
                "  5\n" +
                "448\n" +
                "  8\n" +
                "2\n" +
                " 62\n" +
                "6\n" +
                " 10\n" +
                pointBeginTextX + "\n" +
                " 20\n" +
                pointBeginTextY + "\n" +
                " 30\n" +
                "0.0\n" +
                " 40\n" +
                "10.0\n" +
                "  1\n" +
                zakaz + "\n" +
                " 11\n" +
                pointBeginTextX + "\n" +
                " 21\n" +
                pointBeginTextY + "\n" +
                " 31\n" +
                "0.0\n";
        entityies.add(newTextZakaz);


        lenghtPoz = getLenghtStr(gradeSteel) * 10;

        pointBeginTextX = pointCentrXnewRamki - lenghtPoz / 2;
        pointBeginTextY = pointBeginTextY - 15;
        String newTextSteelGrade = "  0\n" +
                "TEXT\n" +
                "  5\n" +
                "449\n" +
                "  8\n" +
                "2\n" +
                " 62\n" +
                "6\n" +
                " 10\n" +
                pointBeginTextX + "\n" +
                " 20\n" +
                pointBeginTextY + "\n" +
                " 30\n" +
                "0.0\n" +
                " 40\n" +
                "10.0\n" +
                "  1\n" +
                gradeSteel + "\n" +
                " 11\n" +
                pointBeginTextX + "\n" +
                " 21\n" +
                pointBeginTextY + "\n" +
                " 31\n" +
                "0.0\n";
        entityies.add(newTextSteelGrade);


    }


    private boolean isGabaritLine(Point2D beginLine, Point2D endLine, double Xmin, double Xmax, double Ymin, double Ymax) {
        boolean isLineVerticalGabarit = (beginLine.getX() == endLine.getX()) && (beginLine.getX() == Xmin || beginLine.getX() == Xmax);
        boolean isLineGorizontGabarit = (beginLine.getY() == endLine.getY()) && (beginLine.getY() == Ymin || beginLine.getY() == Ymax);
        return isLineGorizontGabarit || isLineVerticalGabarit;
    }

    private void breakNoGabaritLineOnPoints(Point2D endLine, Point2D beginLine, int breakLinesLenght) {
        double lenghtLine = Math.sqrt((endLine.getX() - beginLine.getX()) * (endLine.getX() - beginLine.getX()) + (endLine.getY() - beginLine.getY()) * (endLine.getY() - beginLine.getY()));
        System.out.println("длинна линии =" + lenghtLine);
        int countFor = (int) lenghtLine / breakLinesLenght;
        double deltaX = (endLine.getX() - beginLine.getX()) / countFor;
        double deltaY = (endLine.getY() - beginLine.getY()) / countFor;
        double x = beginLine.getX();
        double y = beginLine.getY();

        for (int j = 0; j < countFor; j++) {
            x += deltaX;
            y += deltaY;
            listPointInsidePoz.add(new Point2D(x, y));
        }
    }

    public double getLenghtStr(String stroka) {

        int count = stroka.length();
        double dlinnnna = 0.0;
        for (int i = 0; i < count; i++) {

            Character simvolACAD = stroka.charAt(i);

            switch (simvolACAD) {
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'd':
                case 'E':
                case 'F':
                case 'f':
                case 'G':
                case 'H':
                    dlinnnna += 1;
                    break;
                case 'I':
                    dlinnnna += 0.66;
                    break;
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'p':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'v':
                    dlinnnna += 1;
                    break;
                case 'V':
                case 'W':
                    dlinnnna += 1.3333;
                    break;
                case 'X':
                case 'Y':
                case 'Z':
                case 'z':

                    dlinnnna += 1;
                    break;
                case '1':
                    dlinnnna += 0.6666;
                    break;
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    dlinnnna += 1;
                    break;
                case '0':
                    dlinnnna += 0.8333;
                    break;
                case '-':
                    dlinnnna += 1;
                    break;
                // RUS
                case 'А':
                case 'Б':
                case 'б':
                case 'В':
                case 'в':
                case 'Г':
                case 'д':
                    dlinnnna += 1;
                    break;
                case 'Д':
                    dlinnnna += 1.33;
                    break;
                case 'Е':
                    dlinnnna += 1;
                    break;
                case 'Ё':
                    dlinnnna += 1;
                    break;
                case 'Ж':
                    dlinnnna += 1.33;
                    break;
                case 'З':
                    dlinnnna += 1;
                    break;
                case 'И':
                    dlinnnna += 1;
                    break;
                case 'Й':
                    dlinnnna += 1;
                    break;
                case 'К':
                    dlinnnna += 1;
                    break;
                case 'Л':
                    dlinnnna += 1.16;
                    break;
                case 'М':
                    dlinnnna += 1;
                    break;
                case 'Н':
                    dlinnnna += 1;
                    break;
                case 'О':
                    dlinnnna += 1;
                    break;
                case 'П':
                    dlinnnna += 1;
                    break;
                case 'Р':
                    dlinnnna += 1;
                    break;
                case 'С':
                    dlinnnna += 1;
                    break;
                case 'Т':
                    dlinnnna += 1;
                    break;
                case 'У':
                    dlinnnna += 1;
                    break;
                case 'Ф':
                    dlinnnna += 1;
                    break;
                case 'Х':
                    dlinnnna += 1;
                    break;
                case 'Ц':
                    dlinnnna += 1.16;
                    break;
                case 'Ч':
                    dlinnnna += 1;
                    break;
                case 'Ш':
                    dlinnnna += 1.33;
                    break;
                case 'Щ':
                    dlinnnna += 1.5;
                    break;
                case 'Ъ':
                    dlinnnna += 1.16;
                    break;
                case 'Ы':
                    dlinnnna += 1.16;
                    break;
                case 'Ь':
                    dlinnnna += 1;
                    break;
                case 'Э':
                    dlinnnna += 1;
                    break;
                case 'Ю':
                    dlinnnna += 1;
                    break;
                case 'Я':
                    dlinnnna += 1;
                    break;


                // DIGIT
                case ':':
                case '.':
                    dlinnnna += 0.3333;
                    break;
                case ',':
                case ';':
                    dlinnnna += 0.5;
                    break;
                case '_':

                case '=':
                case '*':
                case ' ':
                case '/':
                case '+':
                    dlinnnna += 1;
                    break;
                case '<':
                case '>':
                    dlinnnna += 0.8333;
                    break;
                case '?':
                    dlinnnna += 0.777;
                    break;
                case '!':
                    dlinnnna += 0.388;
                    break;
                case '@':
                    dlinnnna += 1.418;
                    break;
                case '#':
                    dlinnnna += 0.777;
                    break;
                case '$':
                    dlinnnna += 0.777;
                    break;
                case '%':
                    dlinnnna += 0.828;
                    break;
                case '^':
                    dlinnnna += 0.655;
                    break;
                case '&':
                    dlinnnna += 0.932;
                    break;
                case '(':
                    dlinnnna += 0.465;
                    break;
                case ')':
                    dlinnnna += 0.465;
                    break;

                case '\'':
                    dlinnnna += 0.496;
                    break;
                case '№':
                    dlinnnna += 1.498;
                    break;
                case '[':
                    dlinnnna += 0.388;
                    break;
                case ']':
                    dlinnnna += 0.388;
                    break;
                case '{':
                    dlinnnna += 0.466;
                    break;
                case '}':
                    dlinnnna += 0.466;
                    break;
                case '|':
                    dlinnnna += 0.363;
                    break;
                case '\\':
                    dlinnnna += 0.388;
                    break;
                default:
                    dlinnnna += 1;
                    break;

            }
        }

        return dlinnnna;

    }

    public List<String> getListPozFromCSV(File fileCSV) throws IOException {
        List<String> result = new LinkedList<>();
        List<String> listPoz = Files.readAllLines(fileCSV.toPath());
        for (String str : listPoz) {
            if (str.equals("\t\t\t\t\t")
                    || str.equals("\t\t\t\t")
                    || str.equals("\r\n")
                    || str.equals("\n")
                    || str.equals("")
                    || str.equals("Инв.\tОбозначение\tКол.Т\tКол.Н\tГабариты\t")
                    || str.equals("Инв.\tОбозначение\tКол.Т\tКол.Н\tГабариты")) {
                continue;
            } else {
                result.add(str);
            }
        }
        return result;
    }

    public void addPozPodkroiToList(MultiValueHashMap<String, String> map, List<String> listPoziciiPlusPodkroi) {
        Set<String> set = map.kSet();
        for (String key : set) {
            List<String> listValue = map.get(key);
            listPoziciiPlusPodkroi.addAll(listValue);
        }

    }

    public List<String> getListFileName(String pathDXF) throws IOException {

        //   Path path = Path.of(pathDXF+"fileORD.Ord");

        //     Files.deleteIfExists(path);

        List<String> result = new LinkedList<>();
        File file = new File(pathDXF);
        File[] massiv = file.listFiles();
        for (int i = 0; i < massiv.length; i++) {
            String str = massiv[i].getName().replace(".dxf", "").replace(".Ord", "");
            if (!str.equals("fileORD")) {
                result.add(str);
            }
            massiv[i].delete();
        }
        return result;
    }

    public void savePozsPodkroi(MultiValueHashMap<String, String> map) throws IOException {

        FileWriter filePodkroi = new FileWriter("c:\\Users\\alexx.STALMOST\\Desktop\\ДеталиБК_всеPPP.csv", StandardCharsets.UTF_8);
        //  static File fileCSV = new File("c:\\Users\\alexx.STALMOST\\Desktop\\ДеталиБК_все.csv");


        Set<String> set = map.kSet();
        for (String key : set) {
            // System.out.println(key);
            List<String> listValue = map.get(key);
            for (String value : listValue) {
                filePodkroi.write(value + "\n");
            }
        }


        //  filePodkroi.write("\uFEFF");
        // filePodkroi.write("@M=0 @T=1.000000" + System.lineSeparator());
        filePodkroi.close();


    }
}
