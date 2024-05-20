package GUI;
import components.Pipe;
import components.Pump;
import enumerations.Direction;
import system.PumpImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PumpView extends JPanel {
    private Pump pump;

    public PumpView(Pump pump) {
        this.pump = pump;
        // Initialize images for different states
        setPreferredSize(new Dimension(80, 80));
        setBackground(new Color(0, 0, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        // Get the current state of the pump
        int openings = pump.getConnectablePipesNumber();

        // Determine the image based on the pump's state
        BufferedImage imageToDraw = PumpImageLoader.getImageNormal();

        if (pump.isLeaking()) {
            imageToDraw = PumpImageLoader.getImageLeaking();
        } else if (pump.isReservoirFull()) {
            imageToDraw = PumpImageLoader.getImageReservoirFull();
        } else if (pump.isFilling()) {
            imageToDraw = PumpImageLoader.getImageReservoirFilling();
        } else if (pump.isBroken()) {
            imageToDraw = PumpImageLoader.getImageBroken();
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
        int textY = (getHeight() - textHeight) / 2 + fm.getAscent() + 10;
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
                case UP -> {
                    x = (getWidth() - fontSize + 12) / 2;
                    y = margin + fontSize;
                }
                case DOWN -> {
                    x = (getWidth() - fontSize + 12) / 2;
                    y = getHeight() - margin - fontSize + 25;
                }
                case LEFT -> {
                    x = margin;
                    y = (getHeight() - fontSize) / 2 + fontSize + 10;
                }
                case RIGHT -> {
                    x = getWidth() - margin - fontSize + 10;
                    y = (getHeight() - fontSize) / 2 + fontSize + 10;
                }
                default -> {
                    continue; // Skip if direction is invalid
                }
            }

            components.Component componentToCheck = pump.getConnectedComponents().get(direction);
            // Draw the letter "I" or "O" based on the direction
            if (componentToCheck instanceof Pipe) {
                Pipe pipe = (Pipe)componentToCheck;
                String letter = (pipe == pump.getIncomingPipe() || pipe == pump.getOutgoingPipe()) ? "P" : "";
                g.drawString(letter, x, y);
            }
        }
    }
}