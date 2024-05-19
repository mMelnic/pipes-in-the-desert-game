package GUI;

import components.Cistern;
import components.Pipe;
import components.Pump;
import components.Spring;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import player.PlumberScorer;
import player.SaboteurScorer;
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
    private Thread timerThread;
    private AtomicBoolean timerRunning = new AtomicBoolean(false);
    GameManager gameManager;
    boolean isTimeUp;

    private Image backgroundImage;

    private PlumberScorer plumberScorer;
    private SaboteurScorer saboteurScorer;

    BufferedImage coinImage = null;
    ImageIcon coinIcon = null;
    
    
    public MapWindow(int mapSize, GameManager gameManager) {
        this.gameManager = gameManager;
        initialize(mapSize);
        this.map = gameManager.getMap();
        map.setMapPanel(mapPanel);
        saboteurController = new SaboteurController(gameManager.getActiveSaboteur(), gameManager.getActiveSaboteur2());
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
        gameManager.setMapWindow(this);
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

        plumberScorer = new PlumberScorer();
        saboteurScorer = new SaboteurScorer();

        try {
            coinImage = ImageIO.read(getClass().getResource("/resources/images/coin.png"));
            int height = 25;
            int width = 25;
            Image smallerImage = coinImage.getScaledInstance(width, height, 0);
            coinIcon = new ImageIcon(smallerImage);
        }
        catch (Exception ex) {ex.printStackTrace();}
        
        plumberScoreLabel = new JLabel();
        plumberScoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        plumberScoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        plumberScoreLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        if (coinIcon != null) {plumberScoreLabel.setIcon(coinIcon);}

        saboteurScoreLabel = new JLabel();
        saboteurScoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        saboteurScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        saboteurScoreLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        if (coinIcon != null) {saboteurScoreLabel.setIcon(coinIcon);}

        frame.add(spacerPanel, BorderLayout.NORTH);
        spacerPanel.add(timeLabel, BorderLayout.EAST);
        spacerPanel.add(plumberScoreLabel, BorderLayout.WEST);
        spacerPanel.add(saboteurScoreLabel, BorderLayout.CENTER);
        try {
            backgroundImage = ImageIO
                    .read(getClass().getResource("/resources/images/desert_top_view_just_sand_best.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load the background image and draw it
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                drawMap(g, squareSize);
            }
        };
        JButton menuButton = new JButton("Menu");
        menuButton.setBackground(new Color(46, 204, 113));
        menuButton.setForeground(Color.WHITE);
        menuButton.setFont(new Font("Arial", Font.BOLD, 16));
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMenu(menuButton); 
            }
        });
        frame.add(menuButton, BorderLayout.SOUTH);
        frame.getContentPane().add(mapPanel);
        startTimer();
    }
    private void showMenu(Component invoker) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

      
        menu.add(exitItem);


        menu.show(invoker, 0, invoker.getHeight());
    }


    public void startTimer() {
        duration = 20 * 60 * 1000;
        endTime = System.currentTimeMillis() + duration;

        timerRunning.set(true);
        timerThread = new Thread(() -> {
            try {
                while (timerRunning.get() && !isTimeUp) {
                    long remainingTime = getRemainingTime();
                    long minutes = (remainingTime / 1000) / 60;
                    long seconds = (remainingTime / 1000) % 60;
                    timeLabel.setText(String.format("Remaining time: %02d:%02d", minutes, seconds));

                    if (remainingTime <= 0) {
                        isTimeUp = true;
                        finishGame();
                        System.out.println("\nTime is up!\n\n\n");
                        // writeToOutputTxt("\nTime is up!\n\n\n");
                    }

                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        timerThread.start();
    }

    public void DisplayCisternFullWindow(){
        String winner = "";
        int plumberScore = gameManager.getPlumberScore();
        int saboteurScore = gameManager.getSaboteurScore();
        if (plumberScore > saboteurScore) {
            winner = "Plumbers win!";
        } else if (saboteurScore > plumberScore) {
            winner = "Saboteurs win!";
        }else if (saboteurScore == plumberScore){
            winner = "It's a tie!";
        }
        frame.dispose();
        CisternFullWindow cisternFullWindow = new CisternFullWindow(plumberScore, saboteurScore, winner, gameManager);
        cisternFullWindow.show();
    }

    public void finishGame() {
        timerRunning.set(false);
        timerThread.interrupt();
        map.stopLeakingAndFreeEnds();
        String winner = "";
        int plumberScore = gameManager.getPlumberScore();
        int saboteurScore = gameManager.getSaboteurScore();
        if (plumberScore > saboteurScore) {
            winner = "Plumbers win!";
        } else if (saboteurScore > plumberScore) {
            winner = "Saboteurs win!";
        }else if (saboteurScore == plumberScore){
            winner = "It's a tie!";
        }
        frame.dispose();
        TimeUpWindow timeUpWindow = new TimeUpWindow(plumberScore, saboteurScore, winner, gameManager);
        timeUpWindow.show();
    }

  public long getRemainingTime() {
        if (!timerRunning.get()) {
            return 0;
        }
        long currentTime = System.currentTimeMillis();
        return Math.max(endTime - currentTime, 0);
    }

    private void drawMap(Graphics g, int squareSize) {
        // int squareSize = 80;
        // Remove all components from the mapPanel
        mapPanel.removeAll();

        plumberScoreLabel.setText("Plumber score: " + plumberScorer.getScore());
        saboteurScoreLabel.setText("Saboteur score: " + saboteurScorer.getScore());

        // Draw the grid
        drawGrid(g, squareSize);

        drawComponents(g, squareSize);
        plumberController.getPlumberView().setLocation(0, 0);
        mapPanel.add(plumberController.getPlumberView());
        saboteurController.getSaboteurView().setLocation(0, 0);
        mapPanel.add(saboteurController.getSaboteurView());
        saboteurController.getSaboteurView2().setLocation(0, 0);
        mapPanel.add(saboteurController.getSaboteurView2());
        mapPanel.setComponentZOrder(plumberController.getPlumberView(), 1);
        mapPanel.setComponentZOrder(saboteurController.getSaboteurView(), 1);
        mapPanel.setComponentZOrder(saboteurController.getSaboteurView2(), 1);

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
                    if (cell.getComponent() instanceof Pipe pipe) {
                        PipeView pv = new PipeView(pipe);
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
                    } else if (cell.getComponent() instanceof Spring spring) {
                        SpringView springView = new SpringView(spring);
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