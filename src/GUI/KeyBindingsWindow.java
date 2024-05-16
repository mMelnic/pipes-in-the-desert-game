package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KeyBindingsWindow {
    private JFrame frame;

    public KeyBindingsWindow() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Key Bindings");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

        JLabel titleLabel = new JLabel("=== KEY BINDINGS ===", SwingConstants.CENTER);
        frame.getContentPane().add(titleLabel);

        JTextArea keyBindingsArea = new JTextArea();
        keyBindingsArea.setEditable(false);
        keyBindingsArea.setLineWrap(true);
        keyBindingsArea.setText("Key Bindings:");
        frame.getContentPane().add(keyBindingsArea);

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

    public void show() {
        frame.setVisible(true);
    }
}
