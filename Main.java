import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String avg[]) throws IOException {
        if (avg.length != 3) {
            System.err.println("You are missing an attribute. Usage: java Main <image.jpg> <square size> <processing mode(S/M)>"); // in case attribute is missing
            System.exit(1); // Exit the program with an error code
        }

        Main mn = new Main(avg);
    }

    public Main(String avg[]) throws IOException {
        BufferedImage img = ImageIO.read(new File(System.getProperty("user.dir") + "/" + avg[0]));
        int pixelSize = Integer.parseInt(avg[1]); // assign input to variables
        String mode = avg[2];

        //  determine the dimensions of the displayed image based on the minimum of the image dimensions and the screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int preferredWidth = Math.min(img.getWidth(), screenSize.width);
        int preferredHeight = Math.min(img.getHeight(), screenSize.height);

        //  the aspect ratio of the image is calculated, and other dimensions are calculated to maintain the original image's proportions.
        double aspectRatio = (double) img.getWidth() / img.getHeight();
        int displayWidth = preferredWidth;
        int displayHeight = (int) (displayWidth / aspectRatio);


        if (displayHeight > screenSize.height) {
            displayHeight = screenSize.height; // adjust displayHeight to be equal to the screen height
            displayWidth = (int) (displayHeight * aspectRatio); // recalculates displayWidth to maintain the original image's aspect ratio
        }

        img = resizeImage(img, displayWidth, displayHeight);  // resize the image

        ImageIcon icon = new ImageIcon(img); // create a JFrame to display the image
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(img.getWidth(), img.getHeight()); // set the size of the frame to match the dimensions of the resized image.
        JLabel imageLabel = new JLabel(); // create a JLabel to hold the image.
        imageLabel.setIcon(icon);
        frame.add(imageLabel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Raster src = img.getData(); // obtains the raster data from the resized image.
        Runtime runtime = Runtime.getRuntime(); // obtain the number of available processors
        int n = runtime.availableProcessors();

        WritableRaster dest = src.createCompatibleWritableRaster();
        img.copyData(dest); // copies data from source raster to destination raster

        switch (mode) {
            case "M":
                processMultiThread(img, pixelSize, imageLabel, n, src, dest); // start multi threading
                break;
            case "S":
                new ImageProcessor(img, pixelSize, imageLabel, 0, img.getHeight(), src, dest).run(); // we pass just one image processor from start to finish in single thread
                break;
            default:
                System.err.println("Invalid mode. Please use 'S' for single-threaded or 'M' for multi-threaded."); // return a message if the input isn't 'S' or 'M'
                break;
        }
    }

    private void processMultiThread(BufferedImage img, int pixelSize, JLabel imageLabel, int n, Raster src, WritableRaster dest) {
        for (int i = 0; i < n; i++) { // for loop to start processing
            int height = img.getHeight();
            int startY = i * (height / n); // calculate starting row for each thread
            int endY = i * (height / n) + height / n; // calculate ending row for each thread
            Thread thread = new Thread(new ImageProcessor(img, pixelSize, imageLabel, startY, endY, src, dest)); //start ImageProcessor for every thread, with rows and destination stated
            thread.start(); // created thread is started
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) { // resize the image to the specified width and height
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resizedImage.createGraphics();
        graphics.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics.dispose();
        return resizedImage;
    }
}