package system;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import enumerations.Direction;

/**
 * The PlumberImageLoader class manages loading images for plumbers.
 */
public class PlumberImageLoader {
    private static BufferedImage[][] images; // 2D array to store images for different colors and directions
    private static BufferedImage plumberPickPipeImage;
    private static BufferedImage plumberPickPumpImage;

    // Private constructor to prevent instantiation
    private PlumberImageLoader() {
    }

    static {
        // Initialize the images array
        images = new BufferedImage[2][4]; // 2 colors (green and red), 4 directions
        loadImages();
        loadAdditionalImages();
    }

    // Method to load plumber images for different colors and directions
    private static void loadImages() {
        try {
            // Load images for green color
            images[0][0] = loadImage("/resources/plumber/plumber_green_down.png");
            images[0][1] = loadImage("/resources/plumber/plumber_green_up.png");
            images[0][2] = loadImage("/resources/plumber/plumber_green_left.png");
            images[0][3] = loadImage("/resources/plumber/plumber_green_right.png");

            // Load images for red color
            images[1][0] = loadImage("/resources/plumber/plumber_red_down.png");
            images[1][1] = loadImage("/resources/plumber/plumber_red_up.png");
            images[1][2] = loadImage("/resources/plumber/plumber_red_left.png");
            images[1][3] = loadImage("/resources/plumber/plumber_red_right.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Method to load additional images
    private static void loadAdditionalImages() {
        try {
            plumberPickPipeImage = loadImage("/resources/plumber/plumberView_pickPipe.png");
            plumberPickPumpImage = loadImage("/resources/plumber/plumberView_pickPump.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load a single image given its path
    private static BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(ImageLoader.class.getResource(path));
    }

    // Method to get the plumber image based on color and direction
    public static BufferedImage getImage(String color, Direction direction) {
        int colorIndex = color.equalsIgnoreCase("green") ? 0 : 1;
        int directionIndex = direction.ordinal();
        return images[colorIndex][directionIndex];
    }

    // Methods to get additional images for when the plumber is picking up a pipe or pump
    public static BufferedImage getPlumberPickPipeImage() {
        return plumberPickPipeImage;
    }

    public static BufferedImage getPlumberPickPumpImage() {
        return plumberPickPumpImage;
    }
}
