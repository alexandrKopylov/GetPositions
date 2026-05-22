package com.company.logic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;

/**
 * Компаратор для сортировки объектов Path по времени создания.
 * Папки с более ранним временем создания идут первыми (по возрастанию).
 */
public class ComparatorPath implements Comparator<Path> {

    @Override
    public int compare(Path path1, Path path2) {
        try {
            // Получаем базовые атрибуты файлов/папок
            BasicFileAttributes attrs1 = Files.readAttributes(path1, BasicFileAttributes.class);
            BasicFileAttributes attrs2 = Files.readAttributes(path2, BasicFileAttributes.class);

            // Извлекаем время создания
            FileTime creationTime1 = attrs1.creationTime();
            FileTime creationTime2 = attrs2.creationTime();

            // Сравниваем времена создания
            return creationTime2.compareTo(creationTime1);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при получении времени создания файла/папки: " + e.getMessage(), e);
        }
    }
}
