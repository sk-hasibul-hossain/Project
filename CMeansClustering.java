import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
//import javax.naming.spi.ObjectFactoryBuilder;

//import net.sf.javaml.distance.EuclideanDistance;

import java.io.File;
import java.util.*;
//import java.io.IOException;

class Image {
    File file = null;
    BufferedImage image;
    int width;
    int height;

    public Image(String path) {
        try {
            file = new File(path);
            image = ImageIO.read(file);
        } catch (Exception e) {
            System.out.println("Error");
        }
        // size of image
        width = image.getWidth();
        height = image.getHeight();
        System.out.println(width + "*" + height);
    }
}

class GrayScale extends Image {
    BufferedImage grayscaleImage; // not needed to create new variable

    GrayScale(String path, String outputFileName) {
        super(path);
        System.out.println(height);
        // grayscale Conversion
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = image.getRGB(x, y);
                // System.out.print(" " + p + " ");
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = (p & 0xff);

                // int avg = (int) Math.sqrt((r * r) + (g * g) + (b * b));
                int avg = (r + g + b) / 3;
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;

                image.setRGB(x, y, p);
            }
        }
        // output
        try {
            file = new File(outputFileName);
            ImageIO.write(image, "jpg", file);
        } catch (Exception e) {
            System.out.print("Error Occoured during Grayscale output");
        }
        // GrayScale Image variable assign
        grayscaleImage = image; // not need to assign
    }
}

// clustering logic
class EuclideanDistance {
    BufferedImage image;
    int width = 0;
    int height = 0;
    int k;
    int positionOfVj = 0;

    List<List<Double>> vj = new LinkedList<>(); // cluster points
    List<List<Double>> uij = new LinkedList<>();

    EuclideanDistance(BufferedImage image, int k) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.k = k;
    }

    // select k random points
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

    public int[][] RandomChoseCluster() { // k is a number of cluster
        int clusterPoints[][] = new int[k][2]; // store the cluster point by using random funtion
        for (int i = 0; i < k; i++) {
            clusterPoints[i][0] = randFunction(this.width);
            clusterPoints[i][1] = randFunction(this.height);
        }
        return clusterPoints;
    }

    public double euclidianDistance(double clusterCenter, double actualPixel) {
        double distance = Math.abs(actualPixel - clusterCenter);
        return distance;
    }
}

class MemberShipCluster extends EuclideanDistance {
    // BufferedImage image;
    // int numberOfCluster;
    // super(image);
    int m;
    double bita;

    MemberShipCluster(BufferedImage image, int k, int m, double bita) {
        super(image, k);
        this.m = m;
        this.bita = bita;
    }

    // intially store the random k point values
    public void initallyPutKValues() {
        List<Double> helpList = new LinkedList<>();
        int randomPintsArr[][] = RandomChoseCluster();
        for (int i = 0; i < k; i++) {
            int x = randomPintsArr[i][0];
            int y = randomPintsArr[i][1];
            double px = imagePixelExtraction(x, y);
            helpList.add(px);
        }
        vj.add(helpList);
    }

    // int sizeOfClusterPointsArray = clusterPoints.length;
    // int clusterValues[] = new int[sizeOfClusterPointsArray];
    public void display() {
        System.out.println("MemberShipCluster is called");
        int clusterPoints[][] = RandomChoseCluster();
        for (int i = 0; i < k; i++) {
            System.out.println(clusterPoints[i][0] + " " + clusterPoints[i][1]);
        }
    }

    public double fuzzyMembership(double pixel) {
        double u = 0.0;
        List<Double> list = vj.get(0);
        for (int i = 0; i < k; i++) {
            u += Math.pow((pixel / euclidianDistance(list.get(i), pixel)), (2 / (m - 1)));
        }
        return ((1 * 1.0) / u);
    }

    // invoke pixel one by one we get fuzzy centers vj
    public void fuzzyCenters() {
        int x = width;
        double u1 = 0.0, u2 = 0.0;
        for (int i = 0; i < x; i++) {
            List<Double> helpList = new LinkedList<>();
            for (int j = 0; j < height; j++) {
                double fuzzyMembershipValue = fuzzyMembership(imagePixelExtraction(i, j));
                helpList.add(fuzzyMembershipValue);
                u1 = fuzzyMembershipValue * imagePixelExtraction(i, j) + u1;
                u2 += fuzzyMembershipValue;
                // uij.get(i).add(fuzzyMembership(imagePixelExtraction(i, j)));
            }
            // System.out.println(i);
            uij.add(helpList);
        }
        System.out.println(u1 / u2);// we can see the result by using print
        // we have to add in vj
    }

    public double imagePixelExtraction(int x, int y) {
        int p = image.getRGB(x, y);
        // System.out.print(" " + p + " ");
        int a = (p >> 24) & 0xff;
        double r = (p >> 16) & 0xff;
        // int g = (p >> 8) & 0xff;
        // int b = (p & 0xff);
        return r;
    }
}

public class CMeansClustering {

    public static void main(String args[]) {
        GrayScale g = new GrayScale("./m.jpg", "selectedGrayscaleForCMeansCluster.jpg");
        BufferedImage image = g.image;
        int numberOfCluster = 3;
        int m = 2; // fuzziness
        double bita = 0.7; // bita range between 0-1
        MemberShipCluster objectOfMemberShipCluster = new MemberShipCluster(image, numberOfCluster, m, bita);
        // objectOfMemberShipCluster.image = image;
        // objectOfMemberShipCluster.numberOfCluster = 3;
        // objectOfMemberShipCluster.RandomChoseCluster();
        // new MemberShipCluster().display();
        // objectOfMemberShipCluster.display();
        objectOfMemberShipCluster.initallyPutKValues();
        objectOfMemberShipCluster.fuzzyCenters();
    }
}
