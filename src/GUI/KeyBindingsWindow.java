package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * The {@code KeyBindingsWindow} class represents a window that displays the key bindings for the game.
 * It extends {@code JDialog} to provide a modal dialog window.
 */
public class KeyBindingsWindow {
    private JDialog frame;
    private JFrame parentFrame;

    /**
     * Constructs a new {@code KeyBindingsWindow} with the specified parent frame.
     *
     * @param parentFrame the parent frame of this dialog
     */
    public KeyBindingsWindow(JFrame parentFrame) {
        initialize();
        this.parentFrame = parentFrame;
    }

    /**
     * Initializes the contents of the dialog.
     */
    private void initialize() {
        frame = new JDialog(parentFrame, "Key Bindings", true);
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

        JLabel titleLabel = new JLabel("=== KEY BINDINGS ===", SwingConstants.CENTER);
        frame.getContentPane().add(titleLabel);

        JTextArea keyBindingsArea = new JTextArea();
        keyBindingsArea.setSize(new Dimension(450, 600));
        keyBindingsArea.setEditable(false);
        keyBindingsArea.setLineWrap(true);
        keyBindingsArea.setText("Plumbers:\n\n" +
                                "Arrow key up: movement up\n" +
                                "Arrow key right: movement right\n" +
                                "Arrow key down: movement down\n" +
                                "Arrow key left: movement left\n" +
                                "Enter: repair pump/pipe (whichever one stands on)\n" +
                                "slash (/): install carried component\n" +
                                "period (.): pick up a component from a nearby cistern\n" +
                                "comma (,): drop component\n" +
                                "Mouse click on a pipe and then on a nearby cistern/spring: connect the relevant components\n" +
                                "Shift: ACTION state two slash n here please" +
                                "In ACTION state:\n" +
                                "Arrow keys represent directions. If you are standing on a pump, press two arrow keys simultaneously to set up the incoming and outgoing (no difference in logic) pipes of the pump. If you are standing on a pipe, press two arrow key buttons simultaneously to detach pipe from one active component (in one direction) and attach to another active component (in another direction).\n\n" +
                                "Saboteurs:\n\n" +
                                "W: movement up\n" +
                                "D: movement right\n" +
                                "S: movement down\n" +
                                "A: movement left\n" +
                                "Q: puncture pipe");

        JScrollPane scrollPane = new JScrollPane(keyBindingsArea);
        scrollPane.setMinimumSize(new Dimension(450, 600));

        // Add the scroll pane to the frame
        frame.add(scrollPane, BorderLayout.CENTER);

        //frame.getContentPane().add(keyBindingsArea);

        JButton returnButton = new JButton("Return to Main Menu");
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); 
            }
        });
        Dimension buttonSize = new Dimension(150, 30);
        returnButton.setPreferredSize(buttonSize);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(returnButton);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        frame.getContentPane().add(returnButton);
    }

    /**
     * Displays the key bindings window.
     */
    public void show() {
        frame.setVisible(true);
    }
}
