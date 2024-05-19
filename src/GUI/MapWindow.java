package GUI;

import components.Cistern;
import components.Pipe;
import components.Pump;
import components.Spring;
import java.awt.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.*;
import system.Cell;
import system.GameManager;
import system.Map;

public class MapWindow {

    private JFrame frame;
    private JPanel mapPanel;
    private Map map;
    PlumberController plumberController;
    SaboteurController saboteurController;
    private JLabel timeLabel; 
    private JLabel plumberScoreLabel;
    private JLabel saboteurScoreLabel;
    private Timer timer;
    private long endTime;
    private long duration;

    public MapWindow(int mapSize, GameManager gameManager) {
        this.duration = gameManager.getDuration();
        initialize(mapSize);
        this.map = gameManager.getMap();
        map.setMapPanel(mapPanel);
        saboteurController = new SaboteurController(gameManager.getActiveSaboteur(), mapPanel);
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
        frame.setBounds(100, 100, frameSize + 20, frameSize + 90);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel spacerPanel = new JPanel();
        spacerPanel.setLayout(new BorderLayout());
        spacerPanel.setPreferredSize(new Dimension(frameSize + 20, 50)); 

        timeLabel = new JLabel();
        timeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        frame.add(spacerPanel, BorderLayout.NORTH);

        plumberScoreLabel = new JLabel();
        plumberScoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        plumberScoreLabel.setHorizontalAlignment(SwingConstants.LEFT);

        frame.add(spacerPanel, BorderLayout.NORTH);
        spacerPanel.add(timeLabel, BorderLayout.EAST);
        spacerPanel.add(plumberScoreLabel, BorderLayout.WEST);
        
        mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load the background image and draw it
                try {
                    Image backgroundImage = ImageIO.read(getClass().getResource("/resources/images/desert_top_view_just_sand_best.jpeg"));
                    // Image backgroundImage = ImageIO.read(getClass().getResource("/resources/images/desertView.png"));
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                drawMap(g, squareSize);
            }
        };
        frame.getContentPane().add(mapPanel);
        startTimer();
    }

    public void startTimer() {
        final long interval = 1000; 
        endTime = System.currentTimeMillis() + duration;

        timer = new Timer("GameTimer");

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                long remainingTime = getRemainingTime();
                long minutes = (remainingTime / 1000) / 60;
                long seconds = (remainingTime / 1000) % 60;
                timeLabel.setText(String.format("Remaining time: %02d:%02d", minutes, seconds));
            }
        }, 0, interval);
    }

    public long getRemainingTime() {
        if (timer == null) {
            return 0;
        }
        long currentTime = System.currentTimeMillis();
        return Math.max(endTime - currentTime, 0);
    }

    private void drawMap(Graphics g, int squareSize) {
        // int squareSize = 80;
        // Remove all components from the mapPanel
        mapPanel.removeAll();

        plumberScoreLabel.setText("Plumber score: ");

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
                    if (cell.getComponent() instanceof Pipe) {
                        PipeView pv = new PipeView((Pipe)cell.getComponent());
                        pv.setBounds(x, y, squareSize, squareSize);
                        mapPanel.add(pv);
                        mapPanel.setComponentZOrder(pv, 0); // Places component at index 0 in the Z-order
                    } else if (cell.getComponent() instanceof Pump) {
                        PumpView pumpView = new PumpView((Pump)cell.getComponent());
                        pumpView.setBounds(x, y, squareSize, squareSize);
                        mapPanel.add(pumpView);
                        mapPanel.setComponentZOrder(pumpView, 0);
                    } else if (cell.getComponent() instanceof Cistern) {
                        CisternView cisternView = new CisternView((Cistern)cell.getComponent());
                        cisternView.setBounds(x, y, squareSize, squareSize);
                        mapPanel.add(cisternView);
                    } else if (cell.getComponent() instanceof Spring) {
                        SpringView springView = new SpringView((Spring)cell.getComponent());
                        springView.setBounds(x, y, squareSize, squareSize);
                        mapPanel.add(springView);
                    }

                }
            }
        }
    }

    public void show() {
        frame.setVisible(true);
    }
}