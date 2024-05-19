package GUI;

import java.awt.Color;
import javax.swing.*;
import player.*; 

public class ScorerView extends JPanel {
    JLabel plumberScoreLabel = new JLabel();
    JLabel saboteurScoreLabel = new JLabel();

    PlumberScorer plumberScorer = new PlumberScorer();
    SaboteurScorer saboteurScorer = new SaboteurScorer();

    public ScorerView(){
        drawScorer();
        setBackground(Color.blue);
    } 

    public void drawScorer(){
        this.setBounds(0, 0, 1, 1);
        plumberScoreLabel.setText("Plumber score: " + plumberScorer.getScore());
        saboteurScoreLabel.setText("Saboteur score:" + saboteurScorer.getScore());

        saboteurScoreLabel.setForeground(Color.pink); 
        plumberScoreLabel.setForeground(Color.PINK);
        
        plumberScoreLabel.setBounds(0, 0, 1 ,1);
        saboteurScoreLabel.setBounds(1, 1, 1, 1);
        plumberScoreLabel.setVisible(true);
        saboteurScoreLabel.setVisible(true);
    }
}