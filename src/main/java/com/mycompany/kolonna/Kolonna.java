/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.kolonna;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public static void main(String[] args) {

        KolonnaFrame frame = new KolonnaFrame();
        frame.setVisible(true);

        frame.getBtnRaschet().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                x1 = Double.parseDouble(frame.getTfX1().getText());
                y1 = Double.parseDouble(frame.getTfY1().getText());
                x2 = Double.parseDouble(frame.getTfX2().getText());
                y2 = Double.parseDouble(frame.getTfY2().getText());
                interval = Double.parseDouble(frame.getTfInterval().getText());
                ypregdenie = Double.parseDouble(frame.getTfYpregdenie().getText());

                run(frame);
            }
        });
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