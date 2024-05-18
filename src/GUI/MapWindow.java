package GUI;

import javax.swing.*;

import components.Cistern;
import components.Pump;
import components.Pipe;
import components.Spring;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import system.Cell;
import system.GameManager;
import system.Map;

public class MapWindow {

    private JFrame frame;
    private JPanel mapPanel;
    private Map map;
    PlumberController plumberController;
    SaboteurController saboteurController;

    public MapWindow(int mapSize, GameManager gameManager) {
        initialize(mapSize);
        saboteurController = new SaboteurController(gameManager.getActiveSaboteur(), mapPanel);
        this.map = gameManager.getMap();
        // Add the plumber view to the map panel
        plumberController = new PlumberController(gameManager.getActivePlumber(), mapPanel);
        // Add key listener to the frame
        frame.addKeyListener(plumberController);
        frame.addKeyListener(saboteurController);

        // Make the frame visible
        frame.setVisible(true);

        // Ensure the frame has focus to receive key events
        frame.requestFocusInWindow();
    }

    private void initialize(int mapSize) {
        int squareSize = 80;
        int frameSize = mapSize * squareSize;
        frame = new JFrame();
        frame.setTitle("Map Window");
        frame.setBounds(100, 100, frameSize + 20, frameSize + 40);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMap(g, squareSize);
            }
        };
        frame.getContentPane().add(mapPanel);
    }

    private void drawMap(Graphics g, int squareSize) {
        // int squareSize = 80;
        // Remove all components from the mapPanel
        mapPanel.removeAll();

        // Draw the grid
        drawGrid(g, squareSize);

        drawComponents(g, squareSize);
        plumberController.getPlumberView().setLocation(0, 0);
        mapPanel.add(plumberController.getPlumberView());
        saboteurController.getSaboteurView().setLocation(0, 0);
        mapPanel.add(saboteurController.getSaboteurView());
        mapPanel.setComponentZOrder(plumberController.getPlumberView(), 1);
        mapPanel.setComponentZOrder(saboteurController.getSaboteurView(), 1);
    }

    private void drawGrid(Graphics g, int squareSize) {
        int rows = map.getRows();
        int columns = map.getColumns();

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
                    // if (cell.isPlayerOn()) {
                    //     g.setColor(Color.RED);
                    //     g.fillOval(x, y, squareSize, squareSize);
                    // } else 
                    if (cell.getComponent() instanceof Pipe) {
                        // g.setColor(Color.GREEN);
                        // g.fillRect(x, y + squareSize / 3, squareSize, squareSize / 3);
                        PipeView pv = new PipeView((Pipe)cell.getComponent());
                        pv.setBounds(x, y, squareSize, squareSize);
                        mapPanel.add(pv);
                        mapPanel.setComponentZOrder(pv, 0); // Places component1 at index 0 in the Z-order
                    } else if (cell.getComponent() instanceof Pump) {
                        // g.setColor(Color.BLUE);
                        // g.fillRect(x, y, squareSize, squareSize);
                        PumpView pumpView = new PumpView((Pump)cell.getComponent());
                        pumpView.setBounds(x, y, squareSize, squareSize);
                        mapPanel.add(pumpView);
                        mapPanel.setComponentZOrder(pumpView, 0);
                    } else if (cell.getComponent() instanceof Cistern) {
                        g.setColor(Color.PINK);
                        g.fillRect(x, y, squareSize, squareSize);
                        // CisternView cisternView = new CisternView((Cistern)cell.getComponent());
                        // cisternView.setBounds(x, y, squareSize, squareSize);
                        // mapPanel.add(cisternView);
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