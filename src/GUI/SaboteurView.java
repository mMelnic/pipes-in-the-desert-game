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

/**
    * The SaboteurView class is responsible for rendering the graphical representation of a saboteur player.
    * It displays the saboteur in different orientations and colors based on the player's direction and state.
    */
public class SaboteurView extends JPanel {
    private Saboteur saboteurPlayer;
    private BufferedImage saboteurDownImage;
    private BufferedImage saboteurUpImage;
    private BufferedImage saboteurLeftImage;
    private BufferedImage saboteurRightImage;

    
    /**
     * Constructs a new SaboteurView for the specified saboteur player and color.
     *
     * @param saboteurPlayer the saboteur player to be rendered
     * @param color the color of the saboteur (used to load the appropriate images)
     */
    public SaboteurView(Saboteur saboteurPlayer, String color) {
        this.saboteurPlayer = saboteurPlayer;
        setPreferredSize(new Dimension(saboteurPlayer.getCurrentCell().getMap().getColumns() * 80,
                saboteurPlayer.getCurrentCell().getMap().getRows() * 80));
        setBackground(new Color(0, 0, 0, 0));
        loadImages(color);
    }

    

     /**
     * Loads the images for the saboteur in different orientations based on the specified color.
     *
     * @param color the color of the saboteur (used to determine the image file names)
     */
    private void loadImages(String color) {
        try {
            saboteurDownImage = ImageIO
                    .read(getClass().getResource("/resources/images/saboteur_" + color + "_down.png"));
            saboteurUpImage = ImageIO.read(getClass().getResource("/resources/images/saboteur_" + color + "_up.png"));
            saboteurLeftImage = ImageIO
                    .read(getClass().getResource("/resources/images/saboteur_" + color + "_left.png"));
            saboteurRightImage = ImageIO
                    .read(getClass().getResource("/resources/images/saboteur_" + color + "_right.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        renderSaboteurPlayer(g);
    }

    /**
     * Renders the saboteur player on the panel based on the player's current facing direction.
     *
     * @param g the Graphics context in which to paint
     */
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
