package GUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

import components.Pipe;
import components.Pump;
import enumerations.Direction;

public class PumpView extends JPanel {
    private Pump pump;
    private BufferedImage imageNormal;
    private BufferedImage imageLeaking;
    private BufferedImage imageBroken;
    private BufferedImage imageReservoirFull;
    private BufferedImage imageReservoirFilling;

    public PumpView(Pump pump) {
        this.pump = pump;
        // Initialize images for different states
        initializeImages();
        setPreferredSize(new Dimension(80, 80));
        setBackground(new Color(0, 0, 0, 0));
    }

    private void initializeImages() {
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

    private BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(getClass().getResource(path));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Get the current state of the pump
        int openings = pump.getConnectablePipesNumber();

        // Determine the image based on the pump's state
        BufferedImage imageToDraw = null;

        if (pump.isBroken()) {
            imageToDraw = imageBroken;
        } else if (pump.isLeaking()) {
            imageToDraw = imageLeaking;
        } else if (pump.isReservoirFull()) {
            imageToDraw = imageReservoirFull;
        } else if (pump.isFilling()) { // Assuming you have a method to check if the pump is filling
            imageToDraw = imageReservoirFilling;
        } else {
            imageToDraw = imageNormal;
        }

        // Draw the pump image
        if (imageToDraw != null) {
            int x = (getWidth() - imageToDraw.getWidth()) / 2;
            int y = (getHeight() - imageToDraw.getHeight()) / 2;
            g.drawImage(imageToDraw, x, y, this);
        }

        // Draw the number of openings
        String openingsText = String.valueOf(openings);
        g.setFont(new Font("Arial", Font.BOLD, 35)); // Set a custom font, bold style, and size 24
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(openingsText);
        int textHeight = fm.getHeight();
        int textX = (getWidth() - textWidth) / 2;
        int textY = (getHeight() - textHeight) / 2 + fm.getAscent() + 17;
        g.setColor(Color.RED); // Set the color to red
        g.drawString(openingsText, textX, textY);

        // Draw connected pipes
        drawConnectedPipes(g);
    }

    private void drawConnectedPipes(Graphics g) {
        int margin = 0; // Margin from the edge of the panel
        int fontSize = 25; // Font size for drawing letters
        Font font = new Font("Arial", Font.BOLD, fontSize);
        g.setFont(font);

        for (Direction direction : pump.getConnectedComponents().keySet()) {
            int x, y;

            // Calculate the position based on the direction
            switch (direction) {
                case UP:
                    x = (getWidth() - fontSize) / 2;
                    y = margin;
                    break;
                case DOWN:
                    x = (getWidth() - fontSize) / 2;
                    y = getHeight() - margin - fontSize;
                    break;
                case LEFT:
                    x = margin;
                    y = (getHeight() - fontSize) / 2 + fontSize + 10;
                    break;
                case RIGHT:
                    x = getWidth() - margin - fontSize + 10;
                    y = (getHeight() - fontSize) / 2 + fontSize + 10;
                    break;
                default:
                    continue; // Skip if direction is invalid
            }

            components.Component componentToCheck = pump.getConnectedComponents().get(direction);
            // Draw the letter "I" or "O" based on the direction
            if (componentToCheck instanceof Pipe) {
                Pipe pipe = (Pipe)componentToCheck;
                String letter = (pipe == pump.getIncomingPipe()) ? "P" : "P";
                g.drawString(letter, x, y);
            }
        }
    }
}