package com.company.modify;

import com.company.logic.Point2D;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ModifyDXF {

    public String readFileInString(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
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

        // проверяем есть ли слой с именем "2" , если нет то создаем его
        if(masStrok[0].contains("0\r\nTABLE\r\n2\r\nLAYER\r\n")) {
            boolean hasLayerName2 = masStrok[0].contains("0\r\nLAYER\r\n2\r\n2\r\n");
            if (!hasLayerName2) {
                int index = masStrok[0].indexOf("TABLES\r\n0\r\nTABLE");
                int index2 = masStrok[0].indexOf("0\r\nENDTAB", index);
                String str11 = masStrok[0].substring(0, index2);
                String str22 = masStrok[0].substring(index2);
                String newLayer = "0\n" +
                        "LAYER\n" +
                        "2\n" +
                        "2\n" +   //  имя слоя
                        "70\n" +
                        "0\n" +
                        "62\n" +
                        "6\n" +    //  цвет слоя
                        "6\n" +
                        "CONTINUOUS\n";
                String result = "";
                result = result.concat(str11).concat(newLayer).concat(str22);
                masStrok[0] = result;
            }
        }
        return masStrok;
    }

    public List<String> breakMidlePartOnEntityes(String entities) {

        List<String> listEnts = new LinkedList<>();
        // search Polyline
        while (entities.contains("0\r\nPOLYLINE")) {
            int beginPoly = entities.indexOf("0\r\nPOLYLINE");
            int endPoly = entities.indexOf("SEQEND\r\n", beginPoly) + 8;     //  "SEQEND\r\n" = 8
            String newEnt = entities.substring(beginPoly, endPoly);
            listEnts.add(newEnt);
            entities = entities.replace(newEnt, "");
        }

        if (entities.length() > 0) {
            while (entities.indexOf("\r\n0\r\nTEXT\r\n", 15) > 0 ||  entities.indexOf("\r\n0\r\nCIRCLE\r\n", 15) > 0 ) {
               Integer endEnt = null;
                int endEnt1 = entities.indexOf("\r\n0\r\nTEXT\r\n", 6) + 2;
                int endEnt2 = entities.indexOf("\r\n0\r\nCIRCLE\r\n", 6) + 2;
                if(endEnt1 == 1 ){
                    endEnt = endEnt2;
                }else if(endEnt2 == 1){
                    endEnt = endEnt1;
                }else if(endEnt1 > endEnt2  ){
                    endEnt = endEnt2;
                }else{
                    endEnt = endEnt1;
                }
                String newEnt = entities.substring(0, endEnt);
                listEnts.add(newEnt);
                entities = entities.replace(newEnt, "");
            }
            listEnts.add(entities);
        }
        return listEnts;
    }


    public List<String> modifyPolylineInCircle(boolean hasSpaces, List<String> entityies, List<Point2D> listPoint) {

        List<String> listCircle = new LinkedList<>();
        Iterator<String> iter2 = entityies.iterator();
        while (iter2.hasNext()) {

            String poly = iter2.next();
            if (poly.contains("0\r\nPOLYLINE\r\n8\r\n18\r\n")
                    || poly.contains("0\r\nPOLYLINE\r\n8\r\n22\r\n")
                    || poly.contains("0\r\nPOLYLINE\r\n8\r\n14\r\n")
                    || poly.contains("0\r\nPOLYLINE\r\n8\r\n27\r\n")
                    || poly.contains("0\r\nPOLYLINE\r\n8\r\n30\r\n")) {      //  удаляем все линии  на слое 18  , 22, 14, 27, 30
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

                String res = null;
                if (hasSpaces) {
                    res = "0\r\nCIRCLE\r\n" +
                            "5\r\n" +
                            "CC\r\n" +
                            "8\r\n" +
                            "0\r\n" +
                            "10\r\n" +
                            centrX + "\r\n" +
                            "20\r\n" +
                            centrY + "\r\n" +
                            "30\r\n" +
                            "0.0\r\n" +
                            "40\r\n" +
                            radius + "\r\n";
                } else {
                    res = "0\r\nCIRCLE\r\n" +
                            "5\r\n" +
                            "CC\r\n" +
                            "8\r\n" +
                            "0\r\n" +
                            "10\r\n" +
                            centrX + "\r\n" +
                            "20\r\n" +
                            centrY + "\r\n" +
                            "30\r\n" +
                            "0.0\r\n" +
                            "40\r\n" +
                            radius + "\r\n";
                }

                listCircle.add(res);

                listPoint.add(new Point2D(centrX - radius, centrY - radius));
                listPoint.add(new Point2D(centrX + radius, centrY + radius));
                listPoint.add(new Point2D(centrX - radius, centrY + radius));
                listPoint.add(new Point2D(centrX + radius, centrY - radius));

                iter2.remove();
            }
        }

        return listCircle;
    }


}
