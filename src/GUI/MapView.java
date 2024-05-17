package GUI;
import javax.swing.*;

import system.GameManager;

import java.awt.*;
import java.util.Map;

public class MapView extends JPanel {
    private Map gameMap;
    private int CELL_SIZE;
    private GameManager gameManager;
    private Font font;

    public MapView(Map gameMap, int CELL_SIZE, GameManager gameManager) {
        this.gameMap = gameMap;
        this.CELL_SIZE = CELL_SIZE;
        this.gameManager = gameManager;
        this.font = new Font("Arial", Font.BOLD, 14);
        setPreferredSize(new Dimension(700, 700));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.BLACK);
        for (int i = 0; i <= 700; i += CELL_SIZE) {
            g.drawLine(i, 0, i, 700); // vertical lines
            g.drawLine(0, i, 700, i); // horizontal lines
        }
    }
}
