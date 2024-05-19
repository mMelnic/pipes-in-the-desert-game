package GUI;

import javax.swing.*;

import system.GameManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimeUpWindow {
    private JFrame frame;
    private GameManager gameManager;

    public TimeUpWindow(int plumberScore, int saboteurScore, String winner, GameManager gameManager) {
        initialize(plumberScore, saboteurScore, winner);
        this.gameManager = gameManager;
    }

    private void initialize(int plumberScore, int saboteurScore, String winner) {
        frame = new JFrame("Time's Up!");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout()); 
        frame.getContentPane().setBackground(new Color(220, 220, 129)); 
    
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new GridLayout(4, 1));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
    
        Font labelFont = new Font("Arial", Font.BOLD, 16); 
    
        JLabel titleLabel = new JLabel("Time's Up!");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(labelFont);
        contentPanel.add(titleLabel);
    
        JLabel scoreLabel = new JLabel("Plumber Score: " + plumberScore + " - Saboteur Score: " + saboteurScore);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setFont(labelFont);
        contentPanel.add(scoreLabel);
    
        JLabel winnerLabel = new JLabel("Winner: " + winner);
        winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        winnerLabel.setFont(labelFont);
        contentPanel.add(winnerLabel);
    
        JButton returnButton = new JButton("Return to Main Menu");
        returnButton.setFont(labelFont);
        returnButton.setBackground(new Color(230, 83, 24)); 
        returnButton.setForeground(Color.WHITE);
        returnButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30)); 
        returnButton.setFocusPainted(false); 
    
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                MainWindow mainWindow = new MainWindow(gameManager);
                mainWindow.show();
            }
        });
        contentPanel.add(returnButton);
    
        frame.add(contentPanel, BorderLayout.CENTER); 
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }

    public void show(){
        frame.setVisible(true);
    }
}