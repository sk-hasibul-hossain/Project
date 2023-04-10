import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class KMeansClusteringRGB {
    public static File f = null;
    public static BufferedImage img;
    public static int w = 0;
    public static int h = 0;
    public static int c1 = 0;
    public static int c2 = 0;
    public static int c3 = 0;

    public static int r(int x, int y) {
        int p = img.getRGB(x, y);
        int r = (p >> 16) & 0xff;
        return r;
    }

    public static int g(int x, int y) {
        int p = img.getRGB(x, y);
        int g = (p >> 8) & 0xff;
        return g;
    }

    public static int b(int x, int y) {
        int p = img.getRGB(x, y);
        int b = (p & 0xff);
        return b;
    }

    public static int a(int x, int y) {
        int p = img.getRGB(x, y);
        int a = (p >> 24) & 0xff;
        return a;
    }

    public static int getVal(int x, int y) {
        // System.out.println(r(x, y) + " " + g(x, y) + " " + b(x, y) + " " + a(x, y));
        return r(x, y) + g(x, y) + b(x, y) + a(x, y);
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
            f = new File("./selected.jpg");
            img = ImageIO.read(f);
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
        // System.out.println(c1 + " " + c2 + " " + c3);
        int rsum1 = 0, gsum1 = 0, bsum1 = 0, asum1 = 0;
        int rsum2 = 0, gsum2 = 0, bsum2 = 0, asum2 = 0;
        int rsum3 = 0, gsum3 = 0, bsum3 = 0, asum3 = 0;
        int count1 = 0, count2 = 0, count3 = 0;
        int prev1 = 0, prev2 = 0, prev3 = 0;
        while (true) {
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int gv = getVal(x, y);
                    // System.out.println(modulasVal(gv, c1) + " " + modulasVal(gv, c2) + " " +
                    // modulasVal(gv, c3));
                    if (modulasVal(gv, c1) < modulasVal(gv, c2)) {
                        if (modulasVal(gv, c1) < modulasVal(gv, c3)) {
                            rsum1 = rsum1 + r(x, y);
                            gsum1 = gsum1 + g(x, y);
                            bsum1 = bsum1 + b(x, y);
                            asum1 = asum1 + a(x, y);
                            count1++;
                        } else {
                            // sum3 = sum3 + gv;
                            rsum3 = rsum3 + r(x, y);
                            gsum3 = gsum3 + g(x, y);
                            bsum3 = bsum3 + b(x, y);
                            asum3 = asum3 + a(x, y);
                            count3++;
                            // System.out.print(" (C3) ");
                        }
                    } else {
                        if (modulasVal(gv, c2) < modulasVal(gv, c3)) {
                            // sum2 = sum2 + gv;
                            rsum2 = rsum2 + r(x, y);
                            gsum2 = gsum2 + g(x, y);
                            bsum2 = bsum2 + b(x, y);
                            asum2 = asum2 + a(x, y);
                            count2++;
                        } else {
                            // sum3 = sum3 + gv;
                            rsum3 = rsum3 + r(x, y);
                            gsum3 = gsum3 + g(x, y);
                            bsum3 = bsum3 + b(x, y);
                            asum3 = asum3 + a(x, y);
                            count3++;
                        }
                    }
                }
            }
            System.out.print(
                    (rsum1 / count1) + " " + (rsum1 / count1) + " " + (rsum1 / count1) + " " + (asum1 / count1) + ", ");
            System.out.print((rsum2 / count2) + ", " + (rsum2 / count2) + " " + (rsum2 / count2) + " "
                    + (asum2 / count2) + ", ");
            System.out.println(
                    (rsum3 / count3) + ", " + (rsum3 / count3) + " " + (rsum3 / count3) + " " + (asum3 / count3));
            prev1 = c1;
            prev2 = c2;
            prev3 = c3;
            // c1 = (sum1 / count1);
            // c2 = (sum2 / count2);
            // c3 = (sum3 / count3);
            c1 = (rsum1 / count1) + (rsum1 / count1) + (rsum1 / count1) + (asum1 / count1);
            c2 = (rsum2 / count2) + (rsum2 / count2) + (rsum2 / count2) + (asum2 / count2);
            c3 = (rsum3 / count3) + (rsum3 / count3) + (rsum3 / count3) + (asum3 / count3);
            // sum1 = sum2 = sum3 = 0;
            rsum1 = gsum1 = bsum1 = asum1 = 0;
            rsum2 = gsum2 = bsum2 = asum2 = 0;
            rsum3 = gsum3 = bsum3 = asum3 = 0;
            count1 = count2 = count3 = 0;
            if (prev1 == c1 && prev2 == c2 && prev3 == c3) {
                break;
            }
            // if (t == 10)
            // break;

        }
        // System.out.println("count= " + count1 + " sum1= " + sum1 + " count2= " +
        // count2 + " sum2= " + sum2 + " count3= " + count3);
        // System.out.print((sum1 / count1) + " " + (sum2 / count2) + " " + (sum3 /
        // count3));
    }
}