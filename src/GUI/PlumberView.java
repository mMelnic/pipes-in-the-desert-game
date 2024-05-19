package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import enumerations.Direction;
import player.Plumber;

public class PlumberView extends JPanel {
    private Plumber plumberPlayer;
    private BufferedImage plumberDownImage;
    private BufferedImage plumberUpImage;
    private BufferedImage plumberLeftImage;
    private BufferedImage plumberRightImage;

    public PlumberView(Plumber plumberPlayer) {
        this.plumberPlayer = plumberPlayer;
        setPreferredSize(new Dimension(plumberPlayer.getCurrentCell().getMap().getColumns() * 80, 
                plumberPlayer.getCurrentCell().getMap().getRows() * 80));
        setBackground(new Color(0,0,0,0));
        loadImages();
    }

    private void loadImages() {
        try {
            plumberDownImage = ImageIO.read(getClass().getResource("/resources/images/plumber_down.png"));
            plumberUpImage = ImageIO.read(getClass().getResource("/resources/images/plumber_up.png"));
            plumberLeftImage = ImageIO.read(getClass().getResource("/resources/images/plumber_left.png"));
            plumberRightImage = ImageIO.read(getClass().getResource("/resources/images/plumber_right.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        renderPlumberPlayer(g);
    }

    private void renderPlumberPlayer(Graphics g) {
        BufferedImage imageToDraw = plumberDownImage;
        Direction facingDirection = plumberPlayer.getFacingDirection();

        switch (facingDirection) {
            case UP:
                imageToDraw = plumberUpImage;
                break;
            case DOWN:
                imageToDraw = plumberDownImage;
                break;
            case LEFT:
                imageToDraw = plumberLeftImage;
                break;
            case RIGHT:
                imageToDraw = plumberRightImage;
                break;
        }

        int row = plumberPlayer.getCurrentCell().getRow();
        int column = plumberPlayer.getCurrentCell().getColumn();
        int cellSize = 80; // Assuming each cell is 80x80 pixels

        g.drawImage(imageToDraw, column * cellSize, row * cellSize, cellSize, cellSize, this);
    }
}
