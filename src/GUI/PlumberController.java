package GUI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import components.Pipe;
import components.Pump;
import enumerations.Direction;
import player.Plumber;
import system.Cell;

public class PlumberController extends KeyAdapter {
    private Plumber plumberPlayer1;
    private PlumberView plumberView1;
    private JPanel mapPanel;


    public PlumberController(Plumber plumberPlayer1, JPanel mapPanel) {
        this.plumberPlayer1 = plumberPlayer1;
        this.plumberView1 = new PlumberView(plumberPlayer1);
        this.mapPanel = mapPanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                plumberPlayer1.move(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                plumberPlayer1.move(Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                plumberPlayer1.move(Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                plumberPlayer1.move(Direction.RIGHT);
                break;
            case KeyEvent.VK_ENTER:
                attemptRepairAction();
                break;
            case KeyEvent.VK_PERIOD:
                plumberPlayer1.pickComponent();
                break;
            case KeyEvent.VK_SLASH:
                plumberPlayer1.installComponent(plumberPlayer1.getFacingDirection());
                break;
            // TODO: Add more key bindings 
        }
        SwingUtilities.invokeLater(() -> mapPanel.repaint());
    }

    private void attemptRepairAction() {
        // Get the current cell the player is standing on
        Cell currentCell = plumberPlayer1.getCurrentCell();

        // Check if the cell contains a component and attempt repair
        if (currentCell.getComponent() != null) {
            if (currentCell.getComponent() instanceof Pump) {
                plumberPlayer1.repairPump();
            } else if (currentCell.getComponent() instanceof Pipe) {
                plumberPlayer1.repairPipe();
            }
        }
    }

    public PlumberView getPlumberView() {
        return plumberView1;
    }
}
