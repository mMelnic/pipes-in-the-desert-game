package GUI;
import javax.swing.*;

import components.Cistern;
import components.Pump;
import components.Pipe;
import components.Spring;

import java.util.List;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import system.Cell;
import system.Map;
public class MapWindow {

    private JFrame frame;
    private JPanel mapPanel;
    private Map map;

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
                drawMap(g);
            }
        };
        frame.getContentPane().add(mapPanel);
        map = new Map(8, 8); 
        map.initializeMap(); 
    }

    private void drawMap(Graphics g) {
        int squareSize = 100;

        // Draw the grid
        drawGrid(g, squareSize);

        drawComponents(g, squareSize);
    }

    private void drawGrid(Graphics g, int squareSize) {
        int rows = 8;
        int columns = 8;

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
    private void drawComponents(Graphics g, int squareSize) {
        for (int i = 0; i < map.rows; i++) {
            for (int j = 0; j < map.columns; j++) {
                Cell cell = map.getCells(i, j);
                int x = j * squareSize;
                int y = i * squareSize;

                if (!cell.isEmpty()) {
                    if (cell.isPlayerOn()) {
                        g.setColor(Color.RED);
                        g.fillOval(x + 10, y + 10, squareSize - 20, squareSize - 20);
                    } else if (cell.getComponent() instanceof Pipe) {
                        g.setColor(Color.GREEN);
                        g.fillRect(x, y + squareSize / 3, squareSize, squareSize / 3); 
                    } else if (cell.getComponent() instanceof Pump) {
                        g.setColor(Color.BLUE);
                        g.fillRect(x, y, squareSize, squareSize); 
                    } else if (cell.getComponent() instanceof Cistern) {
                        g.setColor(Color.PINK);
                        g.fillRect(x, y, squareSize, squareSize); 
                    } else if (cell.getComponent() instanceof Spring) {
                        g.setColor(new Color(128, 0, 128)); 
                        g.fillRect(x, y, squareSize, squareSize); 
                    }
                }
            }
        }
    }

    public void show() {
        frame.setVisible(true);
    }
}