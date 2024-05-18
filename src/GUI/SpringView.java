package GUI;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
public class SpringView extends JPanel {
    private Color color;
    private int size;
    private BufferedImage springImage;

    public SpringView(Color color, int size) {
        this.color = color;
        this.size = size;
        setPreferredSize(new Dimension(size, size));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillOval(0, 0, getWidth(), getHeight());
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