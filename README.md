# Assignment 3
### How to run?
1. Download both files (Main.java and ImageProcessor.java) and save them in the same directory. There is no need to download any external libraries
2. Save the images that you are planning to process in the same directory with both java files. The program can process .jpg and .png images.
3. Open a Terminal and navigate to the directory location using: <br />
```cd path/to/JavaProject```
4. Compile the code using: <br />
```javac Main.java ImageProcessor.java``` <br />
or <br />
```javac Main.java```
6. Run the program using: <br />
```java Main someImage.jpg squareSize processingMode(S/M)``` where **someImage.jpg** is the name of the image, **squareSize** is desired pixel number, and **processingMode** is desired thread mode which can be either **S** which stands for single-thread mode or **M** which stands for multi-thread mode.<br />
Example: <br />
```java Main batman.jpg 10 M```
7. If your inputs are right a window with image that is being gradually processed will open. You can close it at any point, but if you do it before image is
done processng the result will not be saved. After it is don processing the image will be saved as soon as you close the window.
8. After you close the window new **result.jpg**, containing processed image, will appear in the same directory. If you run the code with **result.jpg** already
existing in the directory, the new processed image will replace the existing image, and the old one will be gone.

### Purpose
This programis an image processing tool designed to pixelate **JPG or PNG images**. This command-line application takes three  inputs – **file name, square size, and processing mode ('S' for single-threaded, 'M' for multi-threaded)** – giving you control over the transformation process. As you run the program, it visually displays the step-by-step improvement of your image. The technique involves finding the average color for squares of a specified size, gradually updating the entire square. In multi-threaded mode, the program speeds up the process by intelligently using your computer's CPU cores. The final result is  saved as **'result.jpg'** and contains the transformed image.

### About the code
My solution for the assignment consists of two classes, Main and ImageProcessor. In the Main class, a custom JPanel named imagePanel is created to display the processed image. This JPanel dynamically adjusts the display of images, ensuring that they are presented without distortion, regardless of their size. The paintComponent method within this JPanel is responsible for rendering the image, taking into account the dimensions of both the image and the panel.

The Main class facilitates user interaction by employing the Swing library, allowing users to input an image file, pixel size, and processing mode (single-threaded or multi-threaded). Following input validation and image file verification, the program initializes the graphical interface, displaying the image in the imagePanel within a frame. The pixelation process occurs either in a single thread or multiple threads, depending on the user's selection.

The ImageProcessor class, implementing the Runnable interface, manages the pixelation logic. It calculates the average color of pixel blocks and progressively updates the image within the imagePanel, introducing a visual delay for effect. The JPanel effectively handles the rendering of images, accommodating large images without changing their size. The final pixelated image is saved to a file named "result.jpg." Overall, the code leverages Java's ImageIO, Swing, and JPanel to create an interactive environment for image pixelation, ensuring proper display and processing of images of varying sizes.






   
