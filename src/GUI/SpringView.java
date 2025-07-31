package GUI;
import components.Spring;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * The SpringView class is responsible for rendering the graphical representation of a spring component.
 * It displays the spring image centered within the panel.
 */
public class SpringView extends JPanel {
    private BufferedImage springImage;
    private Spring spring;

    /**
     * Constructs a new SpringView for the specified spring component.
     *
     * @param spring the spring component to be rendered
     */
    public SpringView(Spring spring) {
        this.spring = spring;
        setPreferredSize(new Dimension(80, 80));
        setBackground(new Color(0, 0, 0, 0));
        initializeImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);

        int imageWidth = springImage.getWidth();
        int imageHeight = springImage.getHeight();
        int x = (getWidth() - imageWidth) / 2;
        int y = (getHeight() - imageHeight) / 2;
        g.drawImage(springImage, x, y, this);
    }

    /**
     * Initializes the image for the spring by loading it from the resources.
     */
    private void initializeImage(){
        try {
            springImage = loadImage("/resources/images/springView.png");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Loads an image from the specified path.
     *
     * @param path the path to the image file
     * @return the loaded BufferedImage
     * @throws IOException if an error occurs during reading the image
     */
    private BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(getClass().getResource(path));
    }
}