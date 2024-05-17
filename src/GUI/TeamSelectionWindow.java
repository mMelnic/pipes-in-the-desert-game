package GUI;

import javax.swing.*;

import system.GameManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeamSelectionWindow {

    private JFrame frame;
    private GameManager gameManager;
    private MainWindow mainWindow;

    public TeamSelectionWindow(GameManager gameManager, MainWindow mainWindow) {
        this.gameManager = gameManager;
        this.mainWindow = mainWindow;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Team Selection");
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("=== TEAMS ===", JLabel.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton plumbersBtn = new JButton("Plumbers");
        JButton saboteursBtn = new JButton("Saboteurs");
        JButton returnBtn = new JButton("Return to Main Menu");

        plumbersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        saboteursBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        plumbersBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameManager.setActiveTeam(0); // Set to Plumbers
                gameManager.startGame();
                frame.dispose();
                MapWindow mapWindow = new MapWindow();
                mapWindow.show();
            }
        });

        saboteursBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameManager.setActiveTeam(1); // Set to Saboteurs
                gameManager.startGame();
                frame.dispose();
                MapWindow mapWindow = new MapWindow();
                mapWindow.show();
            }
        });

        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); 
            
            }
        });

        panel.add(Box.createVerticalStrut(10));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(plumbersBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(saboteursBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(returnBtn);
        panel.add(Box.createVerticalStrut(10));

        frame.getContentPane().add(panel);
    }

    public void show() {
        frame.setVisible(true);
    }
}