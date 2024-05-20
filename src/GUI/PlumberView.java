package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import components.Pipe;
import components.Pump;
import enumerations.Direction;
import player.Plumber;
import system.PlumberImageLoader;

public class PlumberView extends JPanel {
    private Plumber plumberPlayer;
    private String color;

    public PlumberView(Plumber plumberPlayer, String color) {
        this.plumberPlayer = plumberPlayer;
        this.color = color;
        setPreferredSize(new Dimension(
                plumberPlayer.getCurrentCell().getMap().getColumns() * 80,
                plumberPlayer.getCurrentCell().getMap().getRows() * 80));
        setBackground(new Color(0, 0, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderPlumberPlayer(g);
    }

    private void renderPlumberPlayer(Graphics g) {
        BufferedImage imageToDraw = null;
        Direction facingDirection = plumberPlayer.getFacingDirection();
        imageToDraw = PlumberImageLoader.getImage(color, facingDirection);

        int row = plumberPlayer.getCurrentCell().getRow();
        int column = plumberPlayer.getCurrentCell().getColumn();
        int cellSize = 80; // Assuming each cell is 80x80 pixels

        g.drawImage(imageToDraw, column * cellSize, row * cellSize, cellSize, cellSize, this);

        // code to add visible carried component
        if (plumberPlayer.getCarriedComponent() instanceof Pipe) {
            g.drawImage(PlumberImageLoader.getPlumberPickPipeImage(), column * cellSize, row * cellSize, cellSize,
                    cellSize, this);
        } else if (plumberPlayer.getCarriedComponent() instanceof Pump) {
            g.drawImage(PlumberImageLoader.getPlumberPickPumpImage(), column * cellSize, row * cellSize, cellSize,
                    cellSize, this);
        }
        
    }
}
