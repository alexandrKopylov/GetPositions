package com.company;

import com.company.logic.Point2D;
import com.company.logic.Rectangle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.*;

public class Moe {
    public static void main(String[] args) {

        double x = 5;
        double y = 5;
        double c = x-y;

        System.out.println(c);

    }

//
//        System.out.println(Runtime.getRuntime().availableProcessors());
//        Path path = Paths.get (    "\\\\nts2dc\\Мосин\\_инв. № 1857 МиТОК\\03105А\\ЧПУ\\dxf\\206.dxf");
//        Path t1 = Paths.get (  "\\\\nts2dc\\Мосин\\_инв. № 1857 МиТОК\\03105А\\ЧПУ от 07.08.2025\\dxf\\206.dxf");
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

}

//    private static void nyMetod() {
//        Util util = new Util();
//        String str = "WWJJSKUWHUHHK;LLKMASZЛЛТДТОТГЦГГНЕЕЙГКЕКЖЛЛБЮЬТ";
//
//
//        System.out.println(10* util.PoluchitDlinnuStroki(str));
//    }

