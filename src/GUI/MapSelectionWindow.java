package GUI;
import javax.swing.*;

import system.GameManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * The {@code MapSelectionWindow} class represents a window for selecting the map size for the game.
 * It provides options for different map sizes and a button to return to the main menu.
 */
public class MapSelectionWindow {
    private GameManager gameManager;
    private JFrame frame;


    /**
     * Constructs a new {@code MapSelectionWindow} with the specified game manager.
     *
     * @param gameManager the game manager to handle game-related operations
     */
    public MapSelectionWindow(GameManager gameManager) {
        this.gameManager = gameManager;
        initUI();
    }


    /**
     * Initializes the user interface components of the map selection window.
     */
    private void initUI() {
        frame = new JFrame();
        frame.setTitle("Map Selection");
        frame.setBounds(100, 100, 450, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mapSelectionPanel = new JPanel();
        mapSelectionPanel.setLayout(new BoxLayout(mapSelectionPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Select Map Size");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton smallMapBtn = new JButton("Small (8x8)");
        JButton mediumMapBtn = new JButton("Medium (10x10)");
        JButton largeMapBtn = new JButton("Large (15x15)");
        JButton backBtn = new JButton("Back to Main Menu");

        smallMapBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        mediumMapBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        largeMapBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        smallMapBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameManager.selectMapSize(8);
                frame.dispose();
                TeamSelectionWindow teamSelectionWindow = new TeamSelectionWindow(gameManager,8);
                teamSelectionWindow.show();
            }
        });

        mediumMapBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameManager.selectMapSize(9);
                frame.dispose();
                TeamSelectionWindow teamSelectionWindow = new TeamSelectionWindow(gameManager,9);
                teamSelectionWindow.show();
            }
        });

        largeMapBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameManager.selectMapSize(10);
                frame.dispose();
                TeamSelectionWindow teamSelectionWindow = new TeamSelectionWindow(gameManager,10);
                teamSelectionWindow.show();
            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                MainWindow main = new MainWindow(gameManager);
                main.show();
            }
        });

        mapSelectionPanel.add(Box.createVerticalStrut(20));
        mapSelectionPanel.add(titleLabel);
        mapSelectionPanel.add(Box.createVerticalStrut(20));
        mapSelectionPanel.add(smallMapBtn);
        mapSelectionPanel.add(Box.createVerticalStrut(10));
        mapSelectionPanel.add(mediumMapBtn);
        mapSelectionPanel.add(Box.createVerticalStrut(10));
        mapSelectionPanel.add(largeMapBtn);
        mapSelectionPanel.add(Box.createVerticalStrut(10));
        mapSelectionPanel.add(backBtn);

        frame.getContentPane().add(mapSelectionPanel);
    }
    
    /**
     * Displays the map selection window.
     */
    public void show() {
        frame.setVisible(true);
    }
}