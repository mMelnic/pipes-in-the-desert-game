package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import components.Pipe;
import enumerations.Shapes;

public class PipeView extends JPanel {
    private Map<Shapes, BufferedImage> pipeImagesNormal;
    private Map<Shapes, BufferedImage> pipeImagesLeaking;
    private Map<Shapes, BufferedImage> pipeImagesBroken;
    private Map<Shapes, BufferedImage> pipeImagesWaterFlowing;
    private Map<Shapes, BufferedImage> pipeImagesFull;

    private Pipe pipeModel;

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

    private void loadImages() {
        // Load images for each state and shape
        try {
            for (Shapes shape : Shapes.values()) {
                if (shape == Shapes.HORIZONTAL) {

                    pipeImagesNormal.put(shape,
                            loadImage("/resources/pipeImages/pipeView_" + shape.name().toLowerCase() + ".png"));
                
                pipeImagesLeaking.put(shape,
                loadImage("/resources/pipeImages/pipeView_" + shape.name().toLowerCase() +
                "_leaking.png"));
                pipeImagesBroken.put(shape,
                loadImage("/resources/pipeImages/pipeView_" + shape.name().toLowerCase() +
                "_red.png"));
                }
                // pipeImagesWaterFlowing.put(shape,
                // loadImage("/resources/pipeImages/pipe_" + shape.name().toLowerCase() +
                // "_water_flowing.png"));
                // pipeImagesFull.put(shape,
                // loadImage("/resources/pipeImages/pipe_" + shape.name().toLowerCase() +
                // "_full.png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(getClass().getResource(path));
    }

    public void updateComponentState() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderPipe(g);
    }

    private void renderPipe(Graphics g) {
        Shapes shape = pipeModel.getShape();
        BufferedImage imageToDraw = pipeImagesNormal.get(shape);

        if (pipeModel.isLeaking() || pipeModel.isFreeEndLeaking()) {
        imageToDraw = pipeImagesLeaking.get(shape);
        }
        else if (pipeModel.isBroken()) {
        imageToDraw = pipeImagesBroken.get(shape);
        }
        // else if (pipeModel.isFull()) {
        // imageToDraw = pipeImagesFull.get(shape);
        // } else if (pipeModel.isWaterFlowing()) {
        // imageToDraw = pipeImagesWaterFlowing.get(shape);
        // }

        if (imageToDraw != null) {
            int imageWidth = imageToDraw.getWidth();
            int imageHeight = imageToDraw.getHeight();
            int x = (getWidth() - imageWidth) / 2;
            int y = (getHeight() - imageHeight) / 2;
            g.drawImage(imageToDraw, x, y, this);
        }
    }
}