// package GUI;

// import javax.swing.*;

// import system.GameManager;

// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;

// public class SandstormView {

//     private JFrame frame;
//     private GameManager gameManager;

//     public SandstormView(GameManager gameManager) {
//         this.gameManager = gameManager;
//         initialize();
//     }

//     private void initialize() {
//         frame = new JFrame();
//         frame.setTitle("Sandstorm Notification");
//         frame.setBounds(100, 100, 450, 300);
//         frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//         frame.setLocationRelativeTo(null);

//         JPanel panel = new JPanel();
//         panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

//         JLabel titleLabel = new JLabel("SANDSTORM INCOMING!", JLabel.CENTER);
//         titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

//         panel.add(Box.createVerticalStrut(10));
//         for(int i = 0; i < 5; i++){
//             panel.add(titleLabel);
//             //system.sleep(500);
//             panel.remove(titleLabel);
//             system.sleep(500);
//         }

//         frame.getContentPane().add(panel);
//     }

//     public void show() {
//         frame.setVisible(true);
//         System.sleep(60000);
//         frame.close();
//     }
// }