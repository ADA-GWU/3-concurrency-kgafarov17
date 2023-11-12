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
 
   
