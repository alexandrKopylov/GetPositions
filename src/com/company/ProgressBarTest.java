package com.company;

import javax.swing.*;

public class ProgressBarTest extends JFrame {
    // Общая модель
    private BoundedRangeModel model;
    public ProgressBarTest() {
        super("Пример использования JProgressBar");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Создание стандартной модели
        model = new DefaultBoundedRangeModel(5, 0, 0, 100);
        // Вертикальный индикатор
//        JProgressBar pbVertical = new JProgressBar(JProgressBar.VERTICAL);
//        pbVertical.setModel(model);
//        pbVertical.setStringPainted(true);
//        pbVertical.setString("Процесс ...");

        // Горизонтальный индикатор
        JProgressBar pbHorizontal = new JProgressBar(model);
        pbHorizontal.setStringPainted(true);

//        // Настройка параметрой UI-представителя
//        UIManager.put("ProgressBar.cellSpacing", new Integer(2));
//        UIManager.put("ProgressBar.cellLength", new Integer(6));
//        // Создание индикатора
//        JProgressBar pbUIManager = new JProgressBar(model);
//
//        // Неопределенный индикатор
//        JProgressBar pbUndefined = new JProgressBar(0, 100);
//        pbUndefined.setIndeterminate(true);
//        pbUndefined.setStringPainted(true);

        // Размещение индикаторов в окне
        JPanel contents = new JPanel();
       // contents.add(pbVertical  );
        contents.add(pbHorizontal);
      //  contents.add(pbUIManager );
      //  contents.add(pbUndefined );

        // Вывод окна на экран
        setContentPane(contents);
        setSize(360, 220);
        setVisible(true);
        // Старт "процесса"
        new ThreadProcess().start();
    }



    // Поток эмуляции некоторого процесса
    class ThreadProcess extends Thread {
        public void run() {
            // Проверка завершения процесса
            while ( model.getValue() < model.getMaximum() ) {
                try {
                    // Увеличение текущего значение
                    model.setValue(model.getValue() + 1);
                    // Случайная временная задержка
                    sleep((int)(Math.random() * 1000));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        new ProgressBarTest();
    }
}