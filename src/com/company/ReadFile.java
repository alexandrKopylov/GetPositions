package com.company;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadFile {


    public static void main(String[] args)
    {
        String filePath = "c:\\Users\\alexx.STALMOST\\Desktop\\_DXF\\1854mПР3-1L_02748.dxf";
        Charset encoding =   Charset.forName("Windows-1251");

        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get( filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String content = new String(encoded, encoding);
        System.out.println(content);
    }
}
