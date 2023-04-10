// import java.util.*;
// import java.lang.*;
// import java.io.*;

// class DunnIndex {
// public static double euclideanDistance(double[] x, double[] y) {
// double distance = 0.0;
// for (int i = 0; i < x.length; i++) {
// distance += Math.pow(x[i] - y[i], 2);
// }
// return Math.sqrt(distance);
// }

// public static double calculateDunnIndex(List<double[]> dataset,
// List<List<Integer>> clusters) {
// double maxIntraClusterDistance = Double.MIN_VALUE;
// double minInterClusterDistance = Double.MAX_VALUE;

// // Compute intra-cluster distances
// for (List<Integer> cluster : clusters) {
// for (int i = 0; i < cluster.size(); i++) {
// double[] point1 = dataset.get(cluster.get(i));
// for (int j = i + 1; j < cluster.size(); j++) {
// double[] point2 = dataset.get(cluster.get(j));
// double distance = euclideanDistance(point1, point2);
// maxIntraClusterDistance = Math.max(maxIntraClusterDistance, distance);
// }
// }
// }

// // Compute inter-cluster distances
// for (int i = 0; i < clusters.size(); i++) {
// List<Integer> cluster1 = clusters.get(i);
// double[] center1 = new double[dataset.get(0).length];
// for (int j = 0; j < cluster1.size(); j++) {
// double[] point = dataset.get(cluster1.get(j));
// for (int k = 0; k < point.length; k++) {
// center1[k] += point[k];
// }
// }
// for (int k = 0; k < center1.length; k++) {
// center1[k] /= cluster1.size();
// }
// for (int j = i + 1; j < clusters.size(); j++) {
// List<Integer> cluster2 = clusters.get(j);
// double[] center2 = new double[dataset.get(0).length];
// for (int l = 0; l < cluster2.size(); l++) {
// double[] point = dataset.get(cluster2.get(l));
// for (int k = 0; k < point.length; k++) {
// center2[k] += point[k];
// }
// }
// for (int k = 0; k < center2.length; k++) {
// center2[k] /= cluster2.size();
// }
// double distance = euclideanDistance(center1, center2);
// minInterClusterDistance = Math.min(minInterClusterDistance, distance);
// }
// }

// // Compute Dunn index
// return minInterClusterDistance / maxIntraClusterDistance;
// }

// public static void main(String[] args) {
// List<double[]> dataset = new ArrayList<>();
// dataset.add(new double[] { 1.0, 2.0 });
// }
// }

// import java.awt.Color;
// import java.awt.image.BufferedImage;
// import java.io.File;
// import java.io.IOException;
// import java.util.ArrayList;
// import javax.imageio.ImageIO;
// import java.util.Random;

// public class ImageSegmentation {

//     public static void main(String[] args) {
//         try {
//             // Load the input image
//             BufferedImage inputImage = ImageIO.read(new File("input.jpg"));
//             int width = inputImage.getWidth();
//             int height = inputImage.getHeight();

//             // Convert the input image to grayscale
//             BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
//             for (int x = 0; x < width; x++) {
//                 for (int y = 0; y < height; y++) {
//                     Color c = new Color(inputImage.getRGB(x, y));
//                     int gray = (int) (0.2126 * c.getRed() + 0.7152 * c.getGreen() + 0.0722 * c.getBlue());
//                     grayscaleImage.setRGB(x, y, new Color(gray, gray, gray).getRGB());
//                 }
//             }

//             // Perform image segmentation using k-means clustering
//             int numClusters = 3;
//             ArrayList<BufferedImage> clusters = performKMeansClustering(grayscaleImage, numClusters);

//             // Calculate the Dunn Index for the resulting clusters
//             double dunnIndex = calculateDunnIndex(clusters);
//             System.out.println("Dunn Index: " + dunnIndex);

