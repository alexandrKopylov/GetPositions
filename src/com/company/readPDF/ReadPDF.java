package com.company.readPDF;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import javax.swing.*;

//  нужна библиотека pdfbox 2.0.32
public class ReadPDF {


    private List<String> fileNotFound = new ArrayList<>();
    private List<String> material = new ArrayList<>();

    public ReadPDF() {
        material.add("С345");
        material.add("C345");
        material.add("C355");
        material.add("C245");
        material.add("C255");
        material.add("345-8-09Г2С");           // eng
        material.add("345-8-09G2S");
        material.add("С255");
        material.add("С245");
        material.add("С355");
        material.add("C235");           // rus
        material.add("355-9");
        material.add("C390");           // eng
        material.add("С390");           // rus

    }


    /**
     * в папке  с pdf файлами
     *
     * @param path путь  до папки с pdf файлами
     * @return список всех pdf файлов
     */
    public List<String> searchPDF(String path) {
        List<String> listPathPDF;
        try {
            listPathPDF = Files.walk(Path.of(path))
                    .filter(Files::isRegularFile)
                    .filter(x -> x.toFile().getName().endsWith(".pdf"))
                    .map(x -> x.getFileName())
                    .map(x -> x.toString())
                    //   .map(x -> x.replace(".dxf", ""))
                    .collect(Collectors.toList());
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        return listPathPDF;
    }


    public String[] readPDFtoString(String path) throws IOException {

        String pdfFileInText = "";

        try (PDDocument document = PDDocument.load(new File(path))) {
            document.getClass();

            if (!document.isEncrypted()) {


                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                // stripper.setSortByPosition(true);
                PDFTextStripper tStripper = new PDFTextStripper();
                pdfFileInText = tStripper.getText(document);
                System.out.println("Text:" + pdfFileInText);
            }
        }
        return pdfFileInText.split("\\r\\n");
    }

    public void serchInTextAndAddToMap2(String[] lines, Map<String, String> map, String stringPath, List<String> listNamePozicii, JTextArea textAreaPDF) {

        try {

            String strokaWithGradeSteeel = null;

            //  smotrim stroki gde est'  stroka GradeSteeel  and   save in string
            for (int i = 0; i < lines.length; i++) {
                if (isLineFound(lines[i] )) {

                    if (isStrokaContainsNamePos(lines[i], listNamePozicii)) {
                        strokaWithGradeSteeel = lines[i];
                        break;
                    }

                }
            }
            if(strokaWithGradeSteeel.equals(null)){
                textAreaPDF.append("\nstrokaWithGradeSteeel  нет  материала ");
            }else {
                textAreaPDF.append("\nstrokaWithGradeSteeel   ===   " + strokaWithGradeSteeel);
            }

            String namePoz = null;
            for (String str : listNamePozicii) {
                if ( isStrokaWithGradeSteeelContainsExaclyNamePosAndLenght(strokaWithGradeSteeel , str) ) {
                    namePoz = str;
                    break;
                }
            }

            String pozModify = null;   // poz nahoditsya v spiske  csv
            boolean delDefis = false;
            String strrr = null;
            if (namePoz == null) {
                for (String str : listNamePozicii) {
                    strrr = deleteDefis(str);
                    if (strokaWithGradeSteeel.contains(strrr)) {
                        namePoz = strrr;
                        delDefis = true;
                        pozModify = str;
                        break;
                    }
                }
            }


            if (namePoz == null) {
                Metka:
                for (String str : lines) {
                    for (String strPoz : listNamePozicii) {
                        if (str.contains(strPoz)) {
                            namePoz = strPoz;
                            break Metka;
                        }
                    }
                }
            }

            textAreaPDF.append("\nnamePoz   ===   " + namePoz);

            String strokaWithNamePozMinLenght = namePoz;                        // "J".repeat(200);
//            for (String str : lines) {
//                if (str.contains(namePoz)) {
//                    if (str.length() < strokaWithNamePozMinLenght.length()) {
//                        strokaWithNamePozMinLenght = str;
//                    }
//                }
//            }
            textAreaPDF.append("\nstrokaWithNamePozMinLenght   ===   " + strokaWithNamePozMinLenght);


            String cod = null;
            if (strokaWithGradeSteeel.contains(strokaWithNamePozMinLenght)) {
                cod = strokaWithGradeSteeel.replace(strokaWithNamePozMinLenght, "").split(" ")[0];
                //  cod = "1096";
                boolean bb = isCodConteinsLetters(cod);
                if (bb) {
                    cod = deleteLettersBeginStartStrings(cod);
                }


            } else {
                String threeChar = strokaWithNamePozMinLenght.substring(0, 3);
                int index = strokaWithGradeSteeel.indexOf(threeChar);

                if (index == -1) {
                    cod = strokaWithGradeSteeel.replace(namePoz, "").split(" ")[0];
                } else {
                    cod = strokaWithGradeSteeel.substring(0, index);
                }
            }


            textAreaPDF.append("\nCOD  ===  " + cod);

            if (cod != null || cod != "") {
                if (delDefis) {
                    map.put(pozModify, cod);
                } else {
                    map.put(namePoz, cod);
                }
            }
            textAreaPDF.append("\n" + "=".repeat(300));

        } catch (Exception e) {
            e.printStackTrace();
            //  textArea.append(System.lineSeparator());
            textAreaPDF.append("\n" + stringPath + "  не парсится \n");
        }

    }

    private boolean isStrokaWithGradeSteeelContainsExaclyNamePosAndLenght(String strokaWithGradeSteeel, String str) {
        String[] masStr = strokaWithGradeSteeel.split(" ");
        for (int i = 0; i <masStr.length ; i++) {
            if(masStr[i].endsWith(str)){
                return true;
            }
        }


        return false;
    }

    private boolean isStrokaContainsNamePos(String line, List<String> listNamePozicii) {
        int count = 0;
        for (String strPoz : listNamePozicii) {
            if (line.contains(strPoz)) {
                return true;
            }
            count++;
        }

         count = 0;
        for (String strPoz : listNamePozicii) {
            if (line.contains(strPoz.replace("-",""))) {
                return true;
            }
            count++;
        }

        return false;
    }

    private String deleteLettersBeginStartStrings(String cod) {
        int count = 0;
        for (int i = 0; i < cod.length(); i++) {
            //  char ch =   cod.charAt(i);
            if (Character.isDigit(cod.charAt(i))) {
                count++;
            } else {
                break;
            }
        }
        String result = cod.substring(0, count);
        return result;
    }

    private boolean isCodConteinsLetters(String cod) {
        for (int i = 0; i < cod.length(); i++) {
            if (Character.isAlphabetic(cod.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private String insertDefis(String namePoz, String pozModify) {
        // defis vstavlyaem mejdu bukvami i ciframi
//        int index = 0;
//        for (int i = 0; i < namePoz.length(); i++) {
//            char ch = namePoz.charAt(i);
//            if (Character.isDigit(ch)){
//                index=i;
//                break;
//            }
//        }

        int index = pozModify.charAt('-');

        String start = namePoz.substring(0, index);
        String end = namePoz.substring(index);
        String res = start + "-" + end;


        return null;
    }

    private String deleteDefis(String str) {
        return str.replace("-", "");
    }

    public void serchInTextAndAddToMap(String[] lines, Map<String, String> map, String stringPath) {
        try {
            //  split by whitespace

            String marka = "";
            String detal = "";
            Set<String> list = new HashSet<>();

            List<String> listSearch = new LinkedList<>();
            listSearch.add("Mounting element");
            listSearch.add("Handrail");
            listSearch.add("[Монтажная деталь]");
            listSearch.add("Mounting detail");
            listSearch.add("[Монтажная накладка]");
            listSearch.add("Гл. констр./Сhief designer");

            listSearch.add("всех");
            listSearch.add("Cover plate");
            // listSearch.add("Loose part");
            listSearch.add("5694 СОЕДИНИТЕЛЬНАЯ ТРУБОПРОВОДНАЯ ЭСТАКАДА");
            listSearch.add("Shim plate");


            Metka:
            for (int i = 0; i < lines.length; i++) {
                for (String str : listSearch) {
                    if (lines[i].equals(str)) {
                        marka = lines[++i];
                        marka = marka.split(" ")[0];
                        marka = marka.replace("ДатаФамилияДолжность", "");
                        break Metka;
                    }
                }
            }


            if (marka.equals("")) {
                for (int i = 0; i < lines.length; i++) {
                    if (lines[i].contains("Монтажный элемент")) {
                        marka = lines[i];
                        marka = marka.replace("Монтажный элемент", "").trim();
                    }
                }
            }

            if (marka.equals("")) {
                for (int i = 0; i < lines.length; i++) {
                    if (lines[i].contains("Пластина")) {
                        marka = lines[i];
                        marka = marka.replace("Пластина", "").trim();
                    }
                }
            }

            System.out.println("++++++++++++++++++++++++++++++++++++++++++     marka = " + marka);


            for (int i = 0; i < lines.length; i++) {
                if (lines[i].contains(marka)) {
                    list.add(lines[--i]);
                    ++i;
                    list.add(lines[i]);
                }
            }


            for (String line : list) {
                if (isLineFound(line)) {
                    line = line.replace(marka, "");
                    String[] str = line.split(" ");
                    detal = str[0];
                    detal = detal.replace(marka, "");
                    break;
                }
            }

            System.out.println("+++++++++ detal ++++++++ == " + detal);

            System.out.println(marka + "===" + detal);
            map.put(marka, detal.replace(".", "_"));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(stringPath + " не парсится ");
        }


    }


    public void fileRename(String path, Map<String, String> map) throws IOException {


        for (Map.Entry<String, String> pair : map.entrySet()) {

            File file = new File(path + pair.getValue() + ".dxf");

            if (file.exists()) {
                File fileTMP = new File(path + pair.getValue() + "TMP.dxf");
                Files.copy(file.toPath(), fileTMP.toPath());


                File newfile = new File(path + pair.getKey() + ".dxf");

                // System.out.println(path+pair.getValue()+".dxf");
                // System.out.println(path+pair.getKey()+".dxf");
                System.out.print(pair.getValue() + " -> " + pair.getKey());
                System.out.print("   ");
                System.out.println(fileTMP.renameTo(newfile));

            } else {
                // System.out.println(pair.getValue() + " - " + pair.getKey()  + " файла нет");
                fileNotFound.add(pair.getKey() + "  *** " + pair.getValue());
            }
        }
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("число файлов = " + map.entrySet().size());
        System.out.println("нашлось      = " + (map.entrySet().size() - fileNotFound.size()));
        System.out.println("не нашлось   = " + fileNotFound.size());
        fileNotFound.sort(Comparator.naturalOrder());
        for (String str : fileNotFound) {
            System.out.println(str);
        }


    }


    public void serchInTextAndAddToMap2(String[] lines, Map<String, String> map, String stringPath, String prefics) {
        try {
            //  split by whitespace

            String marka = "";
            String detal = "";
            List<String> list = new LinkedList<>();
            List<String> listPrefics = new LinkedList<>();


            // int preficsLenght = prefics.length();         нужна для посимвольного сравнения строк


            for (int i = 0; i < lines.length; i++) {
                if (lines[i].contains(prefics)) {
                    listPrefics.add(lines[i].substring(lines[i].indexOf(prefics)));
                }
            }
            System.out.println("список находок");
            System.out.println(listPrefics);

            String minStrokaInListPrefics = listPrefics.get(0);

            for (String str : listPrefics) {
                if (str.length() < minStrokaInListPrefics.length()) {
                    minStrokaInListPrefics = str;
                }
            }
            System.out.println("мин строка = " + minStrokaInListPrefics);

            int countTrue = 0;
            for (String str : listPrefics) {
                if (str.contains(minStrokaInListPrefics)) {
                    System.out.println(str + " содержит " + minStrokaInListPrefics);
                    countTrue++;
                } else {
                    System.out.println(str + " НЕ содержит " + minStrokaInListPrefics);
                }
            }

            if (listPrefics.size() == countTrue) {
                System.out.println("Т.к. мин строка содержится во всех найденых строках");
                System.out.println("значит марка  = " + minStrokaInListPrefics);
                marka = minStrokaInListPrefics;
            } else {
                System.out.println("Т.к. мин строка НЕ содержится во всех найденых строках");
                System.out.println("марка не найдена");
            }


            System.out.println("+++++++++ marka ++++++++ == " + marka);

            for (int i = 0; i < lines.length; i++) {
                if (lines[i].contains(marka)) {
                    list.add(lines[--i]);
                    ++i;
                }
            }


            for (String line : list) {
                if (isLineFound(line)) {
                    String[] str = line.split(" ");
                    detal = str[0];
                    break;
                }
            }

            System.out.println("+++++++++ detal ++++++++ == " + detal);

            System.out.println(marka + "===" + detal);
            map.put(marka, detal);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(stringPath + " не парсится ");
        }


    }

    private boolean isLineFound(String line) {
        boolean bool = false;

        boolean hasMaterial = false;
        for (String mat : material) {
            if (line.contains(mat)) {
                hasMaterial = true;
                break;
            }
        }

        if (hasMaterial) {
            return true;
        }

//        boolean hasChar = (line.contains("x") || line.contains("*") || line.contains("х"));
//        if (hasChar && hasMaterial) {
//            return true;
//        }

        if (line.contains("Ст3пс6Sheet diamond/Лист ромб 2.5")) {
            return true;
        }




        return bool;
    }


}
