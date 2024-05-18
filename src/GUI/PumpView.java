package GUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

import components.Pump;

public class PumpView extends JPanel {
    private Pump pump;
    private Map<Integer, BufferedImage> imagesNormal;
    private Map<Integer, BufferedImage> imagesLeaking;
    private Map<Integer, BufferedImage> imagesBroken;
    private Map<Integer, BufferedImage> imagesReservoirFull;
    private Map<Integer, BufferedImage> imagesReservoirFilling;

    public PumpView(Pump pump) {
        this.pump = pump;
        // Initialize images for different states
        initializeImages();
        setPreferredSize(new Dimension(80, 80));
        setBackground(new Color(0, 0, 0, 0));
    }

    private void initializeImages() {
        // Load images for each state and number of connectable pipes
        try {
            for (int i = 1; i <= 4; i++) { // Assuming a pump can have 1 to 4 connectable pipes
                imagesNormal.put(i, loadImage("/resources/pumpImages/pump_" + i + "_normal.png"));
                imagesLeaking.put(i, loadImage("/resources/pumpImages/pump_" + i + "_leaking.png"));
                imagesBroken.put(i, loadImage("/resources/pumpImages/pump_" + i + "_broken.png"));
                imagesReservoirFull.put(i, loadImage("/resources/pumpImages/pump_" + i + "_reservoir_full.png"));
                imagesReservoirFilling.put(i, loadImage("/resources/pumpImages/pump_" + i + "_reservoir_filling.png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(getClass().getResource(path));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Get the current state of the pump
        int openings = pump.getConnectablePipesNumber();

        // Determine the image based on the pump's state
        BufferedImage pumpImage = null;
        if (pump.isLeaking()) {
            pumpImage = imagesLeaking.get(openings);
        } else if (pump.isReservoirFull()) {
            pumpImage = imagesReservoirFull.get(openings);
        } else if (pump.isBroken()) {
            pumpImage = imagesBroken.get(openings);
        } else if (pump.isFilling()) { 
            pumpImage = imagesReservoirFilling.get(openings);
        } else {
            pumpImage = imagesNormal.get(openings);
        }

        // Draw the pump image
        if (pumpImage != null) {
            int imageWidth = pumpImage.getWidth();
            int imageHeight = pumpImage.getHeight();
            int x = (getWidth() - imageWidth) / 2;
            int y = (getHeight() - imageHeight) / 2;
            g.drawImage(pumpImage, x, y, this);
        }
    }
}