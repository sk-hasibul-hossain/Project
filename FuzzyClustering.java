import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FuzzyClustering {

    public static void main(String[] args) {

        // parameters
        int K = 3; // number of clusters
        double m = 2.0; // fuzziness parameter
        double epsilon = 0.01; // convergence criterion

        try {

            // read image
            BufferedImage image = ImageIO.read(new File("hasibulfuzzy.jpg"));
            int width = image.getWidth();
            int height = image.getHeight();
            double[][] pixels = new double[width * height][3];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = image.getRGB(i, j);
                    pixels[i * height + j][0] = (rgb >> 16) & 0xFF;
                    pixels[i * height + j][1] = (rgb >> 8) & 0xFF;
                    pixels[i * height + j][2] = rgb & 0xFF;
                }
            }

            // initialize membership matrix
            double[][] membership = new double[width * height][K];
            for (int i = 0; i < width * height; i++) {
                double sum = 0;
                for (int j = 0; j < K; j++) {
                    membership[i][j] = Math.random();
                    sum += membership[i][j];
                }
                for (int j = 0; j < K; j++) {
                    membership[i][j] /= sum;
                }
            }

            // initialize centroids
            double[][] centroids = new double[K][3];
            for (int i = 0; i < K; i++) {
                int index = (int) (Math.random() * (width * height));
                centroids[i][0] = pixels[index][0];
                centroids[i][1] = pixels[index][1];
                centroids[i][2] = pixels[index][2];
            }

            // iterate until convergence
            boolean converged = false;
            while (!converged) {

                // update centroids
                for (int i = 0; i < K; i++) {
                    double sum1 = 0;
                    double sum2 = 0;
                    double sum3 = 0;
                    double sum4 = 0;
                    for (int j = 0; j < width * height; j++) {
                        double u = Math.pow(membership[j][i], m);
                        sum1 += u * pixels[j][0];
                        sum2 += u * pixels[j][1];
                        sum3 += u * pixels[j][2];
                        sum4 += u;
                    }
                    centroids[i][0] = sum1 / sum4;
                    centroids[i][1] = sum2 / sum4;
                    centroids[i][2] = sum3 / sum4;
                }

                // update membership values
                double[][] newMembership = new double[width * height][K];
                for (int i = 0; i < width * height; i++) {
                    for (int j = 0; j < K; j++) {
                        double sum = 0;
                        for (int k = 0; k < K; k++) {
                            double dist1 = pixels[i][0] - centroids[j][0];
                            double dist2 = pixels[i][1] - centroids[j][1];
                            double dist3 = pixels[i][2] - centroids[j][2];
                            double dist = Math.sqrt(dist1 * dist1 + dist2 * dist2 + dist3 * dist3);
                            double u = Math.pow(1 / dist, 2 / (m - 1));
                            sum += u;
                        }
                        newMembership[i][j] = 1 / sum;
                    }
                }

                // check convergence
                double maxDiff = 0;
                for (int i = 0; i < width * height; i++) {
                    for (int j = 0; j < K; j++) {
                        double diff = Math.abs(membership[i][j] - newMembership[i][j]);
                        if (diff > maxDiff) {
                            maxDiff = diff;
                        }
                    }
                }
                if (maxDiff < epsilon) {
                    converged = true;
                } else {
                    membership = newMembership;
                }
            }

            // create segmented image
            BufferedImage segmented = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    double maxMembership = -1;
                    int maxIndex = -1;
                    for (int k = 0; k < K; k++) {
                        if (membership[i * height + j][k] > maxMembership) {
                            maxMembership = membership[i * height + j][k];
                            maxIndex = k;
                        }
                    }
                    int rgb = ((int) centroids[maxIndex][0] << 16) | ((int) centroids[maxIndex][1] << 8)
                            | (int) centroids[maxIndex][2];
                    segmented.setRGB(i, j, rgb);
                }
            }

            // write segmented image
            ImageIO.write(segmented, "jpg", new File("segmented.jpg"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
