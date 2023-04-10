// import java.awt.image.BufferedImage;
// import javax.imageio.ImageIO;
// import java.io.File;
// import java.io.IOException;
// import java.awt.*;
// import javax.swing.*;

// import net.sf.javaml.classification.KNearestNeighbors;
// import net.sf.javaml.core.Dataset;
// import net.sf.javaml.tools.data.FileHandler;

// //import com.example.clusterer.FuzzyCMeansClusterer;
// //import net.sf.javaml.clustering.FuzzyCMeansClusterer;
// import net.sf.javaml.core.Dataset;
// import net.sf.javaml.core.DefaultDataset;
// import net.sf.javaml.distance.EuclideanDistance;

// import java.awt.Color;
// import java.awt.image.BufferedImage;
// import java.io.File;
// import java.io.IOException;
// import javax.imageio.ImageIO;
// import net.sf.javaml.clustering.FuzzyCMeansClusterer;
// import net.sf.javaml.core.Dataset;
// import net.sf.javaml.core.DefaultDataset;
// import net.sf.javaml.core.Instance;
// import net.sf.javaml.distance.EuclideanDistance;

// public class fuzzyCluster {
// public static void main(String args[]) throws IOException {
// BufferedImage image = null;
// try {
// image = ImageIO.read(new File("murgi.jpg"));
// } catch (Exception e) {
// System.out.println("during Image input problem");
// }

// int height = image.getHeight();
// int width = image.getWidth();
// double[][] pixels = new double[height * width][3];
// for (int i = 0; i < height; i++) {
// for (int j = 0; j < width; j++) {
// Color color = new Color(image.getRGB(j, i));
// pixels[i * width + j][0] = color.getRed();
// pixels[i * width + j][1] = color.getGreen();
// pixels[i * width + j][2] = color.getBlue();
// }
// }

// int k = 3; // number of clusters
// double m = 2; // fuzziness parameter
// int maxIterations = 100; // maximum number of iterations
// double[][] centers = new double[k][3];
// FuzzyCMeansClusterer clusterer;
// try {
// clusterer = new FuzzyCMeansClusterer(k, m, maxIterations);
// } catch (Exception e) {
// System.out.println("hi");
// }
// Cluster[] clusters = clusterer.cluster(pixels, centers);
// BufferedImage image = null;
// try {
// image = ImageIO.read(new File("input.jpg"));
// } catch (IOException e) {
// e.printStackTrace();
// System.exit(1);
// }

// // Create a dataset with the pixel values of the image
// Dataset dataset = new DefaultDataset();
// for (int y = 0; y < image.getHeight(); y++) {
// for (int x = 0; x < image.getWidth(); x++) {
// Color c = new Color(image.getRGB(x, y));
// Instance pixel = new Instance(new double[] { c.getRed(), c.getGreen(),
// c.getBlue() });
// dataset.add(pixel);
// }
// }

// // Cluster the pixel values using Fuzzy C-Means clustering
// int numClusters = 3;
// FuzzyCMeansClusterer clusterer = new FuzzyCMeansClusterer(numClusters);
// clusterer.setDistanceFunction(new EuclideanDistance());
// Dataset[] clusters = clusterer.cluster(dataset);

// // Generate a new image with the cluster colors
// BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(),
// BufferedImage.TYPE_INT_RGB);
// int i = 0;
// for (Dataset cluster : clusters) {
// Color c = new Color((int) cluster.getMeanInstance().value(0), (int)
// cluster.getMeanInstance().value(1),
// (int) cluster.getMeanInstance().value(2));
// for (Instance pixel : cluster) {
// int x = i % image.getWidth();
// int y = i / image.getWidth();
// output.setRGB(x, y, c.getRGB());
// i++;
// }
// }

// // Save the output image to a file
// try {
// ImageIO.write(output, "jpg", new File("output.jpg"));
// } catch (IOException e) {
// e.printStackTrace();
// System.exit(1);
// }

// }

// }
