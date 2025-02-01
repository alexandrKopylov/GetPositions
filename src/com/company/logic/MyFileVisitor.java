package com.company.logic;

import javax.naming.directory.BasicAttribute;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class MyFileVisitor extends SimpleFileVisitor<Path>  {

    public FileVisitResult visitFile(Path file, BasicAttribute attr) throws IOException, IOException {

        List<String> lines = Files.readAllLines(file);
        for (String s: lines) {
            if (s.contains("This is the file we need")) {
                System.out.println("Нужный файл обнаружен!");
                System.out.println(file.toAbsolutePath());
                break;

            }
        }

        return FileVisitResult.CONTINUE;
    }
}
