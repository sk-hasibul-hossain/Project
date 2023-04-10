import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;

class Browse implements ActionListener {
    JButton btn, browse;
    Button btnObj;

    Browse(JButton btn, JButton browse, Button btnObj) {
        this.btn = btn;
        this.btn.setEnabled(false);
        this.browse = browse;
        this.btnObj = btnObj;
        // this.browse.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == browse) {
            JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView());
            // Open the save dialog
            j.showSaveDialog(null);
            // System.out.println(str);
            // System.out.println(j.getSelectedFile().getAbsolutePath());
            this.btn.setEnabled(true);
            btnObj.path = j.getSelectedFile().getAbsolutePath();

        }
    }
}

class Button implements ActionListener {
    JButton btn;
    String path;
    JLabel label;

    Button(JButton btn, JLabel label) {
        this.btn = btn;
        this.label = label;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        ImageIcon icon = new ImageIcon(path);
        label.setIcon(icon);
        System.out.println(path);
        // String path = "./manna.jpeg"; // image path
        // String outputFileName = "ooooooo.jpg";// output file name with extension
        // GrayScale gray_obj = new GrayScale(path, outputFileName);

        // Clustering
        Cluster obj = new Cluster();
        List<List<Integer>> c1 = new LinkedList<List<Integer>>();
        List<List<Integer>> c2 = new LinkedList<edList<Integer>>();
        List<List<Integer>> c3 = new LinkedList<List<Integer>>();
        obj.fun(c1, c2, c3, path);
        System.out.println(c1 + " " + c2 + " " + c3);
    }
}
/* Gray scale conversion */
// class Image {
// File file = null;
// BufferedImage image;
// int width;
// int height;

// public Image(String path) {
// try {
// file = new File(path);
// image = ImageIO.read(file);
// } catch (Exception e) {
// System.out.println("Error");
// }
// // size of image
// width = image.getWidth();
// height = image.getHeight();
// System.out.println(width + "*" + height);
// }
// }

// class GrayScale extends Image {
// GrayScale(String path, String outputFileName) {
// super(path);
// System.out.println(height);
// // grayscale Conversion
// for (int y = 0; y < height; y++) {
// for (int x = 0; x < width; x++) {
// int p = image.getRGB(x, y);
// // System.out.print(" " + p + " ");
// int a = (p >> 24) & 0xff;
// int r = (p >> 16) & 0xff;
// int g = (p >> 8) & 0xff;
// int b = (p & 0xff);

// // int avg = (int) Math.sqrt((r * r) + (g * g) + (b * b));
// int avg = (r + g + b) / 3;
// p = (a << 24) | (avg << 16) | (avg << 8) | avg;

// image.setRGB(x, y, p);
// }
// }
// // output
// try {
// System.out.println("successful");
// file = new File("\\log\\" + outputFileName);
// ImageIO.write(image, "jpg", file);
// } catch (Exception e) {
// System.out.print("Error Occoured during Grayscale output");
// }
// }
// }
/* GrayScale conversion end */

/* Kmeans Theoram start */
class Cluster {
    public File file1 = null;
    public BufferedImage img;
    public File file2 = null;
    public BufferedImage img2;
    public int w = 0;
    public int h = 0;
    public int c1 = 0;
    public int c2 = 0;
    public int c3 = 0;

    public int getVal(int x, int y) {
        int p = img.getRGB(x, y);
        int gray = (p & 0xff);// blue
        return gray;
    }

    public int randFunction(int val) {
        int max = val;
        int min = 0;
        double range = (max - min) + 1;
        // System.out.print("Range " + range + " ");
        int r = 0;
        while (true) {
            r = (int) (Math.random() * range) + 0;
            if (r <= val) {
                break;
            }
        }
        return (int) r;
    }

    public int modulasVal(int val, int mid) {
        return val > mid ? (val - mid) : (mid - val);
    }

