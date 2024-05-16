package GUI;
import javax.swing.*;

import system.GameManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow {
    private JFrame frame;
    private GameManager gameManager;

    public MainWindow(GameManager gameManager) {
        this.gameManager = gameManager;
        initialize();
    }

    private void initialize() {

        frame = new JFrame("Pipes in the Desert");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(4, 1, 0, 0));

        //title
        JLabel titleLabel = new JLabel("=== PIPES IN THE DESERT ===", SwingConstants.CENTER);
        frame.getContentPane().add(titleLabel);

        // Start button
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameManager.showMaps(); 
            }
        });
        frame.getContentPane().add(startButton);

        //  Key bindings button
        JButton keyBindingsButton = new JButton("Key bindings");
        keyBindingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showKeyBindingsWindow();
            }
        });
        frame.getContentPane().add(keyBindingsButton);

        //Exit button
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        frame.getContentPane().add(exitButton);
    }
    private void showKeyBindingsWindow() {
        frame.dispose(); 
        KeyBindingsWindow keyBindingsWindow = new KeyBindingsWindow();
        keyBindingsWindow.show();
    }


    public void show() {
        frame.setVisible(true);
    }
}