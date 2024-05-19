package GUI;
import components.Spring;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class SpringView extends JPanel {
    private Color color;
    private int size;
    private BufferedImage springImage;
    private Spring spring;

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

    private void initializeImage(){
        try {
            springImage = loadImage("/resources/images/springView.png");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(getClass().getResource(path));
    }
}