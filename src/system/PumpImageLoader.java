package system;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The PumpImageLoader class manages loading images for pump components.
 */
public class PumpImageLoader {
    // Image variables for different states of the pump
    private static BufferedImage imageNormal;
    private static BufferedImage imageLeaking;
    private static BufferedImage imageBroken;
    private static BufferedImage imageReservoirFull;
    private static BufferedImage imageReservoirFilling;

    // Static initialization block to load images
    static {
        loadImages();
    }
    // Private constructor to prevent instantiation
    private PumpImageLoader() {}

     // Method to load images for different states of the pump
    private static void loadImages() {
        try {
            imageNormal = loadImage("/resources/pumpImages/pump_normal.png");
            imageLeaking = loadImage("/resources/pumpImages/pump_leaking.png");
            imageBroken = loadImage("/resources/pumpImages/pump_broken.png");
            imageReservoirFull = loadImage("/resources/pumpImages/pump_reservoir_full.png");
            imageReservoirFilling = loadImage("/resources/pumpImages/pump_reservoir_filling.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load a single image given its path
    private static BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(PumpImageLoader.class.getResource(path));
    }

    // Methods to get images for different states of the pump
    public static BufferedImage getImageNormal() {
        return imageNormal;
    }

    public static BufferedImage getImageLeaking() {
        return imageLeaking;
    }

    public static BufferedImage getImageBroken() {
        return imageBroken;
    }

    public static BufferedImage getImageReservoirFull() {
        return imageReservoirFull;
    }

    public static BufferedImage getImageReservoirFilling() {
        return imageReservoirFilling;
    }
}
