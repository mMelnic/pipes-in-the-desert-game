package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import system.GameManager;

/**
 * The {@code MainWindow} class represents the main menu window for the game "Pipes in the Desert".
 * It provides options to start the game, view key bindings, and exit the application.
 */
public class MainWindow {
    private JFrame frame;
    private BufferedImage backGroundImage = null;
    private BufferedImage titleImage = null;
    private BufferedImage startButtonImage = null;
    private BufferedImage keyBindingsButtonImage = null;
    private BufferedImage exitButtonImage = null;



    private GameManager gameManager;

    /**
     * Constructs a new {@code MainWindow} with the specified game manager.
     *
     * @param gameManager the game manager to handle game-related operations
     */
    public MainWindow(GameManager gameManager) {
        this.gameManager = gameManager;
        initialize();
    }

    /**
     * Initializes the contents of the main menu window.
     */
    private void initialize() {

        frame = new JFrame("Pipes in the Desert");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.getContentPane().setLayout(new GridLayout(4, 3, 0, 10));
        GridBagLayout gridBagLayout = new GridBagLayout();
        frame.getContentPane().setLayout(gridBagLayout);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 0, 10, 0);
        frame.setLocationRelativeTo(null);

        //title
        try {
            titleImage =  ImageIO.read(getClass().getResource("/resources/images/main_menu.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageButton titleLabel = new ImageButton(titleImage);
        //frame.getContentPane().add(titleLabel);
        titleLabel.setPreferredSize(new Dimension(140, 40));
        c.gridx = 0;
        c.gridy = 0;
        gridBagLayout.setConstraints(titleLabel, c);
        frame.getContentPane().add(titleLabel);

        // Start button
        try {
            startButtonImage =  ImageIO.read(getClass().getResource("/resources/images/start_button.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageButton startButton = new ImageButton(startButtonImage);
        
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showTeams();
                
            }
        });
        //frame.add(startButton);
        startButton.setPreferredSize(new Dimension(140, 40));
        c.gridx = 0;
        c.gridy = 1;
        gridBagLayout.setConstraints(startButton, c);
        frame.getContentPane().add(startButton);

        //  Key bindings button
        try {
            keyBindingsButtonImage = ImageIO.read(getClass().getResource("/resources/images/key_bindings_button.png"));
        } catch (Exception e) {
        }

        ImageButton keyBindingsButton = new ImageButton(keyBindingsButtonImage);
        keyBindingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showKeyBindingsWindow();
            }
        });
        // frame.add(keyBindingsButton);
        keyBindingsButton.setPreferredSize(new Dimension(140, 40));
        c.gridx = 0;
        c.gridy = 2;
        gridBagLayout.setConstraints(keyBindingsButton, c);
        frame.getContentPane().add(keyBindingsButton);

        //Exit button
        try {
            exitButtonImage =  ImageIO.read(getClass().getResource("/resources/images/exit_button.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageButton exitButton = new ImageButton(exitButtonImage);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        // frame.add(exitButton);
        exitButton.setPreferredSize(new Dimension(140, 40));
        c.gridx = 0;
        c.gridy = 3;
        gridBagLayout.setConstraints(exitButton, c);
        frame.getContentPane().add(exitButton);
    }

    /**
     * Displays the key bindings window.
     */
    private void showKeyBindingsWindow() {
        //frame.dispose(); 
        KeyBindingsWindow keyBindingsWindow = new KeyBindingsWindow(frame);
        keyBindingsWindow.show();
    }
    // private void showMapWindow() {
    //     frame.dispose(); 
    // MapWindow mapWindow = new MapWindow();
    // mapWindow.show();
    // }

    /**
     * Displays the team selection window and disposes of the main menu window.
     */
    public void showTeams() {
        frame.dispose();
        MapSelectionWindow teamSelectionWindow = new MapSelectionWindow(gameManager);
        teamSelectionWindow.show();
    }

    /**
     * Displays the main menu window.
     */
    public void show() {
        frame.setVisible(true);
    }
}