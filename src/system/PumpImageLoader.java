package system;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PumpImageLoader {
    private static BufferedImage imageNormal;
    private static BufferedImage imageLeaking;
    private static BufferedImage imageBroken;
    private static BufferedImage imageReservoirFull;
    private static BufferedImage imageReservoirFilling;

    static {
        loadImages();
    }
    private PumpImageLoader() {}

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

    private static BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(PumpImageLoader.class.getResource(path));
    }

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