//             // Save the segmented image
//             BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//             for (int i = 0; i < clusters.size(); i++) {
//                 BufferedImage cluster = clusters.get(i);
//                 int rgb = new Color((i + 1) * (255 / numClusters), (i + 1) * (255 / numClusters), (i + 1) * (255 / numClusters)).getRGB();
//                 for (int x = 0; x < width; x++) {
//                     for (int y = 0; y < height; y++) {
//                         if (cluster.getRGB(x, y) != Color.BLACK.getRGB()) {
//                             outputImage.setRGB(x, y, rgb);
//                         }
//                     }
//                 }
//             }
//             ImageIO.write(outputImage, "jpg", new File("output.jpg"));
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     private static ArrayList<BufferedImage> performKMeansClustering(BufferedImage inputImage, int numClusters) {
//         int width = inputImage.getWidth();
//         int height = inputImage.getHeight();
//         int numPixels = width * height;

//         // Convert the input image to a 1D array of pixel values
//         int[] pixels = new int[numPixels];
//         int index = 0;
//         for (int y = 0; y < height; y++) {
//             for (int x = 0; x < width; x++) {
//                 pixels[index] = inputImage.getRGB(x, y) & 0xff;
//                 index++;
//             }
//         }

//         // Perform k-means clustering
//         KMeans kmeans = new KMeans(numClusters, pixels);
//         kmeans.run();
//         int[] labels = kmeans.getLabels();

//         // Convert the 1D array of labels back to a 2D array of clusters
//         ArrayList<BufferedImage> clusters = new ArrayList<BufferedImage>();
//         for (int i = 0; i < numClusters; i++) {
//             BufferedImage cluster = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
//             for (int y = 0; y < height; y++) {
//                 for (int x = 0; x < width; x++) {
//                     if (labels[y * width + x] == i) {
//                         cluster.setRGB(x, y, Color.WHITE.getRGB());
//                     } else {
//                         cluster.setRGB(x, y, Color.BLACK.getRGB());
//                     }
//                 }
//             }
//             clusters.add(cluster);
//         }

//         return clusters;
//     }

//     private static double calculateDunnIndex(ArrayList<BufferedImage> clusters) {
//         int numClusters = clusters.size();
//         double maxDiameter = Double.NEGATIVE_INFINITY;
//         double minDistance = Double.POSITIVE_INFINITY;

//         // Calculate the maximum diameter and minimum distance between clusters
//         for (int i = 0; i < numClusters; i++) {
//             BufferedImage cluster1 = clusters.get(i);
//             int size1 = cluster1.getWidth() * cluster1.getHeight();
//             for (int j = i + 1; j < numClusters; j++) {
//                 BufferedImage cluster2 = clusters.get(j);
//                 int size2 = cluster2.getWidth() * cluster2.getHeight();
//                 double diameter = calculateDiameter(cluster1, cluster2);
//                 if (diameter > maxDiameter) {
//                     maxDiameter = diameter;
//                 }
//                 double distance = calculateDistance(cluster1, cluster2, size1, size2);
//                 if (distance < minDistance) {
//                     minDistance = distance;
//                 }
//             }
//         }

//         return minDistance / maxDiameter;
//     }

//     private static double calculateDiameter(BufferedImage cluster1, BufferedImage cluster2) {
//         int width1 = cluster1.getWidth();
//         int height1 = cluster1.getHeight();
//         int width2 = cluster2.getWidth();
//         int height2 = cluster2.getHeight();

//         double maxDistance = Double.NEGATIVE_INFINITY;
//         for (int y1 = 0; y1 < height1; y1++) {
//             for (int x1 = 0; x1 < width1; x1++) {
//                 if (cluster1.getRGB(x1, y1) == Color.WHITE.getRGB()) {
//                     for (int y2 = 0; y2 < height2; y2++) {
//                         for (int x2 = 0; x2 < width2; x2++) {
//                             if (cluster2.getRGB(x2, y2) == Color.WHITE.getRGB()) {
//                                 double distance = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
//                                 if (distance > maxDistance) {
//                                     maxDistance = distance;
//                                 }
//                             }
//                         }
//                     }
//                 }
//             }
//         }

//         return maxDistance;
//     }

//     private static double calculateDistance(BufferedImage cluster1, BufferedImage cluster2, int size1, int size2) {
//         int width1 = cluster1.getWidth();
//         int height1 = cluster1.getHeight();
//         int width2 = cluster2.getWidth();
//         int height2 = cluster2.getHeight();

//         double minDistance = Double.POSITIVE_INFINITY;
//         for (int y1 = 0; y1 < height1; y1++) {
//             for (int x1 = 0; x1 < width1; x1++) {
//                 if (cluster1.getRGB(x1, y1) == Color.WHITE.getRGB()) {
//                     for (int y2 = 0; y2 < height2; y2++) {
//                         for (int x2 = 0; x2 < width2; x2++) {
//                             if (cluster2.getRGB(x2, y2) == Color.WHITE.getRGB()) {
//                                 double distance = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
//                                 if (distance < minDistance) {
//                                     minDistance = distance;
//                                 }
//                             }
//                         }
//                     }
//                 }
//             }
//         }

//         return minDistance / Math.max(Math.sqrt(size1), Math.sqrt(size2));
//     }
// }        

// class KMeans {
//     private int numClusters;
//     private int maxIterations;
//     private int[][] clusterAssignments;
//     private double[][] centroids;

//     public KMeans(int numClusters, int maxIterations) {
//         this.numClusters = numClusters;
//         this.maxIterations = maxIterations;
//     }

//     public int[] cluster(int[] pixels) {
//         int numPixels = pixels.length / 3;
//         this.clusterAssignments = new int[numPixels][1];
//         this.centroids = new double[this.numClusters][3];

//         // Initialize cluster assignments randomly
//         Random random = new Random();
//         for (int i = 0; i < numPixels; i++) {
//             this.clusterAssignments[i][0] = random.nextInt(numClusters);
//         }

//         // Initialize centroids as the mean of the pixels in each cluster
//         for (int i = 0; i < numClusters; i++) {
//             int count = 0;
//             int sumRed = 0;
//             int sumGreen = 0;
//             int sumBlue = 0;
//             for (int j = 0; j < numPixels; j++) {
//                 if (this.clusterAssignments[j][0] == i) {
//                     sumRed += pixels[j * 3];
//                     sumGreen += pixels[j * 3 + 1];
//                     sumBlue += pixels[j * 3 + 2];
//                     count++;
//                 }
//             }
//             if (count > 0) {
//                 this.centroids[i][0] = (double) sumRed / count;
//                 this.centroids[i][1] = (double) sumGreen / count;
//                 this.centroids[i][2] = (double) sumBlue / count;
//             } else {
//                 this.centroids[i][0] = random.nextInt(256);
//                 this.centroids[i][1] = random.nextInt(256);
//                 this.centroids[i][2] = random.nextInt(256);
//             }
//         }

//         boolean centroidsChanged = true;
//         int iteration = 0;
//         while (centroidsChanged && iteration < maxIterations) {
//             // Assign each pixel to the nearest centroid
//             centroidsChanged = false;
//             for (int i = 0; i < numPixels; i++) {
//                 double minDistance = Double.MAX_VALUE;
//                 int closestCentroid = -1;
//                 for (int j = 0; j < numClusters; j++) {
//                     double distance = distance(
//                             pixels[i * 3], pixels[i * 3 + 1], pixels[i * 3 + 2],
//                             this.centroids[j][0], this.centroids[j][1], this.centroids[j][2]);
//                     if (distance < minDistance) {
//                         minDistance = distance;
//                         closestCentroid = j;
//                     }
//                 }
//                 if (this.clusterAssignments[i][0] != closestCentroid) {
//                     this.clusterAssignments[i][0] = closestCentroid;
//                     centroidsChanged = true;
//                 }
//             }

//             // Update centroids as the mean of the pixels in each cluster
//             for (int i = 0; i < numClusters; i++) {
//                 int count = 0;
//                 int sumRed = 0;
//                 int sumGreen = 0;
//                 int sumBlue = 0;
//                 for (int j = 0; j < numPixels; j++) {
//                     if (this.clusterAssignments[j][0] == i) {
//                         sumRed += pixels[j * 3];
//                         sumGreen += pixels[j * 3                    + 1];
//                         sumBlue += pixels[j * 3 + 2];
//                         count++;
//                     }
//                 }
//                 if (count > 0) {
//                     double newRed = (double) sumRed / count;
//                     double newGreen = (double) sumGreen / count;
//                     double newBlue = (double) sumBlue / count;
//                     if (newRed != this.centroids[i][0] ||
//                             newGreen != this.centroids[i][1] ||
//                             newBlue != this.centroids[i][2]) {
//                         centroidsChanged = true;
//                         this.centroids[i][0] = newRed;
//                         this.centroids[i][1] = newGreen;
//                         this.centroids[i][2] = newBlue;
//                     }
//                 }
//             }

//             iteration++;
//         }

//         // Convert the 1D array of cluster assignments to a 2D array of clusters
//         int[][] clusters = new int[numClusters][numPixels];
//         for (int i = 0; i < numPixels; i++) {
//             int clusterIndex = this.clusterAssignments[i][0];
//             clusters[clusterIndex][i] = 1;
//         }

//         // Return the 1D array of cluster assignments
//         int[] clusterAssignments = new int[numPixels];
//         for (int i = 0; i < numPixels; i++) {
//             clusterAssignments[i] = this.clusterAssignments[i][0];
//         }
//         return clusterAssignments;
//     }

//     private static double distance(int r1, int g1, int b1, double r2, double g2, double b2) {
//         double redDiff = r1 - r2;
//         double greenDiff = g1 - g2;
//         double blueDiff = b1 - b2;
//         return Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
//     }
// }    

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.util.Arrays;
import java.util.Random;

public class DunnIndexImage {
    public static void main(String[] args) {
        // Load image
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("UIT.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert image to 2D array of colors
        Color[][] pixels = new Color[img.getWidth()][img.getHeight()];
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                pixels[x][y] = new Color(img.getRGB(x, y));
            }
        }

        // Perform k-means clustering
        int k = 3; // number of clusters
        KMeans kMeans = new KMeans(k, pixels);
        Color[][][] clusters = kMeans.getClusters();

        // Calculate Dunn index
        double minInterClusterDist = Double.POSITIVE_INFINITY;
        double maxIntraClusterDist = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < k; i++) {
            for (int j = i + 1; j < k; j++) {
                double dist = euclideanDistance(kMeans.getCentroids()[i], kMeans.getCentroids()[j]);
                if (dist < minInterClusterDist) {
                    minInterClusterDist = dist;
                }
            }
            double intraClusterDist = 0;
            for (int x = 0; x < clusters[i].length; x++) {
                for (int y = 0; y < clusters[i][x].length; y++) {
                    intraClusterDist = Math.max(intraClusterDist,
                            euclideanDistance(kMeans.getCentroids()[i], clusters[i][x][y]));
                }
            }
            if (intraClusterDist > maxIntraClusterDist) {
                maxIntraClusterDist = intraClusterDist;
            }
        }
        double dunnIndex = minInterClusterDist / maxIntraClusterDist;

        // Display result
        System.out.println("Dunn index: " + dunnIndex);
    }

    // Euclidean distance between two colors
    private static double euclideanDistance(Color c1, Color c2) {
        if (c1 == null || c2 == null) {
            return Double.NaN;
        }
        int rDiff = c1.getRed() - c2.getRed();
        int gDiff = c1.getGreen() - c2.getGreen();
        int bDiff = c1.getBlue() - c2.getBlue();
        return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }
}

