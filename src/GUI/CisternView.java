package GUI;
import components.Cistern;
import components.Pipe;
import components.Pump;
import system.CisternImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * The {@code CisternView} class represents a graphical view of a cistern component in the game.
 * It extends {@code JPanel} and handles the rendering of the cistern's different states and components.
 */
public class CisternView extends JPanel {
    private Cistern cistern;

    /**
     * Constructs a new {@code CisternView} with the specified cistern.
     *
     * @param cistern the cistern to be displayed in this view
     */
    public CisternView(Cistern cistern) {
        this.cistern = cistern;
        
        setPreferredSize(new Dimension(80, 80));
        setBackground(new Color(0, 0, 0, 0));
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
        BufferedImage imageToDraw = CisternImageLoader.getCisternWithoutComponentImage();
        BufferedImage fillStateImageToDraw = null;
        
        if (cistern.getManufacturedComponent() instanceof Pipe) {
            imageToDraw = CisternImageLoader.getCisternWithPipeImage();
        } else if (cistern.getManufacturedComponent() instanceof Pump) {
            imageToDraw = CisternImageLoader.getCisternWithPumpImage();
        }

        if (cistern.getIsCisternFilling()) {
            fillStateImageToDraw = CisternImageLoader.getCisternFillingImage();
        } else if (cistern.getIsCisternFull()) {
            fillStateImageToDraw = CisternImageLoader.getCisternFullImage();
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