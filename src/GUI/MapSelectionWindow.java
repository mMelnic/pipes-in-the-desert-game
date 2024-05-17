package GUI;
import javax.swing.*;

import system.GameManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MapSelectionWindow {
    private GameManager gameManager;
    private JFrame frame;

    public MapSelectionWindow(GameManager gameManager) {
        this.gameManager = gameManager;
        initUI();
    }

    private void initUI() {
        frame = new JFrame();
        frame.setTitle("Map Selection");
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mapSelectionPanel = new JPanel();
        mapSelectionPanel.setLayout(new BoxLayout(mapSelectionPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Select Map Size");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton smallMapBtn = new JButton("Small (5x5)");
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
                gameManager.selectMapSize(5);
                frame.dispose();
                TeamSelectionWindow teamSelectionWindow = new TeamSelectionWindow(gameManager);
                teamSelectionWindow.show();
            }
        });

        mediumMapBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameManager.selectMapSize(10);
                frame.dispose();
                TeamSelectionWindow teamSelectionWindow = new TeamSelectionWindow(gameManager);
                teamSelectionWindow.show();
            }
        });

        largeMapBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameManager.selectMapSize(15);
                frame.dispose();
                TeamSelectionWindow teamSelectionWindow = new TeamSelectionWindow(gameManager);
                teamSelectionWindow.show();
            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
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
    public void show() {
        frame.setVisible(true);
    }
}