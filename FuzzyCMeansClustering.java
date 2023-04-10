
//workable
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;

public class FuzzyCMeansClustering {

    private int K; // number of clusters
    private double m; // fuzzifier
    private double epsilon; // convergence threshold
    private int maxIterations; // maximum number of iterations
    public double[][] centroids; // cluster centroids

    public FuzzyCMeansClustering(int K, double m, double epsilon, int maxIterations) {
        this.K = K;
        this.m = m;
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
    }

    private double[][] initializeMembershipMatrix(int n, int K) {
        Random r = new Random();
        double[][] u = new double[n][K];
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < K; j++) {
                u[i][j] = r.nextDouble();
                sum += u[i][j];
            }
            for (int j = 0; j < K; j++) {
                u[i][j] /= sum;
            }
        }
        return u;
    }

    private double[][] calculateCentroids(double[][] data, double[][] u) {
        int n = data.length;
        int d = data[0].length;
        double[][] centroids = new double[K][d];
        for (int k = 0; k < K; k++) {
            double sum1 = 0;
            double sum2 = 0;
            for (int i = 0; i < n; i++) {
                double uikm = Math.pow(u[i][k], m);
                for (int j = 0; j < d; j++) {
                    centroids[k][j] += uikm * data[i][j];
                }
                sum1 += uikm;
            }
            for (int j = 0; j < d; j++) {
                centroids[k][j] /= sum1;
            }
        }
        return centroids;
    }

    private double[][] updateMembershipMatrix(double[][] data, double[][] centroids) {
        int n = data.length;
        double[][] u = new double[n][K];
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < K; k++) {
                double sum = 0;
                for (int j = 0; j < K; j++) {
                    double dist1 = distance(data[i], centroids[k]);
                    double dist2 = distance(data[i], centroids[j]);
                    u[i][k] += Math.pow(dist1 / dist2, 2.0 / (m - 1));
                }
                u[i][k] = 1.0 / u[i][k];
            }
        }
        return u;
    }

    private double distance(double[] a, double[] b) {
        int d = a.length;
        double sum = 0;
        for (int i = 0; i < d; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(sum);
    }

    public double[][] cluster(double[][] data) {
        int n = data.length;
        double[][] u = initializeMembershipMatrix(n, K);
        centroids = calculateCentroids(data, u);
        double[][] uPrev = u;
        double delta = Double.MAX_VALUE;
        int t = 0;
        while (delta > epsilon && t < maxIterations) {
            uPrev = u;
            centroids = calculateCentroids(data, uPrev);
            u = updateMembershipMatrix(data, centroids);
            delta = 0;
            for (int i = 0; i < n; i++) {
                for (int k = 0; k < K; k++) {
                    delta += Math.pow(u[i][k] - uPrev[i][k], 2);
                }
            }
            t++;
        }
        return u;
    }

    public static void main(String[] args) {
        int K = 5;
        double m = 2.0;
        double epsilon = 1e-6;
        int maxIterations = 100;

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("murgi.jpg"));
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        int width = img.getWidth();
        int height = img.getHeight();
        double[][] data = new double[width * height][3];

        int index = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color c = new Color(img.getRGB(j, i));
                data[index][0] = c.getRed();
                data[index][1] = c.getGreen();
                data[index][2] = c.getBlue();
                index++;
            }
        }

        FuzzyCMeansClustering fcm = new FuzzyCMeansClustering(K, m, epsilon, maxIterations);
        double[][] u = fcm.cluster(data);

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double[] membership = u[i * width + j];
                double max = membership[0];
                int maxIndex = 0;
                for (int k = 1; k < K; k++) {
                    if (membership[k] > max) {
                        max = membership[k];
                        maxIndex = k;
                    }
                }
                Color c = new Color((int) fcm.centroids[maxIndex][0], (int) fcm.centroids[maxIndex][1],
                        (int) fcm.centroids[maxIndex][2]);
                result.setRGB(j, i, c.getRGB());
            }
        }

        try {
            File output = new File("output.png");
            ImageIO.write(result, "png", output);
            System.out.println("Output image saved.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
