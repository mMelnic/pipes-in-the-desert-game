package GUI;
import java.awt.*;
import javax.swing.*;

public class SpringView extends JPanel {
    private Color color;
    private int size;

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
}