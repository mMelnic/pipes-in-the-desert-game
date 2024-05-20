package GUI;

import components.Pipe;
import enumerations.Shapes;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * The {@code PipeView} class represents the graphical view of a pipe component.
 * It displays the pipe in different states (normal, leaking, broken, water flowing)
 * based on the current state of the pipe model.
 */
public class PipeView extends JPanel {
    private Map<Shapes, BufferedImage> pipeImagesNormal;
    private Map<Shapes, BufferedImage> pipeImagesLeaking;
    private Map<Shapes, BufferedImage> pipeImagesBroken;
    private Map<Shapes, BufferedImage> pipeImagesWaterFlowing;
    private Map<Shapes, BufferedImage> pipeImagesFull;

    private Pipe pipeModel;

    /**
     * Constructs a new {@code PipeView} with the specified pipe model.
     *
     * @param pipeModel the pipe model to be represented by this view
     */
    public PipeView(Pipe pipeModel) {
        this.pipeModel = pipeModel;
        pipeImagesNormal = new HashMap<>();
        pipeImagesLeaking = new HashMap<>();
        pipeImagesBroken = new HashMap<>();
        pipeImagesWaterFlowing = new HashMap<>();
        pipeImagesFull = new HashMap<>();
        setPreferredSize(new Dimension(80, 80));
        setBackground(new Color(0, 0, 0, 0));
        loadImages();
    }

    /**
     * Loads the images for each state and shape of the pipe.
     */
    private void loadImages() {
        // Load images for each state and shape
        try {
            for (Shapes shape : Shapes.values()) {


                pipeImagesNormal.put(shape,
                            loadImage("/resources/pipeImages/pipeView_" + shape.name().toLowerCase() + ".png"));
                pipeImagesLeaking.put(shape, loadImage("/resources/pipeImages/pipeView_" + shape.name().toLowerCase() + "_leaking.png"));
                pipeImagesBroken.put(shape, loadImage("/resources/pipeImages/pipeView_" + shape.name().toLowerCase() + "_red.png"));
                pipeImagesWaterFlowing.put(shape, loadImage("/resources/pipeImages/pipeView_" + shape.name().toLowerCase() + "_filled.png"));
                pipeImagesFull.put(shape, loadImage("/resources/pipeImages/pipe_" + shape.name().toLowerCase() +"_full.png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads an image from the specified path.
     *
     * @param path the path to the image resource
     * @return the loaded BufferedImage
     * @throws IOException if an error occurs during reading the image
     */
    private BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(getClass().getResource(path));
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        renderPipe(g);
    }

    /**
     * Renders the pipe based on its current state.
     *
     * @param g the Graphics object used for drawing
     */
    private void renderPipe(Graphics g) {
        Shapes shape = pipeModel.getShape();
        BufferedImage imageToDraw = pipeImagesNormal.get(shape);

        if (pipeModel.isLeaking()) {
            imageToDraw = pipeImagesLeaking.get(shape);
        } else if (pipeModel.isFreeEndLeaking()) {
            imageToDraw = pipeImagesLeaking.get(shape);
        } else if (pipeModel.isBroken()) {
            imageToDraw = pipeImagesBroken.get(shape);
        } else if (pipeModel.isFull()) {
        imageToDraw = pipeImagesFull.get(shape);
        }
        else if (pipeModel.isWaterFlowing()) {
            imageToDraw = pipeImagesWaterFlowing.get(shape);
        }

        if (imageToDraw != null) {
            int imageWidth = imageToDraw.getWidth();
            int imageHeight = imageToDraw.getHeight();
            int x = (getWidth() - imageWidth) / 2;
            int y = (getHeight() - imageHeight) / 2;
            g.drawImage(imageToDraw, x, y, this);
        }
    }
}