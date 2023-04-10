import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageC {
    public static void main(String args[]) throws IOException {
        File file = null;
        BufferedImage image = null;
        try {
            file = new File("b.bmp");
            image = ImageIO.read(file);
        } catch (Exception e) {
            System.out.println("Error");
        }

        int width = image.getWidth();
        int height = image.getHeight();
        System.out.println(width + "*" + height);

        // image pixels

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = image.getRGB(x, y);
                // System.out.print(" " + p + " ");
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = (p & 0xff);

                int avg = (int) Math.sqrt(r * r + g * g + b * b);
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;

                image.setRGB(x, y, p);
            }
            // System.out.println();
        }
        try {
            file = new File("grayS.jpg");
            ImageIO.write(image, "jpg", file);
        } catch (Exception e) {
            System.out.print("Error Occoured");
        }
    }
}
