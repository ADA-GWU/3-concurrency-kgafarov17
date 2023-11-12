# Assignment 3
### How to run?
1. Download both files (Main.java and ImageProcessor.java) and save them in the same directory.
2. Save the images that you are planning to process in the same directory with both java files. The program can process .jpg and .png images.
3. Open a Terminal and navigate to the directory location using: <br />
**cd path/to/JavaProject**
4. Compile the code using: <br />
**javac Main.java ImageProcessor.java** or **javac Main.java**
5. Run the program usingL <br />
**java Main someImage.jpg squareSize processingMode(S/M)** <br />
where **someImage.jpg** is the name of the image, **squareSize** is desired pixel number, and **processingMode** is desired thread mode which can be either
**S** which stands for single-thread mode or **M** which stands for multi-thread mode. <br/ >
6. If your inputs are right a window with image that is being gradually processed will open. You can close it at any point, but if you do it before image is
done processng the result will not be saved. After it is don processing the image will be saved as soon as you close the window.
7. After you close the window new **result.jpg**, containing processed image, will appear in the same directory. If you run the code with **result.jpg** already
existing in the directory, the new processed image will replace the existing image, and the old one will be gone.


 
   
