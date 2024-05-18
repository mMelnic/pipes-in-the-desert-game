package GUI;

import javax.swing.*;

import system.GameManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeamSelectionWindow {

    private JFrame frame;
    private GameManager gameManager;

    public TeamSelectionWindow(GameManager gameManager, int size) {
        this.gameManager = gameManager;
        initialize(size);
    }

    private void initialize(int size) {
        frame = new JFrame();
        frame.setTitle("Team Selection");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

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
                MapWindow mapWindow = new MapWindow(size, gameManager);
                mapWindow.show();

            }
        });

        saboteursBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameManager.setActiveTeam(1); // Set to Saboteurs
                gameManager.startGame();
                frame.dispose();
                MapWindow mapWindow = new MapWindow(size, gameManager);
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