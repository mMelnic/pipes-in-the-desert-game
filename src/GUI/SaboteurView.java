package GUI;

import enumerations.Direction;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import player.Saboteur;

public class SaboteurView extends JPanel {
    private Saboteur saboteurPlayer;
    private BufferedImage saboteurDownImage;
    private BufferedImage saboteurUpImage;
    private BufferedImage saboteurLeftImage;
    private BufferedImage saboteurRightImage;

    public SaboteurView(Saboteur saboteurPlayer) {
        this.saboteurPlayer = saboteurPlayer;
        setPreferredSize(new Dimension(saboteurPlayer.getCurrentCell().getMap().getColumns() * 80,
                saboteurPlayer.getCurrentCell().getMap().getRows() * 80));
        setBackground(new Color(0, 0, 0, 0));
        loadImages();
    }

    private void loadImages() {
        try {
            saboteurDownImage = ImageIO.read(getClass().getResource("/resources/images/saboteurView_down_yellow.png"));
            saboteurUpImage = ImageIO.read(getClass().getResource("/resources/images/saboteurView_top_yellow.png"));
            saboteurLeftImage = ImageIO.read(getClass().getResource("/resources/images/saboteurView_yellow_left.png"));
            saboteurRightImage = ImageIO.read(getClass().getResource("/resources/images/saboteurView_yellow.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        renderSaboteurPlayer(g);
    }

    private void renderSaboteurPlayer(Graphics g) {
        BufferedImage imageToDraw = saboteurDownImage;
        Direction facingDirection = saboteurPlayer.getFacingDirection();

        switch (facingDirection) {
            case UP:
                imageToDraw = saboteurUpImage;
                break;
            case DOWN:
                imageToDraw = saboteurDownImage;
                break;
            case LEFT:
                imageToDraw = saboteurLeftImage;
                break;
            case RIGHT:
                imageToDraw = saboteurRightImage;
                break;
        }

        int row = saboteurPlayer.getCurrentCell().getRow();
        int column = saboteurPlayer.getCurrentCell().getColumn();
        int cellSize = 80; // Assuming each cell is 80x80 pixels

        g.drawImage(imageToDraw, column * cellSize, row * cellSize, cellSize, cellSize, this);
    }
}
