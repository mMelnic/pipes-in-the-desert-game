package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class MapWindow {

    private JFrame frame;
    private JPanel mapPanel;

    public MapWindow() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Map Window");
        frame.setBounds(100, 100, 800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGrid(g);
            }
        };
        frame.getContentPane().add(mapPanel);
    }

    private void drawGrid(Graphics g) {
        int rows = 8; 
        int columns = 8; 
        int squareSize = 100; 

        // Draw vertical lines
        for (int i = 0; i <= columns; i++) {
            int x = i * squareSize;
            g.drawLine(x, 0, x, squareSize * rows);
        }

        // Draw horizontal lines
        for (int i = 0; i <= rows; i++) {
            int y = i * squareSize;
            g.drawLine(0, y, squareSize * columns, y);
        }
    }

    public void show() {
        frame.setVisible(true);
    }
}