package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import system.GameManager;

public class TimerWindow extends JFrame{
    private GameManager gameManager;
    private JLabel timerLabel;
    private Timer swingTimer;

    public TimerWindow(){

    }

    public void initialize(){
        setTitle("Timer Window");
        setSize(200, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    
}