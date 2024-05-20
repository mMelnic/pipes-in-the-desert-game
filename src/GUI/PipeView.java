package GUI;

import components.Pipe;
import enumerations.Shapes;
import system.ImageLoader;

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

    private Pipe pipeModel;

    /**
     * Constructs a new {@code PipeView} with the specified pipe model.
     *
     * @param pipeModel the pipe model to be represented by this view
     */
    public PipeView(Pipe pipeModel) {
        this.pipeModel = pipeModel;
        setPreferredSize(new Dimension(80, 80));
        setBackground(new Color(0, 0, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderPipe(g);
    }

    /**
     * Renders the pipe based on its current state.
     *
     * @param g the Graphics object used for drawing
     */
    private void renderPipe(Graphics g) {
        Shapes shape = pipeModel.getShape();
        BufferedImage imageToDraw = ImageLoader.getPipeImageNormal(shape);

        if (pipeModel.isLeaking()) {
            imageToDraw = ImageLoader.getPipeImageLeaking(shape);
        } else if (pipeModel.isFreeEndLeaking()) {
            imageToDraw = ImageLoader.getPipeImageLeaking(shape);
        } else if (pipeModel.isBroken()) {
            imageToDraw = ImageLoader.getPipeImageBroken(shape);
        } else if (pipeModel.isFull()) {
            imageToDraw = ImageLoader.getPipeImageFull(shape);
        } else if (pipeModel.isWaterFlowing()) {
            imageToDraw = ImageLoader.getPipeImageWaterFlowing(shape);
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