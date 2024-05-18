package GUI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.event.MouseEvent;

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
    private Cell firstClickedCell; // Variable to store the first clicked cell

    public PlumberController(Plumber plumberPlayer1, JPanel mapPanel) {
        this.plumberPlayer1 = plumberPlayer1;
        this.plumberView1 = new PlumberView(plumberPlayer1);
        this.mapPanel = mapPanel;
        // Add mouse listener to handle mouse clicks
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e);
            }
        });
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

    // Method to handle mouse clicks
private void handleClick(MouseEvent e) {
    // Step a: Get mouse coordinates of two clicks in a row
    int mouseX = e.getX();
    int mouseY = e.getY();

    // Step b: Translate to cell coordinates
    int cellRow = mouseY / 80;
    int cellColumn = mouseX / 80;

    // Step c: Get the clicked cells
    Cell clickedCell = plumberPlayer1.getCurrentCell().getMap().getCells(cellRow, cellColumn);

    // Step d: Retrieve the components on the map
    Component clickedComponent = clickedCell.getComponent();

    // Step e: Validate selection
    if (clickedComponent instanceof Pipe || clickedComponent instanceof Spring || clickedComponent instanceof Cistern) {
        if (firstClickedCell == null) {
            // This is the first click, store the first clicked cell
            firstClickedCell = clickedCell;
        } else {
            // This is the second click
            // Check if the second clicked cell also has one of the specified components
            if (clickedComponent instanceof Pipe || clickedComponent instanceof Spring || clickedComponent instanceof Cistern) {
                // Both first and second clicked cells have valid components
                // Do something with the first and second clicked cells
                // For example, print their row and column indices
                System.out.println("First Clicked Cell: (" + firstClickedCell.getRow() + ", " + firstClickedCell.getColumn() + ")");
                System.out.println("Second Clicked Cell: (" + clickedCell.getRow() + ", " + clickedCell.getColumn() + ")");
            } else {
                // The second clicked cell does not have a valid component
                // Reset the first clicked cell
                firstClickedCell = null;
                // Optionally, provide feedback to the user that the selection is invalid
            }
        }
    }
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
