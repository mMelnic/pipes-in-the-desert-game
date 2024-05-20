package GUI;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JButton;

/**
 * The {@code ImageButton} class represents a button that displays an image as its background.
 * It extends {@code JButton} and customizes the button appearance and behavior.
 */
public class ImageButton extends JButton {
private Image image;

    /**
     * Constructs a new {@code ImageButton} with the specified image.
     *
     * @param image the image to be displayed as the button's background
     */
    public ImageButton(Image image) {
        this.image = image;
        setContentAreaFilled(false); // Makes the button content area transparent
        setBorderPainted(false); // Do not paint the border
        setFocusPainted(false); // Do not paint the focus outline
    }

    /**
     * Overrides the {@code paintComponent} method to draw the image as the button's background.
     *
     * @param g the {@code Graphics} context in which to paint
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Draw the background image
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }
}
