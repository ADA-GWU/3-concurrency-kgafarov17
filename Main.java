import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class Main {
    static BufferedImage img;
    static JFrame frame;
    static int pixelSize;
    static String mode;

    public static void main(String args[]) throws IOException {
        if (args.length != 3) { // check if all attributes are provided
            System.err.println("You are missing an attribute. Usage: java Main <image.jpg> <square size> <processing mode(S/M)>");
            System.exit(1);
        }

        String imagePath = args[0]; // used to check if the file exists

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            System.err.println("Error: Image file not found at path: " + imagePath);
            System.exit(1);
        }
        else if (!imageFile.isFile()) {
            System.err.println("Error: The specified path does not point to a regular file: " + imagePath);
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            try {
                Main mn = new Main(args);
                mn.startProcessing(); // Start processing after frame is visible
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public Main(String args[]) throws IOException {
        img = ImageIO.read(new File(System.getProperty("user.dir") + "/" + args[0])); // assign input to variables
        pixelSize = Integer.parseInt(args[1]);
        mode = args[2];

        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int panelWidth = getWidth(); // get dimensions
                int panelHeight = getHeight();

                if (img != null) { // ensures that pixelating starts when image is on the display
                    int imageWidth = img.getWidth();
                    int imageHeight = img.getHeight();

                    if (imageWidth <= panelWidth && imageHeight <= panelHeight) { // if the image is smaller than the panel, draw without scaling
                        int x = (panelWidth - imageWidth) / 2;
                        int y = (panelHeight - imageHeight) / 2;
                        g.drawImage(img, x, y, imageWidth, imageHeight, this);
                    }
                    else { // calculate the scaled dimensions while maintaining the aspect ratio
                        int scaledWidth, scaledHeight;
                        double aspectRatio = (double) imageWidth / imageHeight;
                        if (panelWidth / aspectRatio <= panelHeight) {
                            scaledWidth = panelWidth;
                            scaledHeight = (int) (scaledWidth / aspectRatio);
                        }
                        else {
                            scaledHeight = panelHeight;
                            scaledWidth = (int) (scaledHeight * aspectRatio);
                        }

                        int x = (panelWidth - scaledWidth) / 2; // calculate the position to center the scaled image
                        int y = (panelHeight - scaledHeight) / 2;

                        g.drawImage(img, x, y, scaledWidth, scaledHeight, this); // draw the image on the panel using specified dimensions and position
                    }
                }
            }
        };

        imagePanel.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));

        frame = new JFrame();
        frame.setLayout(new BorderLayout()); // BorderLayout divides the container into five areas: north, south, east, west, and center.
        frame.add(imagePanel, BorderLayout.CENTER); // imagePanel is added to the center
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void startProcessing() {
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width; //used to check if the image is bigger or smaller than the screen
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        // check if the image is smaller than the screen size
        if (img.getWidth() <= screenWidth && img.getHeight() <= screenHeight) {
            frame.setSize(img.getWidth(), img.getHeight());
        } else {
            double aspectRatio = (double) img.getWidth() / img.getHeight();

            // calculate the maximum size while maintaining the aspect ratio
            int maxScreenWidth = Math.min(screenWidth, (int) (screenHeight * aspectRatio));
            int maxScreenHeight = Math.min(screenHeight, (int) (screenWidth / aspectRatio));

            frame.setSize(maxScreenWidth, maxScreenHeight); // set the frame size to the calculated maximum size
        }

        frame.setVisible(true); // Make the frame visible before processing

        Raster src = img.getData(); // used for pixelating
        Runtime runtime = Runtime.getRuntime();
        int n = runtime.availableProcessors();
        WritableRaster dest = src.createCompatibleWritableRaster();
        img.copyData(dest);

        switch (mode) {
            case "S": // start single threading, we pass just one image processor from start to finish in single thread
                processSingleThread(img, pixelSize, src, dest);
                break;
            case "M": // start multi thread
                processMultiThread(img, pixelSize, n, src, dest);
                break;
            default: // in case user writes something else in args
                System.err.println("Invalid mode. Please use 'S' for single-threaded or 'M' for multi-threaded.");
                System.exit(1);
        }
    }

    private void updateImage(BufferedImage updatedImage) { // update the image in the panel
        img = updatedImage;
        frame.repaint();
    }

    private void processSingleThread(BufferedImage img, int pixelSize, Raster src, WritableRaster dest) {
        int height = img.getHeight();
        int startY = 0; // sets the start and end row that needs to processed by single thread (which is from 1st to last row)
        int endY = height;

        Thread thread = new Thread(new ImageProcessor(img, pixelSize, this::updateImage, startY, endY, src, dest));
        thread.start();
    }

    private void processMultiThread(BufferedImage img, int pixelSize, int n, Raster src, WritableRaster dest) {
        for (int i = 0; i < n; i++) {
            int height = img.getHeight();
            int startY = i * (height / n); // formulas used to calculate start and end row for each thread
            int endY = (i + 1) * (height / n);
            Thread thread = new Thread(new ImageProcessor(img, pixelSize, this::updateImage, startY, endY, src, dest));
            thread.start();
        }
    }
}
