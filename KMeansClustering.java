import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class KMeansClustering {
    public static File f = null;
    public static BufferedImage img;
    public static File f2 = null;
    public static BufferedImage img2;
    public static int w = 0;
    public static int h = 0;
    public static int c1 = 0;
    public static int c2 = 0;
    public static int c3 = 0;

    public static int getVal(int x, int y) {
        int p = img.getRGB(x, y);
        // int a = (p >> 24) & 0xff;
        int gray = (p & 0xff);// blue
        // System.out.println(gray);
        // int acc = (a << 24) | (gray << 16) | (gray << 8) | gray;

        // int a = (p >> 24) & 0xff;
        // int r = (p >> 16) & 0xff;
        // int g = (p >> 8) & 0xff;
        // int b = (p & 0xff);
        // // System.out.println(a + " " + gray);
        // int acc = (a << 24) | (r << 16) | (g << 8) | b;
        // System.out.println(r + " " + g + " " + b + " " + a);
        return gray;
    }

    public static int randFunction(int val) {
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

    public static int modulasVal(int val, int mid) {
        return val > mid ? (val - mid) : (mid - val);
    }

    public static void main(String args[]) throws IOException {
        try {
            f = new File("./grayScale.jpg");
            f2 = new File("./grayScale.jpg");
            img = ImageIO.read(f);
            img2 = ImageIO.read(f2);
        } catch (Exception e) {
            System.out.println("Image file not found");
        }
        h = img.getHeight();
        w = img.getWidth();
        System.out.println("Height= " + h + " Width= " + w);
        c1 = getVal(randFunction(w), randFunction(h));
        c2 = getVal(randFunction(w), randFunction(h));
        c3 = getVal(randFunction(w), randFunction(h));
        // c1 = 170;
        // c2 = 58;
        // c3 = 224;

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
                    // System.out.println(modulasVal(gv, c1) + " " + modulasVal(gv, c2) + " " +
                    // modulasVal(gv, c3));
                    // int t1,t2,t3;
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
            System.out.println((sum1 / count1) + " " + (sum2 / count2) + " " + (sum3 / count3) + ", ");
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
        // System.out.println("count= " + count1 + " sum1= " + sum1 + " count2= " +
        // count2 + " sum2= " + sum2 + " count3= " + count3);
        // System.out.print((sum1 / count1) + " " + (sum2 / count2) + " " + (sum3 /
        // count3));
    }
}