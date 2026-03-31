import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class login extends JFrame {
    private JPanel panel1;
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginbutton;

    public login() {
        setContentPane(panel1);
        setTitle("Login - Payroll System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null); // Centers the window

        loginbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                // Get password safely
                String password = new String(passField.getPassword());

                // Simple check (You can change "admin" to whatever you like)
                if (username.equals("Mibenstev") && password.equals("msv1234")) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");

                    // Open the payroll window
                    new payroll().setVisible(true);

                    // Close the login window
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                }
            }
        });
    }

    public static void main(String[] args) {
        new login().setVisible(true);
    }
}
