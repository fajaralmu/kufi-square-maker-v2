/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebookswing.painting;

/**
 *
 * @author fajar
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.util.List;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author fajar
 */
public class AppKotakPanel extends JPanel implements MouseListener, KeyListener {

    public static int panjang = 0, lebar = 0;
    public static List<KotakKecil> daftarKotak = new ArrayList<>();
    private boolean programjalan = false;
    private static JFrame frame;
    private static AppKotakPanel appKotak;
    private static JPanel panelDasar;
    private static JScrollPane alatScrolling;
    private boolean warnaSatuan = false, kotakTebal = false;
    Color warnaPena = Color.black;

    public void inisiasiKotakKecil(int panjang, int lebar) {
        daftarKotak.clear();
        int id = 1;
        for (int i = 0; i <= panjang; i++) {
            for (int j = 0; j <= lebar; j++) {
                KotakKecil k = new KotakKecil(i * 20, j * 20, id);
                k.setWarnaBiasa(Color.white);
                daftarKotak.add(k);
                id++;
            }
        }
    }

    public static void main(String[] args) {
        frame = new JFrame("Kufi SQR");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String pjg = JOptionPane.showInputDialog("Please input length: ");
        String lbr = JOptionPane.showInputDialog("Please input width: ");

        panjang = Integer.parseInt(pjg);
        lebar = Integer.parseInt(lbr);
        appKotak = new AppKotakPanel();
        appKotak.addMouseListener(appKotak);
        appKotak.addKeyListener(appKotak);
        appKotak.setFocusable(true);
        appKotak.requestFocus();
        appKotak.buatMenu(frame);

        AppKotakPanel.settingLayout(panjang, lebar);
        appKotak.mulai();

    }

    private synchronized void mulai() {
        while (programjalan) {
            repaint();
        }
    }

