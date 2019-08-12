/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebookswing.painting;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

/**
 *
 * @author fajar
 */
public class AppKotakCamvas extends Canvas implements MouseListener, KeyListener {

    private static int panjang = 0, lebar = 0;
    public static List<KotakKecil> daftarKotak = new ArrayList<>();
    private boolean programjalan = false;

    public void inisiasiKotakKecil() {
        addMouseListener(this);
        addKeyListener(this);
        int id = 1;
        for (int i = 0; i <= panjang; i++) {
            for (int j = 0; j <= lebar; j++) {
                KotakKecil k = new KotakKecil(i * 20, j * 20, id);
                k.setColor(Color.red);
                // System.out.println("i : " + i + ", j: " + j);
                daftarKotak.add(k);
                id++;
            }
        }

    }

    public static void mains(String[] args) {
        JFrame frame = new JFrame("Rectangles");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String pjg = JOptionPane.showInputDialog("Please input length: ");
        String lbr = JOptionPane.showInputDialog("Please input width: ");

        panjang = Integer.parseInt(pjg);
        lebar = Integer.parseInt(lbr);

        AppKotakCamvas appKotak = new AppKotakCamvas();
        JPanel panelDasar = new JPanel();
        
        JScrollPane alatScrolling = new JScrollPane(appKotak);//panelDasar, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        appKotak.setPreferredSize(new Dimension(panjang * 20, lebar * 20));
        appKotak.setBackground(Color.green);

        appKotak.inisiasiKotakKecil();
       // alatScrolling.getViewport().setPreferredSize(new Dimension(panjang * 20, lebar * 20));
       // alatScrolling.getViewport().add(appKotak);

        panelDasar.add(alatScrolling);
        //   panelDasar.add(appKotak);
        frame.add(panelDasar);
        alatScrolling.setPreferredSize(new Dimension(750, 550));

        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        appKotak.programjalan = true;
        appKotak.mulai();
    }

    private synchronized void mulai() {
        while (programjalan) {
            gambarUlang();
        }
    }

    public void gambarUlang() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = (Graphics) bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;
        daftarKotak.forEach((k) -> {
            k.render(g2d);
        });
        g.setColor(Color.black);
        g.fillRect(panjang * 15, lebar * 15, 40, 40);

        g.dispose();
        bs.show();
    }

    ///Input Listener
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        // System.out.println("click :" + x);
        daftarKotak.stream().filter((k) -> (x <= k.getX() + 20 && x >= k.getX() && y <= k.getY() + 20 && y >= k.getY())).forEachOrdered((k) -> {
            // System.out.println(x + "," + y + " ada click di kotak " + k.getId());
            if (!k.isAktif()) {
                k.setAktif(true);
                System.out.println("now aktif: " + k.getId());
            } else if (k.isAktif()) {
                k.setAktif(false);
                System.out.println("now tdk aktif: " + k.getId());
            }
        });

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_B:
                for (KotakKecil k : daftarKotak) {
                    if (!k.isBorder()) {
                        k.setBorder(true);
                    } else {
                        k.setBorder(false);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    static class KotakKecil extends Rectangle {

        int x, y;
        Color c = Color.gray;
        Color aktifColor = Color.black;
        int id;
        boolean aktif = false, border = false;

        public KotakKecil(int x, int y, int id) {
            this.id = id;
            this.x = x;
            this.y = y;
            System.out.println("x kotak: " + x);
        }

        public KotakKecil() {

        }

        public void setBorder(boolean b) {
            this.border = b;
        }

        public boolean isBorder() {
            return this.border;
        }

        public void setAktif(boolean a) {
            this.aktif = a;
        }

        public boolean isAktif() {
            return this.aktif;
        }

        public void render(Graphics2D g) {
            g.setStroke(new BasicStroke(3));
            if (isAktif()) {
                g.setColor(aktifColor);
            } else {
                g.setColor(c);
            }
            // g.setColor(c);
            g.fillRect(this.x, this.y, 20, 20);
            if (isBorder()) {
                g.setColor(Color.black);
                g.drawRect(this.x, this.y, 20, 20);
            }
            //   System.out.println("render  :"+x+","+y);

        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public void setColor(Color c) {
            this.c = c;
        }

        public double getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public Color getColor() {
            return c;
        }

        public void setC(Color c) {
            this.c = c;
        }

    }

}
