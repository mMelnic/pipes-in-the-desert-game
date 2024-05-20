package system;

import java.awt.image.BufferedImage;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import enumerations.Shapes;

/**
 * The ImageLoader class loads images for different shapes of pipes.
 */
public class ImageLoader {
    private static Map<Shapes, BufferedImage> pipeImagesNormal = new HashMap<>();
    private static Map<Shapes, BufferedImage> pipeImagesLeaking = new HashMap<>();
    private static Map<Shapes, BufferedImage> pipeImagesBroken = new HashMap<>();
    private static Map<Shapes, BufferedImage> pipeImagesWaterFlowing = new HashMap<>();
    private static Map<Shapes, BufferedImage> pipeImagesFull = new HashMap<>();

    // Static block to load images when the class is initialized
    static {
        loadImages();
    }

    // Private constructor to prevent instantiation
    private ImageLoader() {}

    // Method to load images for all pipe shapes and states
    private static void loadImages() {
        try {
            for (Shapes shape : Shapes.values()) {
                pipeImagesNormal.put(shape,
                        loadImage("/resources/pipeImages/pipeView_" + shape.name().toLowerCase() + ".png"));
                pipeImagesLeaking.put(shape,
                        loadImage("/resources/pipeImages/pipeView_" + shape.name().toLowerCase() + "_leaking.png"));
                pipeImagesBroken.put(shape,
                        loadImage("/resources/pipeImages/pipeView_" + shape.name().toLowerCase() + "_red.png"));
                pipeImagesWaterFlowing.put(shape,
                        loadImage("/resources/pipeImages/pipeView_" + shape.name().toLowerCase() + "_filled.png"));
                pipeImagesFull.put(shape,
                        loadImage("/resources/pipeImages/pipe_" + shape.name().toLowerCase() + "_full.png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load a single image given its path
    private static BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(ImageLoader.class.getResource(path));
    }

    // Methods to get specific pipe images based on shape and state
    public static BufferedImage getPipeImageNormal(Shapes shape) {
        return pipeImagesNormal.get(shape);
    }

    public static BufferedImage getPipeImageLeaking(Shapes shape) {
        return pipeImagesLeaking.get(shape);
    }

    public static BufferedImage getPipeImageBroken(Shapes shape) {
        return pipeImagesBroken.get(shape);
    }

    public static BufferedImage getPipeImageWaterFlowing(Shapes shape) {
        return pipeImagesWaterFlowing.get(shape);
    }

    public static BufferedImage getPipeImageFull(Shapes shape) {
        return pipeImagesFull.get(shape);
    }
}
