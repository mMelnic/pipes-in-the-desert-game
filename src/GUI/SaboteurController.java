package GUI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import enumerations.Direction;
import player.Saboteur;

public class SaboteurController extends KeyAdapter {
    private Saboteur saboteurPlayer1;
    private SaboteurView saboteurView1;
    private JPanel mapPanel;

    public SaboteurController(Saboteur saboteurPlayer1, JPanel mapPanel) {
        this.saboteurPlayer1 = saboteurPlayer1;
        this.saboteurView1 = new SaboteurView(saboteurPlayer1);
        this.mapPanel = mapPanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_W:
                saboteurPlayer1.move(Direction.UP);
                break;
            case KeyEvent.VK_S:
                saboteurPlayer1.move(Direction.DOWN);
                break;
            case KeyEvent.VK_A:
                saboteurPlayer1.move(Direction.LEFT);
                break;
            case KeyEvent.VK_D:
                saboteurPlayer1.move(Direction.RIGHT);
                break;
            case KeyEvent.VK_Q:
                saboteurPlayer1.puncturePipe();
                break;
            // TODO: Add more key bindings if necessary
        }
        SwingUtilities.invokeLater(() -> mapPanel.repaint());
    }

    public SaboteurView getSaboteurView() {
        return saboteurView1;
    }
}
