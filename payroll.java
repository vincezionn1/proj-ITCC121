import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class payroll extends JFrame {
    private JPanel box;
    private JTextField textField1; // Assuming this is Full Name
    private JTextField textField2; // Assuming this is Monthly Salary
    private JTextField textField3; // Position (Add if missing)
    private JTextField textField4; // Tax (Add if missing)
    private JButton button1; // Rename your "Button" in the designer

    public payroll() {
        setContentPane(box);
        setTitle("Payroll Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); // Adjusts window size to fit components
        setLocationRelativeTo(null); // Centers it on screen


        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculatePayroll();
            }
        });
    }

    private void calculatePayroll() {
        try {
            // .trim() removes any accidental spaces you might have typed
            String name = textField1.getText().trim();
            String position = textField2.getText().trim();

            // Convert the text to numbers
            double salary = Double.parseDouble(textField3.getText().trim());
            double tax = Double.parseDouble(textField4.getText().trim());

            double netSalary = salary - tax;

            JOptionPane.showMessageDialog(this,
                    "Employee: " + name + " (" + position + ")" +
                            "\nGross Salary: " + salary +
                            "\nDeductions (Tax): " + tax +
                            "\n-----------------------" +
                            "\nNet Salary: " + netSalary);

        } catch (NumberFormatException ex) {
            // This triggers if any field is empty or contains letters instead of numbers
            JOptionPane.showMessageDialog(this, "Error: Please ensure Salary and Tax contain only numbers.");
        }
    }

    static void main(String[] args) {
        payroll frame = new payroll();
        frame.setVisible(true);

    }
    }