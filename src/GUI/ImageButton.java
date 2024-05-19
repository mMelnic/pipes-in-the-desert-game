package GUI;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JButton;


public class ImageButton extends JButton {
private Image image;

    public ImageButton(Image image) {
        this.image = image;
        setContentAreaFilled(false); // Makes the button content area transparent
        setBorderPainted(false); // Do not paint the border
        setFocusPainted(false); // Do not paint the focus outline
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Draw the background image
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }
}
