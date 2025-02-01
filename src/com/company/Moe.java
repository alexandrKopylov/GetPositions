package com.company;

import com.company.logic.Point2D;
import com.company.logic.Rectangle;

import java.nio.file.Path;
import java.util.*;

public class Moe {
    public static void main(String[] args) {

String strokaWithGradeSteeel = "10233PDH2.3-3700-PB-001-1.1-KMD1-Lp-1019";
String strokaWithNamePozMinLenght = "PDH2.3-3700-1.1-KMD1-Lp-1019";

       // System.out.println(ss.contains(tt));

String threeChar = strokaWithNamePozMinLenght.substring(0, 3);
int index = strokaWithGradeSteeel.indexOf(threeChar);
String cod = strokaWithGradeSteeel.substring(0,index);


        System.out.println(cod);

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

    }

//    private static void nyMetod() {
//        Util util = new Util();
//        String str = "WWJJSKUWHUHHK;LLKMASZЛЛТДТОТГЦГГНЕЕЙГКЕКЖЛЛБЮЬТ";
//
//
//        System.out.println(10* util.PoluchitDlinnuStroki(str));
//    }
}