    public void buatMenu(JFrame frame) {
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        menubar.add(file);
        JMenuItem lembarBaru = new JMenuItem("Baru");
        JMenuItem tutupLembar = new JMenuItem("Keluar");
        JMenuItem simpanLembar = new JMenuItem("Simpan");

        lembarBaru.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        lembarBaru.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pjg = JOptionPane.showInputDialog("Please input length: ");
                String lbr = JOptionPane.showInputDialog("Please input width: ");

                panjang = Integer.parseInt(pjg);
                lebar = Integer.parseInt(lbr);

                appKotak.inisiasiKotakKecil(panjang, lebar);

            }
        });
        simpanLembar.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        simpanLembar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                appKotak.simpanGambar();
            }
        });
        tutupLembar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        file.add(lembarBaru);
        file.add(simpanLembar);
        file.add(tutupLembar);

        JMenu edit = new JMenu("Edit warna");
        menubar.add(edit);
        JMenuItem warnaDasar = new JMenuItem("Warna gambar");
        JMenuItem editWarnaPena = new JMenuItem("Warna pena");
        JMenuItem warnaBkgrd = new JMenuItem("Warna background");
        JMenuItem setWarnaPena = new JMenuItem("Set/unset warna pena (W)");

        warnaDasar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JColorChooser colorChooser = new JColorChooser();
                Color c = colorChooser.showDialog(null, "pilih warna dasar", Color.yellow);
                appKotak.setWarnaDasar(c);
            }
        });
        editWarnaPena.setAccelerator(KeyStroke.getKeyStroke('P', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        editWarnaPena.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JColorChooser colorChooser = new JColorChooser();
                Color c = colorChooser.showDialog(null, "pilih warna dasar", Color.yellow);
                warnaPena = c;
                warnaSatuan = true;
            }
        });
        setWarnaPena.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPakaiWarnaSatuan();
            }
        });
        warnaBkgrd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JColorChooser colorChooser = new JColorChooser();
                Color c = colorChooser.showDialog(null, "pilih warna background", Color.yellow);
                appKotak.setWarnaBackground(c);
            }
        });
        edit.add(warnaDasar);
        edit.add(warnaBkgrd);
        edit.add(editWarnaPena);
        edit.add(setWarnaPena);

        JMenu alat = new JMenu("Alat");
        menubar.add(alat);
        JMenuItem tambahBorder = new JMenuItem("Tambah/hapus border (B)");
        JMenuItem bersihkanPanel = new JMenuItem("Bersihkan panel (X)");
        JMenuItem tebalKotak = new JMenuItem("Atur ketebalan kotak");
        JMenuItem alatSetKotakTebal = new JMenuItem("Set/unset kotaktebal (T)");

        tambahBorder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingBorder();
            }
        });
        bersihkanPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bersihPanel();
            }
        });
        tebalKotak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ketebalan = Integer.parseInt(JOptionPane.showInputDialog("Masukan ketebalan:"));
                for (KotakKecil k : daftarKotak) {
                    // k.setBorderKotak(true);
                    k.setTebalBorder(ketebalan);
                }
            }
        });
        alatSetKotakTebal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setKotakTebal();
            }
        });
        alat.add(tebalKotak);
        alat.add(tambahBorder);
        alat.add(bersihkanPanel);
        alat.add(alatSetKotakTebal);

        frame.setJMenuBar(menubar);
    }

    public static void settingLayout(int panjang, int lebar) {
        panelDasar = new JPanel();
        panelDasar.setBackground(Color.GREEN);
        appKotak.setPreferredSize(new Dimension(panjang * 20, lebar * 20));
        //  appKotak.setBackground(Color.green);
        appKotak.inisiasiKotakKecil(panjang, lebar);

        alatScrolling = new JScrollPane(panelDasar, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        alatScrolling.getViewport().add(appKotak);
        alatScrolling.setPreferredSize(new Dimension(750, 550));

        panelDasar.add(alatScrolling);

        frame.add(panelDasar);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        appKotak.programjalan = true;
    }

    public void setWarnaBackground(Color c) {
        daftarKotak.forEach((k) -> {
            k.setWarnaBiasa(c); //warna 
        });
        // appKotak.setBackground(c);
    }

    public void setWarnaDasar(Color c) {
        daftarKotak.forEach((k) -> {
            k.setWarnaAktif(c);
        });
    }

    public void bersihPanel() {
        daftarKotak.forEach((k) -> {
            k.setAktif(false);
        });
    }

    public void settingBorder() {
        daftarKotak.forEach((k) -> {
            if (!k.isBorderPenggaris()) {
                k.setBorderPenggaris(true);
            } else {
                k.setBorderPenggaris(false);
            }
        });
    }

    public void setKotakTebal() {
        daftarKotak.forEach((k) -> {
            if (!k.isBorderKotak()) {
                k.setBorderKotak(true);
            } else {
                k.setBorderKotak(false);
            }
        });
    }

    public void setPakaiWarnaSatuan() {
        if (!warnaSatuan) {
            warnaSatuan = true;
        } else {
            warnaSatuan = false;
        }
    }

    private void simpanGambar() {
        BufferedImage imagebuf = null;
        try {
            imagebuf = new Robot().createScreenCapture(this.bounds());
        } catch (AWTException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
        fileChooser.addChoosableFileFilter(filter);
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + file.getAbsolutePath());
            Graphics2D graphics2D = imagebuf.createGraphics();
            this.paint(graphics2D);

            try {
                ImageIO.write(imagebuf, "jpeg", new File(file.getAbsolutePath() + ".jpeg"));
                JOptionPane.showMessageDialog(null, "Gambar berhasil disimpan!", "Kufi SQR", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println(e.getStackTrace());
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        daftarKotak.forEach((k) -> {
            k.render(g2d);
        });
    }

    ///Input Listener
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        daftarKotak.stream().filter((k) -> (x <= k.getX() + 20 && x >= k.getX() && y <= k.getY() + 20 && y >= k.getY())).forEachOrdered((k) -> {
            if (!k.isAktif()) {
                if (warnaSatuan) {
                    k.setWarnaAktif(warnaPena);
                }
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
                settingBorder();
                break;
            case KeyEvent.VK_X:
                bersihPanel();
                break;
            case KeyEvent.VK_W:
                setPakaiWarnaSatuan();
                break;
            case KeyEvent.VK_T:
                setKotakTebal();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    static class KotakKecil {

        private int x, y, id, tebalBorder = 1;
        private Color warnaBiasa = Color.gray, warnaAktif = Color.black;
        private boolean aktif = false, borderPenggaris = true, borderKotak = false;

        public KotakKecil(int x, int y, int id) {
            this.id = id;
            this.x = x;
            this.y = y;
            System.out.println("x kotak: " + x);
        }

        public void render(Graphics2D g) {
            g.setStroke(new BasicStroke(2));
            if (isBorderPenggaris()) {
                g.setColor(Color.black);
                g.drawRect(this.x, this.y, 20, 20);
            }
            if (isAktif()) {
                g.setColor(warnaAktif);
                if (isBorderKotak()) {
                    g.setStroke(new BasicStroke(getTebalBorder()));
                    int[] xPos = new int[3];
                    xPos[0] = (int) getX() + 20;
                    xPos[1] = (int) getX();
                    xPos[2] = (int) getX();
                    int[] yPos = new int[3];
                    yPos[0] = (int) getY();
                    yPos[1] = (int) getY();
                    yPos[2] = (int) getY() + 20 - getTebalBorder() / 2;
                    g.drawPolyline(xPos, yPos, 3);

                    // g.drawRect(this.x, this.y, 20, 20);
                }
            } else {
                g.setColor(warnaBiasa);
            }
            g.fillRect(this.x, this.y, 20, 20);

        }

        public void setBorderPenggaris(boolean b) {
            this.borderPenggaris = b;
        }

        public boolean isBorderPenggaris() {
            return this.borderPenggaris;
        }

        public void setAktif(boolean a) {
            this.aktif = a;
        }

        public boolean isAktif() {
            return this.aktif;
        }

        public boolean isBorderKotak() {
            return borderKotak;
        }

        public void setBorderKotak(boolean borderKotak) {
            this.borderKotak = borderKotak;
        }

        public int getTebalBorder() {
            return tebalBorder;
        }

        public void setTebalBorder(int tebalBorder) {
            this.tebalBorder = tebalBorder;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public void setWarnaBiasa(Color c) {
            this.warnaBiasa = c;
        }

        public void setWarnaAktif(Color c) {
            this.warnaAktif = c;
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

        public Color getWarnaBiasa() {
            return warnaBiasa;
        }

    }

}
