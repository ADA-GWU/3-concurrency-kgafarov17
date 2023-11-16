import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import java.io.File;

class ImageProcessor implements Runnable {

    private static final int SLEEP_DURATION = 20;
    private BufferedImage img; // original image that is going to be pixelated
    private int pixelSize; // the size of the pixel blocks
    private Consumer<BufferedImage> imageUpdateCallback; // callback to update the image
    private int startY; // starting row
    private int endY; //ending row
    private Raster src; // allows access to pixel data in the original image for processing.
    private WritableRaster dest; // used to store the pixel data of the modified (pixelated) image.

    public ImageProcessor(BufferedImage img, int pixelSize, Consumer<BufferedImage> imageUpdateCallback, int startY, int endY, Raster src, WritableRaster dest) {
        this.img = img;
        this.pixelSize = pixelSize;
        this.imageUpdateCallback = imageUpdateCallback;
        this.startY = startY;
        this.endY = endY;
        this.src = src;
        this.dest = dest;
    }

    @Override
    public void run() {
        for (int y = startY; y < endY; y += pixelSize) { // iterates through rows based on pixelSize
            for (int x = 0; x < src.getWidth(); x += pixelSize) { // iterates through column
                double[] sum = getAverageColor(y, x); // calculate average colour

                for (int yPixel = y; (yPixel < y + pixelSize) && (yPixel < dest.getHeight()); yPixel++) { // 2 loops to colour all images in the square with the average colour
                    for (int xPixel = x; (xPixel < x + pixelSize) && (xPixel < dest.getWidth()); xPixel++) {
                        dest.setPixel(xPixel, yPixel, sum);
                    }
                }

                try {
                    Thread.sleep(SLEEP_DURATION); // delay for visual effect
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                img.setData(dest); // update the image data with the modified pixel values
                SwingUtilities.invokeLater(() -> imageUpdateCallback.accept(img)); // invokeLater to update the UI
            }
        }

        try {
            saveImage("result.jpg"); //save pixelated image to result.jpg
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double[] getAverageColor(int y, int x) {
        double[] temp = new double[3]; // temp value for each pixel RGB
        double[] averageColour = new double[3]; // average colour of the area
        int cnt = 0;

        for(int yPixel = y; (yPixel < y + pixelSize) && (yPixel < dest.getHeight()); yPixel++) {
            for(int xPixel = x; (xPixel < x + pixelSize) && (xPixel < dest.getWidth()); xPixel++) {
                src.getPixel(xPixel, yPixel, temp);
                averageColour[0] += temp[0];
                averageColour[1] += temp[1];
                averageColour[2] += temp[2];
                cnt++;
            }
        }

        for (int i = 0; i < 3; i++) {
            averageColour[i] /= cnt;
        }

        return averageColour;
    }

    private void saveImage(String outputPath) throws IOException {
        File outputFile = new File(outputPath);
        ImageIO.write(img, "jpg", outputFile);
    }
}