class KMeans {
    private int k;
    private Color[][] pixels;
    private Color[] centroids;
    private Color[][][] clusters;

    public KMeans(int k, Color[][] pixels) {
        this.k = k;
        this.pixels = pixels;
        this.centroids = new Color[k];
        this.clusters = new Color[k][pixels.length][pixels[0].length];

        // Initialize centroids randomly
        Random rand = new Random();
        for (int i = 0; i < k; i++) {
            int x = rand.nextInt(pixels.length);
            int y = rand.nextInt(pixels[0].length);
            centroids[i] = pixels[x][y];
        }

        // Assign pixels to clusters
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                int closestCentroidIdx = getClosestCentroidIdx(pixels[i][j]);
                clusters[closestCentroidIdx][i][j] = pixels[i][j];
            }
        }

        // Repeat until convergence
        boolean converged = false;
        while (!converged) {
            // Recalculate centroids

            for (int i = 0; i < k; i++) {
                centroids[i] = calculateCentroid(clusters[i]);
            }

            // Reassign pixels to clusters
            Color[][][] newClusters = new Color[k][pixels.length][pixels[0].length];
            for (int i = 0; i < pixels.length; i++) {
                for (int j = 0; j < pixels[0].length; j++) {
                    int closestCentroidIdx = getClosestCentroidIdx(pixels[i][j]);
                    newClusters[closestCentroidIdx][i][j] = pixels[i][j];
                }
            }

            // Check for convergence
            if (Arrays.deepEquals(clusters, newClusters)) {
                converged = true;
            } else {
                clusters = newClusters;
            }
        }
    }

    public Color[] getCentroids() {
        return centroids;
    }

    public Color[][][] getClusters() {
        return clusters;
    }

    // Euclidean distance between two colors
    private double euclideanDistance(Color c1, Color c2) {
        // if (c1 == null || c2 == null) {
        // return Double.NaN;
        // }
        int rDiff = c1.getRed() - c2.getRed();
        int gDiff = c1.getGreen() - c2.getGreen();
        int bDiff = c1.getBlue() - c2.getBlue();
        return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }

    // Index of the closest centroid to a given color
    private int getClosestCentroidIdx(Color color) {
        int closestIdx = -1;
        double minDist = Double.POSITIVE_INFINITY;
        for (int i = 0; i < k; i++) {
            double dist = euclideanDistance(color, centroids[i]);
            if (dist < minDist) {
                closestIdx = i;
                minDist = dist;
            }
        }
        return closestIdx;
    }

    // Calculate the centroid of a cluster
    // private Color calculateCentroid(Color[][] cluster) {
    // int rSum = 0, gSum = 0, bSum = 0, count = 0;
    // for (int i = 0; i < cluster.length; i++) {
    // for (int j = 0; j < cluster.length; j++) {
    // if (cluster[i][j] != null) {
    // rSum += cluster[i][j].getRed();
    // gSum += cluster[i][j].getGreen();
    // bSum += cluster[i][j].getBlue();
    // count++;
    // }
    // }
    // }
    // int rAvg = count > 0 ? rSum / count : 0;
    // int gAvg = count > 0 ? gSum / count : 0;
    // int bAvg = count > 0 ? bSum / count : 0;
    // return new Color(rAvg, gAvg, bAvg);
    // }
    private static Color calculateCentroid(Color[][] cluster) {
        int numPixels = 0;
        int rSum = 0;
        int gSum = 0;
        int bSum = 0;
        for (int i = 0; i < cluster.length; i++) {
            for (int j = 0; j < cluster[i].length; j++) {
                if (cluster[i][j] != null) { // add a null check here
                    numPixels++;
                    rSum += cluster[i][j].getRed();
                    gSum += cluster[i][j].getGreen();
                    bSum += cluster[i][j].getBlue();
                }
            }
        }
        int rAvg = (int) Math.round((double) rSum / numPixels);
        int gAvg = (int) Math.round((double) gSum / numPixels);
        int bAvg = (int) Math.round((double) bSum / numPixels);
        return new Color(rAvg, gAvg, bAvg);
    }
}