    void fun(List<List<Integer>> l1, List<List<Integer>> l2, List<List<Integer>> l3,
            String path) {
        try {
            file1 = new File("./grayScale.jpg");
            file2 = new File("./grayScale.jpg");
            // file1 = new File(path);
            // file2 = new File(path);
            img = ImageIO.read(file1);
            img2 = ImageIO.read(file2);
        } catch (Exception e) {
            System.out.println("Image file not found");
        }
        h = img.getHeight();
        w = img.getWidth();
        System.out.println("Height= " + h + " Width= " + w);
        c1 = getVal(randFunction(w), randFunction(h));
        c2 = getVal(randFunction(w), randFunction(h));
        c3 = getVal(randFunction(w), randFunction(h));

        // clustring
        System.out.println("c1= " + c1 + " c2= " + c2 + " c3= " + c3);
        int sum1 = 0, sum2 = 0, sum3 = 0;
        int count1 = 0, count2 = 0, count3 = 0;
        int prev1 = 0, prev2 = 0, prev3 = 0;
        int p;
        while (true) {
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int gv = getVal(x, y);
                    if (modulasVal(gv, c1) < modulasVal(gv, c2)) {
                        if (modulasVal(gv, c1) < modulasVal(gv, c3)) {
                            p = (255 << 24) | (c1 << 16) | (c1 << 8) | c1;
                            img2.setRGB(x, y, p);
                            sum1 = sum1 + gv;
                            count1++;
                        } else {
                            p = (255 << 24) | (c3 << 16) | (c3 << 8) | c3;
                            img2.setRGB(x, y, p);
                            sum3 = sum3 + gv;
                            count3++;
                            // System.out.print(" (C3) ");
                        }
                    } else {
                        if (modulasVal(gv, c2) < modulasVal(gv, c3)) {
                            p = (255 << 24) | (c2 << 16) | (c2 << 8) | c2;
                            img2.setRGB(x, y, p);
                            sum2 = sum2 + gv;
                            count2++;
                        } else {
                            p = (255 << 24) | (c3 << 16) | (c3 << 8) | c3;
                            img2.setRGB(x, y, p);
                            sum3 = sum3 + gv;
                            count3++;
                        }
                    }
                }
            }
            System.out.println((sum1 / count1) + " " + (sum2 / count2) + " " + (sum3 / count3) + ", " + count1 + " "
                    + count2 + " " + count3);
            List<Integer> list1 = new LinkedList<Integer>();
            list1.add(c1);
            list1.add(count1);
            l1.add(list1);

            List<Integer> list2 = new LinkedList<Integer>();
            list2.add(c2);
            list2.add(count2);
            l2.add(list2);

            List<Integer> list3 = new LinkedList<Integer>();
            list3.add(c3);
            list3.add(count3);
            l3.add(list3);

            prev1 = c1;
            prev2 = c2;
            prev3 = c3;
            c1 = (sum1 / count1);
            c2 = (sum2 / count2);
            c3 = (sum3 / count3);
            sum1 = sum2 = sum3 = 0;
            count1 = count2 = count3 = 0;
            if (prev1 == c1 && prev2 == c2 && prev3 == c3) {
                break;
            }
        }
        try {
            File f3 = new File("outFile.jpg");
            ImageIO.write(img2, "jpg", f3);
        } catch (Exception e) {
            System.out.print("Error Occoured during Grayscale output");
        }

    }
}

/* K means theoram end */
public class Main {
    public static Container c;
    public static JFrame f;

    public static void main(String args[]) throws IOException {

        f = new JFrame("Image processing");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // f.setUndecorated(true);
        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);

        c = f.getContentPane();
        c.setLayout(null);
        JButton browse = new JButton("Browse");
        JButton btn = new JButton("View");

        browse.setBounds(100, 100, 100, 50);
        btn.setBounds(100, 200, 100, 50);
        c.add(browse);
        c.add(btn);

        ImageIcon icon = new ImageIcon("");
        JLabel label = new JLabel(icon);

        label.setBounds(300, 0, 800, 700);

        Button btnObj = new Button(btn, label);
        Browse browsObj = new Browse(btn, browse, btnObj);

        browse.addActionListener(browsObj);
        btn.addActionListener(btnObj);

        c.add(label);
        // f.add(scroll);
        f.setVisible(true);

    }
}