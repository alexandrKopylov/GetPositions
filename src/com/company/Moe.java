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

    /*
    Разработайте цифровой список покупок с консольным интерфейсом. Программа должна уметь выполнять четыре действия:
добавлять товар в список;
показывать список;
очищать список;
завершать работу.
Для реализации консольного интерфейса используйте бесконечный цикл.
 На каждой итерации пользователю предлагается список всех доступных команд, после чего он выбирает одну из них.
 С помощью Scanner программа считывает номер команды из консоли в переменную actionNumber. Нужное действие выполняется в отдельной ветке if — else.
Если пользователь ввёл значение actionNumber, которого нет в списке, на экран должно быть выведено сообщение Неизвестная команда!.

Список покупок храните в массиве shoppingList с заранее заданным размером.
 Для простоты установите ограничение: в список можно добавлять не более восьми товаров.
  Если нужно купить больше, пользователь должен будет выбрать восемь наиболее важных из них.

При вызове команды «Добавить товар в список» нужно проверить, есть ли ещё свободное место, или все слоты закончились.
 Для этого удобно вести подсчёт количества добавленных продуктов в отдельной переменной. Назовите её productCount.
Программа должна приглашать пользователя ввести название продукта с помощью сообщения Введите название товара:.
 Название следует писать без пробелов. Несколько слов нужно соединять символом подчёркивания.
  Например, так: «кокосовоемолоко», «молочныйшоколад», «зерновой_хлеб».
Если элемент успешно добавлен в список, программа должна показать пользователю сообщение следующего вида: Товар печенье добавлен в список под номером 5.
Обратите внимание: элементы массива нумеруются от 0 до 7, но для пользователя нумерация должна быть привычной — от 1 до 8.
Если список уже содержит восемь элементов и товар не может быть добавлен, должно появиться сообщение Извините, список полон!.

При вызове команды «Отобразить список» программа будет выводить текущий список покупок в консоль.
 При этом нужно показать столько товаров, сколько пользователь добавил на текущий момент: два, пять или все восемь.
 У каждого товара должен быть порядковый номер от 1 до 8.

Вызов команды «Очистить список» должен очищать список: удалять из него все товары и возвращать переменную productCount в исходное состояние.
После этого пользователю нужно показать сообщение Список очищен!.

Перед отправкой кода на автоматическую проверку протестируйте его локально.
Запустите метод main и выполните следующий алгоритм:

Добавьте в список товар «лук».
Добавьте в список товар «морковь».
Выведите список.
Очистите список.
Добавьте в список товар «томаты».
Выведите список.
При выполнении последней команды на экране должна появиться единственная строка: 1. томаты. Если это не так, ещё раз внимательно изучите текст программы и постарайтесь найти ошибку. Учтите, что внутри тренажёра в программу будет передан набор операций, отличающийся от того, что мы описали выше. Это поможет лучше протестировать ваш код.
Также рекомендуем заглянуть в файл с подробным разбором того, как должен работать список покупок.

    */
    public static void main(String[] args) {

        System.out.println("Вас приветствует список покупок!");

        int currentListSize = 8;
        String[] shoppingList = new String[currentListSize];
        int productCount = 0;

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите одну из команд:");
            System.out.println("1. Добавить товар в список");
            System.out.println("2. Отобразить список");
            System.out.println("3. Очистить список");
            System.out.println("4. Завершить программу");

            int actionNumber = scanner.nextInt();

            if (actionNumber == 1) {
                if (productCount == currentListSize) {
                    int newListSize = currentListSize * 2;
                    String[] newShoppingList = new String[newListSize];
                    for (int i = 0; i < currentListSize; i++) {
                        newShoppingList[i] = shoppingList[i];
                    }
                    shoppingList = newShoppingList;
                    currentListSize = newListSize;
                }
                System.out.println("Введите название товара:");
                String productName = scanner.next();
                shoppingList[productCount] = productName;
                productCount++;
                System.out.println("Товар " + productName + " добавлен в список под номером " + productCount);
            } else if (actionNumber == 2) {
                for (int i = 0; i < productCount; i++) {
                    System.out.println(i + 1 + ". " + shoppingList[i]);
                }
            } else if (actionNumber == 3) {
                for (int i = 0; i < productCount; i++) {
                    shoppingList[i] = null;
                }
                productCount = 0;
                System.out.println("Список очищен!");
            } else if (actionNumber == 4) {
                break;
            } else {
                System.out.println("Неизвестная команда!");
            }
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

