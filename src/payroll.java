import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class payroll extends JFrame {
    private JPanel box;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton button1;
    private JButton backButton;

    public payroll() {
        setTitle("Payroll Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 320);
        setLocationRelativeTo(null);

        // Build UI manually since we're not using .form binding here
        buildUI();

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculatePayroll();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Go back to dashboard
                new dashboard().setVisible(true);
                dispose();
            }
        });
    }

    private void buildUI() {
        JPanel main = new JPanel(new GridBagLayout());
        main.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        main.setBackground(new Color(43, 45, 48));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Color fg = new Color(223, 225, 229);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 13);

        // Title
        JLabel title = new JLabel("Payroll Calculator", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(fg);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        main.add(title, gbc);

        gbc.gridwidth = 1;

        // Row helper
        textField1 = addRow(main, gbc, fg, labelFont, "Employee Name:", 1);
        textField2 = addRow(main, gbc, fg, labelFont, "Position:",       2);
        textField3 = addRow(main, gbc, fg, labelFont, "Gross Salary ($):", 3);
        textField4 = addRow(main, gbc, fg, labelFont, "Tax / Deductions ($):", 4);

        // Buttons
        button1 = new JButton("Calculate");
        button1.setBackground(new Color(70, 130, 180));
        button1.setForeground(fg);
        button1.setFocusPainted(false);
        button1.setFont(new Font("SansSerif", Font.BOLD, 13));

        backButton = new JButton("← Back to Dashboard");
        backButton.setBackground(new Color(80, 80, 80));
        backButton.setForeground(fg);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(new Color(43, 45, 48));
        btnPanel.add(backButton);
        btnPanel.add(button1);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(14, 6, 6, 6);
        main.add(btnPanel, gbc);

        setContentPane(main);
    }

    private JTextField addRow(JPanel panel, GridBagConstraints gbc,
                              Color fg, Font font, String labelText, int row) {
        JLabel label = new JLabel(labelText);
        label.setForeground(fg);
        label.setFont(font);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        panel.add(label, gbc);

        JTextField field = new JTextField();
        field.setFont(font);
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(field, gbc);

        return field;
    }

    private void calculatePayroll() {
        try {
            String name     = textField1.getText().trim();
            String position = textField2.getText().trim();
            double salary   = Double.parseDouble(textField3.getText().trim());
            double tax      = Double.parseDouble(textField4.getText().trim());
            double netSalary = salary - tax;

            JOptionPane.showMessageDialog(this,
                    "Employee: " + name + " (" + position + ")" +
                            "\nGross Salary:      $" + String.format("%,.2f", salary) +
                            "\nDeductions (Tax):  $" + String.format("%,.2f", tax) +
                            "\n───────────────────────" +
                            "\nNet Salary:        $" + String.format("%,.2f", netSalary),
                    "Payroll Result", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: Please ensure Salary and Tax contain only numbers.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new payroll().setVisible(true));
    }
}
