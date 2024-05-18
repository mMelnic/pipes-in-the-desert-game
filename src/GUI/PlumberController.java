package GUI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import system.Map;

import components.Cistern;
import components.Component;
import components.Pipe;
import components.Pump;
import components.Spring;
import enumerations.Direction;
import player.Plumber;
import system.Cell;

public class PlumberController extends KeyAdapter {
    private Plumber plumberPlayer1;
    private PlumberView plumberView1;
    private JPanel mapPanel;
    private Pipe selectedPipe = null;
    private static final int CELL_SIZE = 80;

    public PlumberController(Plumber plumberPlayer1, JPanel mapPanel) {
        this.plumberPlayer1 = plumberPlayer1;
        this.plumberView1 = new PlumberView(plumberPlayer1);
        this.mapPanel = mapPanel;
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }
        });
    }

    private void handleMousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        int row = mouseY / CELL_SIZE;
        int col = mouseX / CELL_SIZE;

        Map map = plumberPlayer1.getCurrentCell().getMap();
        Cell clickedCell = map.getCells(row, col);

        if (clickedCell != null) {
            Pipe clickedPipe = clickedCell.getComponent() instanceof Pipe ? (Pipe) clickedCell.getComponent() : null;
            Component clickedComponent = clickedCell.getComponent();

            if (clickedPipe != null || clickedComponent instanceof Spring || clickedComponent instanceof Cistern) {
                if (selectedPipe == null) {
                    // First click
                    if (clickedPipe != null) {
                        selectedPipe = clickedPipe;
                    }
                } else {
                    // Second click
                    if (clickedComponent instanceof Spring || clickedComponent instanceof Cistern) {
                        plumberPlayer1.connectPipeWithComponent(selectedPipe, clickedComponent);

                        selectedPipe = null;
                    } else {
                        selectedPipe = null;
                    }
                }
            } else {
                selectedPipe = null;
            }
        }
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
            case KeyEvent.	VK_COMMA:
            plumberPlayer1.dropComponent();
            
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
