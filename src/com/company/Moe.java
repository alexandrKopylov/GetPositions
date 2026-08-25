package com.company;

import com.company.logic.Point2D;
import com.company.logic.Rectangle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Moe {
    public static ArrayList<int[]> figures = new ArrayList<>();  // Придумайте тип хранилища фигур

    private static boolean conteins(String str, String poz) {

        String escapedPos = Pattern.quote(poz);
        //String regex = "^(?!.*" + escapedPos + ".*" + escapedPos + ")[^0-9]*" + escapedPos + ".*$";
        String regex = "^(?!.*" + escapedPos + ".*" + escapedPos + ").*" + escapedPos + "([^0-9]*|$)";
        Pattern pat = Pattern.compile(regex);
        Matcher matcher = pat.matcher(str);
        return matcher.find();
    }
    public static void main(String[] args) {
        System.out.println(conteins("6МЕ-3-10041","10041"));

    }


    private  String sapog(String razmerA, String razmerB) {
String str = "0\r\n" +
        "SECTION\r\n" +
        "2\r\n" +
        "HEADER\r\n" +
        "9\r\n" +
        "$PDMODE\r\n" +
        "70\r\n" +
        "33\r\n" +
        "9\r\n" +
        "$PDSIZE\r\n" +
        "40\r\n" +
        "1.000\r\n" +
        "9\r\n" +
        "$EXTMIN\r\n" +
        "10\r\n" +
        "-18.000\r\n" +
        "20\r\n" +
        "-14.000\r\n" +
        "30\r\n" +
        "-1.000\r\n" +
        "9\r\n" +
        "$EXTMAX\r\n" +
        "10\r\n" +
        "198.000\r\n" +
        "20\r\n" +
        "154.000\r\n" +
        "30\r\n" +
        "1.000\r\n" +
        "9\r\n" +
        "$LIMMIN\r\n" +
        "10\r\n" +
        "-18.000\r\n" +
        "20\r\n" +
        "-14.000\r\n" +
        "9\r\n" +
        "$LIMMAX\r\n" +
        "10\r\n" +
        "198.000\r\n" +
        "20\r\n" +
        "154.000\r\n" +
        "0\r\n" +
        "ENDSEC\r\n" +
        "0\r\n" +
        "SECTION\r\n" +
        "2\r\n" +
        "TABLES\r\n" +
        "0\r\n" +
        "TABLE\r\n" +
        "2\r\n" +
        "LAYER\r\n" +
        "70\r\n" +
        "7\r\n" +
        "0\r\n" +
        "LAYER\r\n" +
        "2\r\n" +
        "0\r\n" +
        "70\r\n" +
        "0\r\n" +
        "62\r\n" +
        "7\r\n" +
        "6\r\n" +
        "CONTINUOUS\r\n" +
        "0\r\n" +
        "ENDTAB\r\n" +
        "0\r\n" +
        "TABLE\r\n" +
        "2\r\n" +
        "VPORT\r\n" +
        "70\r\n" +
        "4\r\n" +
        "0\r\n" +
        "VPORT\r\n" +
        "2\r\n" +
        "*ACTIVE\r\n" +
        "70\r\n" +
        "0\r\n" +
        "10\r\n" +
        "0.000000\r\n" +
        "20\r\n" +
        "0.000000\r\n" +
        "11\r\n" +
        "1.000000\r\n" +
        "21\r\n" +
        "1.000000\r\n" +
        "12\r\n" +
        "90.000000\r\n" +
        "22\r\n" +
        "70.000000\r\n" +
        "13\r\n" +
        "0.000000\r\n" +
        "23\r\n" +
        "0.000000\r\n" +
        "14\r\n" +
        "1.000000\r\n" +
        "24\r\n" +
        "1.000000\r\n" +
        "15\r\n" +
        "1.000000\r\n" +
        "25\r\n" +
        "1.000000\r\n" +
        "16\r\n" +
        "0.000000\r\n" +
        "26\r\n" +
        "0.000000\r\n" +
        "36\r\n" +
        "1.000000\r\n" +
        "17\r\n" +
        "0.000000\r\n" +
        "27\r\n" +
        "0.000000\r\n" +
        "37\r\n" +
        "0.000000\r\n" +
        "40\r\n" +
        "180.000000\r\n" +
        "41\r\n" +
        "1.500000\r\n" +
        "42\r\n" +
        "50.000000\r\n" +
        "43\r\n" +
        "0.000000\r\n" +
        "44\r\n" +
        "0.000000\r\n" +
        "50\r\n" +
        "0.000000\r\n" +
        "51\r\n" +
        "0.000000\r\n" +
        "71\r\n" +
        "0\r\n" +
        "72\r\n" +
        "100\r\n" +
        "73\r\n" +
        "1\r\n" +
        "74\r\n" +
        "1\r\n" +
        "75\r\n" +
        "0\r\n" +
        "76\r\n" +
        "0\r\n" +
        "77\r\n" +
        "0\r\n" +
        "78\r\n" +
        "0\r\n" +
        "0\r\n" +
        "ENDTAB\r\n" +
        "0\r\n" +
        "ENDSEC\r\n" +
        "0\r\n" +
        "SECTION\r\n" +
        "2\r\n" +
        "ENTITIES\r\n" +
        "0\r\n" +
        "POLYLINE\r\n" +
        "8\r\n" +
        "0\r\n" +
        "6\r\n" +
        "CONTINUOUS\r\n" +
        "62\r\n" +
        "7\r\n" +
        "66\r\n" +
        "1\r\n" +
        "10\r\n" +
        "0.000\r\n" +
        "20\r\n" +
        "0.000\r\n" +
        "30\r\n" +
        "0.000\r\n" +
        "70\r\n" +
        "1\r\n" +
        "0\r\n" +
        "VERTEX\r\n" +
        "8\r\n" +
        "0\r\n" +
        "10\r\n" +
        "0.000\r\n" +
        "20\r\n" +
        "0.000\r\n" +
        "30\r\n" +
        "0.000\r\n" +
        "42\r\n" +
        "0.00000000\r\n" +
        "0\r\n" +
        "VERTEX\r\n" +
        "8\r\n" +
        "0\r\n" +
        "10\r\n" +
        "0.000\r\n" +
        "20\r\n" +
                             razmerA + "\r\n" +
        "30\r\n" +
        "0.000\r\n" +
        "42\r\n" +
        "0.00000000\r\n" +
        "0\r\n" +
        "VERTEX\r\n" +
        "8\r\n" +
        "0\r\n" +
        "10\r\n" +
                            razmerB + "\r\n" +
        "20\r\n" +
                            razmerA + "\r\n" +
        "30\r\n" +
        "0.000\r\n" +
        "42\r\n" +
        "0.00000000\r\n" +
        "0\r\n" +
        "VERTEX\r\n" +
        "8\r\n" +
        "0\r\n" +
        "10\r\n" +
                             razmerB + "\r\n" +
        "20\r\n" +
        "0.000\r\n" +
        "30\r\n" +
        "0.000\r\n" +
        "42\r\n" +
        "0.00000000\r\n" +
        "0\r\n" +
        "SEQEND\r\n" +
        "0\r\n" +
        "ENDSEC\r\n" +
        "0\r\n" +
        "EOF\r\n";

        return null;


    }

}  // end main


