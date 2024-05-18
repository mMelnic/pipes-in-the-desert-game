package GUI;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import components.Cistern;
import components.Pipe;
import components.Pump;

public class CisternView extends JPanel {
    private Cistern cistern;
    private BufferedImage cisternWithoutComponentImage;
    private BufferedImage cisternWithPipeImage;
    private BufferedImage cisternWithPumpImage;
    private BufferedImage cisternFillingImage;
    private BufferedImage cisternFullImage;

    public CisternView(Cistern cistern) {
        this.cistern = cistern;
        
        setPreferredSize(new Dimension(80, 80));
        setBackground(new Color(0, 0, 0, 0));
        loadImages();
    }

    private void loadImages() {
        try {
            cisternWithoutComponentImage = ImageIO.read(getClass().getResource("/resources/images/cisternView.png"));
            cisternWithPipeImage = ImageIO.read(getClass().getResource("/resources/images/cisternView_manufacturedPipe.png"));
            cisternWithPumpImage = ImageIO.read(getClass().getResource("/resources/images/cisternView_manufacturedPump.png"));
            cisternFillingImage = ImageIO.read(getClass().getResource("/resources/images/cisternView_filling.png"));
            cisternFullImage = ImageIO.read(getClass().getResource("/resources/images/citernView_full.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderCistern(g);
    }

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