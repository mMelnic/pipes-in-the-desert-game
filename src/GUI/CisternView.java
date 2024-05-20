package GUI;
import components.Cistern;
import components.Pipe;
import components.Pump;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * The {@code CisternView} class represents a graphical view of a cistern component in the game.
 * It extends {@code JPanel} and handles the rendering of the cistern's different states and components.
 */
public class CisternView extends JPanel {
    private Cistern cistern;
    private BufferedImage cisternWithoutComponentImage;
    private BufferedImage cisternWithPipeImage;
    private BufferedImage cisternWithPumpImage;
    private BufferedImage cisternFillingImage;
    private BufferedImage cisternFullImage;

    /**
     * Constructs a new {@code CisternView} with the specified cistern.
     *
     * @param cistern the cistern to be displayed in this view
     */
    public CisternView(Cistern cistern) {
        this.cistern = cistern;
        
        setPreferredSize(new Dimension(80, 80));
        setBackground(new Color(0, 0, 0, 0));
        loadImages();
    }

    /**
     * Loads the images required to render the cistern in different states and with different components.
     */
    private void loadImages() {
        try {
            cisternWithoutComponentImage = ImageIO.read(getClass().getResource("/resources/images/cisternView.png"));
            cisternWithPipeImage = ImageIO.read(getClass().getResource("/resources/images/cisternView_manufacturedPipe.png"));
            cisternWithPumpImage = ImageIO.read(getClass().getResource("/resources/images/cisternView_manufacturedPump.png"));
            cisternFillingImage = ImageIO.read(getClass().getResource("/resources/images/cisternView_filling.png"));
            cisternFullImage = ImageIO.read(getClass().getResource("/resources/images/cisternView_full.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Overrides the {@code paintComponent} method to render the cistern.
     *
     * @param g the {@code Graphics} context in which to paint
     */
    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        renderCistern(g);
    }

    /**
     * Renders the cistern based on its current state and the components it contains.
     *
     * @param g the {@code Graphics} context in which to paint
     */
    private void renderCistern(Graphics g) {
        BufferedImage imageToDraw = cisternWithoutComponentImage;
        BufferedImage fillStateImageToDraw = null;
        if (cistern.getManufacturedComponent() instanceof Pipe) {
            imageToDraw = cisternWithPipeImage;
        } else if (cistern.getManufacturedComponent() instanceof Pump) {
            imageToDraw = cisternWithPumpImage;
        }

        if (cistern.getIsCisternFilling()) {
            fillStateImageToDraw = cisternFillingImage;
        }
        else if (cistern.getIsCisternFull()) {
            fillStateImageToDraw = cisternFullImage;
        }

        if (imageToDraw != null) {
            int imageWidth = imageToDraw.getWidth();
            int imageHeight = imageToDraw.getHeight();
            int x = (getWidth() - imageWidth) / 2;
            int y = (getHeight() - imageHeight) / 2;
            g.drawImage(imageToDraw, x, y, this);
            if (fillStateImageToDraw != null) {
                g.drawImage(fillStateImageToDraw, x, y, this);
            }
        }
    }
}