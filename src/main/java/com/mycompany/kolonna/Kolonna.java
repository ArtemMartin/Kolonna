/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.kolonna;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Kolonna {

    static double x1;
    static double y1;
    static double x2;
    static double y2;
    static double interval;
    static double ypregdenie;
    static double xTVZaranee;
    static double yTVZaranee;

    public static void main(String[] args) {

        KolonnaFrame frame = new KolonnaFrame();
        frame.setVisible(true);

        Thread thread = new Thread(new ListeneerHandler(frame));
        thread.start();

        frame.getBtnRaschet().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                x1 = Double.parseDouble(frame.getTfX1().getText());
                y1 = Double.parseDouble(frame.getTfY1().getText());
                x2 = Double.parseDouble(frame.getTfX2().getText());
                y2 = Double.parseDouble(frame.getTfY2().getText());
                interval = Double.parseDouble(frame.getTfInterval().getText());
                ypregdenie = Double.parseDouble(frame.getTfYpregdenie().getText());

                try {
                    xTVZaranee = Double.parseDouble(frame.getTfTVZaraneeeX().getText());
                    yTVZaranee = Double.parseDouble(frame.getTfTVZaraneeY().getText());
                } catch (NumberFormatException eexeption) {
                    System.out.println("Class Kolonna, listener btnRaschet:" + eexeption.getMessage());
                }
                run(frame);

                if (xTVZaranee != 0.0) {
                    frame.getLabKomanda().setText("Внимание!!!");
                    frame.getLabKomanda().setForeground(Color.black);
                    int ostatok = (int) ostatokDoTV(x1, y1, x2, y2, xTVZaranee, yTVZaranee);                  
                    countdownTimerExample(ostatok, frame);
                }

            }
        });
    }

    public static void countdownTimerExample(int seconds, KolonnaFrame frame) {
        int poletnoe = Integer.parseInt(frame.getTfPoletnoe().getText());
        int vrZadergki = Integer.parseInt(frame.getTfZadergkaPodKom().getText());
        int vremiaKomandu = poletnoe + vrZadergki;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int countdown = seconds;

            @Override
            public void run() {
                System.out.println(countdown);
                if (countdown <= 0) {
                    frame.getLabKomanda().setText("Ну что попал?");
                    frame.getLabKomanda().setForeground(Color.BLACK);
                    timer.cancel(); // Останавливаем таймер
                    return;
                }
                countdown--;
                frame.getTfOstalosSekynd().setText(String.valueOf(countdown));
                if (vremiaKomandu > countdown) {
                    frame.getLabKomanda().setText("ОГОНЬ Нах!!!");
                    frame.getLabKomanda().setForeground(Color.RED);
                }
            }
        }, 0, 1000); // Задаем выполнение задачи каждые 1000 миллисекунд (1 секунда)

    }

    /*
    пример
    5320160 7358281
    5319714 7361112
    1 АТ
    1м23с
    5319447 7363721
     */
    public static double ostatokDoTV(double x1, double y1, double x2, double y2, double xTV, double yTV) {
        double[] da = OGZ(x1, y1, x2, y2);
        double dalnost = da[0];
        double ms = dalnost / interval;
        double[] daTV = OGZ(x2, y2, xTV, yTV);
        double dalnostTV = daTV[0];

        return Math.round(dalnostTV / ms);
    }

    public static void run(KolonnaFrame frame) {
        double[] da = OGZ(x1, y1, x2, y2);
        double dalnost = da[0];
        double ygol = da[1];
        double dalYpr = dalnost * (ypregdenie / interval);
        double[] xyYpr = PGZ(x2, y2, dalYpr, ygol);
        frame.getTfOutput().setText("53" + Math.round(xyYpr[0]) + " " + "73" + Math.round(xyYpr[1]));
    }

    public static double[] PGZ(double x2, double y2, double dalYpr, double ygol) {
        double xYpr = Math.cos(ygol / 100 * 6 * Math.PI / 180) * dalYpr + x2;
        double yYpr = Math.sin(ygol / 100 * 6 * Math.PI / 180) * dalYpr + y2;
        return new double[]{xYpr, yYpr};
    }

    public static double[] OGZ(double x1, double y1, double x2, double y2) {
        double dxc = x2 - x1;
        double dyc = y2 - y1;
        double Dc = Math.sqrt(Math.pow(dxc, 2) + Math.pow(dyc, 2));
        double Ac = Math.abs(Math.atan(dyc / (dxc)) / Math.PI * 30) * 100;
        double Ygolc = 0;
        if (dxc > 0 && dyc > 0) {
            Ygolc = Math.round(Ac);
        } else if (dxc < 0 && dyc > 0) {
            Ygolc = Math.round(3000 - Ac);
        } else if (dxc < 0 && dyc < 0) {
            Ygolc = Math.round(3000 + Ac);
        } else if (dxc > 0 && dyc < 0) {
            Ygolc = Math.round(6000 - Ac);
        }
        return new double[]{Dc, Ygolc};
    }
}

class ListeneerHandler implements Runnable {

    public final KolonnaFrame frame;

    public ListeneerHandler(KolonnaFrame frame) {
        this.frame = frame;
    }

    @Override
    public void run() {

        frame.getTfX1().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    frame.getTfY1().setText("");
                    frame.getTfY1().requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        frame.getTfY1().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    frame.getTfX2().setText("");
                    frame.getTfX2().requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        frame.getTfX2().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    frame.getTfY2().setText("");
                    frame.getTfY2().requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        frame.getTfY2().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    frame.getTfInterval().setText("");
                    frame.getTfInterval().requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        frame.getTfInterval().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    frame.getTfYpregdenie().setText("");
                    frame.getTfYpregdenie().requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

    }

}