// System.out.println("43627.01391618608");
/*
    String str1 = "22100-SHT-051-131";
    String str2 = "\t1923";
        System.out.println(str1);
        System.out.println(str2);
        System.out.println("    5");

    String s1 = "111";
    String s2 = "22222222222222";
    String s3 = "33333333";
    int chislo = 50;

        System.out.println(String.format("%"+chislo+"s",s1));
        System.out.println(String.format("%20s",s2));
        System.out.println(String.format("%20s",s3));
    */

// double roubles = NumberReader.getDouble();

//
//        System.out.println(Runtime.getRuntime().availableProcessors());
//        Path path = Paths.get (    "\\\\r\nts2dc\\Мосин\\_инв. № 1857 МиТОК\\03105А\\ЧПУ\\dxf\\206.dxf");
//        Path t1 = Paths.get (  "\\\\r\nts2dc\\Мосин\\_инв. № 1857 МиТОК\\03105А\\ЧПУ от 07.08.2025\\dxf\\206.dxf");
//
//
//        FileTime ft1 =null;
//        FileTime   ft2 = null;
//        try {
//            path= path.getParent();
//            t1= t1.getParent();
//            ft1 =(FileTime) Files.getAttribute(path, "creationTime");
//            ft2 =(FileTime) Files.getAttribute(t1, "creationTime");
//
//            System.out.println(ft1.toMillis());
//            System.out.println(ft2.toMillis());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }


//String name = "Lp103";
//int index = 0;
//        for (int i = 0; i < name.length(); i++) {
//           char ch = name.charAt(i);
//            if (Character.isDigit(ch)){
//                index=i;
//                break;
//            }
//        }
//        String start = name.substring(0,index);
//        String end = name.substring(index);
//        String res = start + "-" + end;
//        System.out.println(res);


//        HashMap<String, String> map = new HashMap<>();
//        map.put("111","2222");
//        System.out.println(map.get("111"));
//        System.out.println( map.get("333"));
//
//
//
//        String s1 = "111";
//        String s2 = "22222222222222";
//        String s3 = "33333333";
//        int chislo = 50;
//
//        System.out.println(String.format("%"+chislo+"s",s1));
//        System.out.println(String.format("%20s",s2));
//        System.out.println(String.format("%20s",s3));
//
//
//        int shiftCoordinateX = 80 / 100;
//        System.out.println(shiftCoordinateX);


//
//String str = "C:\\Users\\alexx.STALMOST\\Desktop\\DXF формат";
//        Path path = Path.of(str);
//        System.out.println(path.getParent());
//String poz = "МД-1";
//       String file = path.toString()+"\\"+poz + ".dxf";
//
//        System.out.println(file);

// nyMetod();
// System.out.println(20* util.PoluchitDlinnuStroki(str""));


//        String ss = "FA";
//        int decimalValue = Integer.parseInt(ss, 16);
//        System.out.println(ss +" = "+decimalValue);
//       // decimalValue++;
//       String hex = Integer.toHexString(++decimalValue).toUpperCase();
//        System.out.println(hex +" = "+decimalValue);

//}

//    private static void nyMetod() {
//        Util util = new Util();
//        String str = "WWJJSKUWHUHHK;LLKMASZЛЛТДТОТГЦГГНЕЕЙГКЕКЖЛЛБЮЬТ";
//
//
//        System.out.println(10* util.PoluchitDlinnuStroki(str));
//    }

