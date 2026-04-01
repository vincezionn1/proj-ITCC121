import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class dashboard extends JFrame {

    // ── In-memory employee store ──────────────────────────────────────────────
    static ArrayList<String[]> employees = new ArrayList<>();

    static {
        employees.add(new String[]{"1",  "Juan dela Cruz",   "Present", "$5,000",  "Paid"});
        employees.add(new String[]{"2",  "Maria Santos",     "Present", "$6,000",  "Pending"});
        employees.add(new String[]{"3",  "Pedro Reyes",      "Absent",  "$4,500",  "Paid"});
        employees.add(new String[]{"4",  "Ana Gonzales",     "Present", "$7,000",  "Pending"});
        employees.add(new String[]{"5",  "Jose Villanueva",  "Present", "$5,500",  "Paid"});
        employees.add(new String[]{"6",  "Rosa Fernandez",   "Present", "$6,500",  "Paid"});
        employees.add(new String[]{"7",  "Carlo Mendoza",    "Absent",  "$4,000",  "Pending"});
        employees.add(new String[]{"8",  "Liza Bautista",    "Present", "$5,200",  "Paid"});
        employees.add(new String[]{"9",  "Ramon Cruz",       "Present", "$4,800",  "Paid"});
        employees.add(new String[]{"10", "Elena Ramos",      "Present", "$5,500",  "Paid"});
    }

    // ── Widgets ───────────────────────────────────────────────────────────────
    private JLabel totalEmployeesVal, pendingPaymentsVal, attendanceTodayVal, totalSalaryVal;
    private JTextField searchEmployeeTextField;
    private JButton addEmployeeButton, logoutButton, viewPayrollButton;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
private JPanel box;
    private static final String[] COLUMNS = {"ID", "Name", "Attendance", "Salary", "Payment Status"};

    public dashboard() {
        setTitle("Dashboard - Payroll System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 580);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(43, 45, 48));

        add(buildTopPanel(),    BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        refreshStats();
        wireActions();
    }

    // ── TOP: summary records ──────────────────────────────────────────────────
    private JPanel buildTopPanel() {
        JPanel box = new JPanel(new GridLayout(4, 2, 0, 0));
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        box.add(headerLabel("Records:"));
        box.add(new JLabel());

        box.add(plainLabel("Total Employees:"));
        totalEmployeesVal = valueLabel("0");
        box.add(totalEmployeesVal);

        box.add(plainLabel("Pending Payments:"));
        pendingPaymentsVal = valueLabel("0");
        box.add(pendingPaymentsVal);

        box.add(plainLabel("Attendance Today:"));
        attendanceTodayVal = valueLabel("0");
        box.add(attendanceTodayVal);

        box.add(plainLabel("Total Salary Paid:"));
        totalSalaryVal = valueLabel("$0");
        box.add(totalSalaryVal);

        return box;
    }

    // ── CENTER: search bar + table + side buttons ─────────────────────────────
    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new BorderLayout(6, 6));
        center.setBackground(new Color(43, 45, 48));
        center.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        // Search bar
        searchEmployeeTextField = new JTextField("[Search Employee...]");
        searchEmployeeTextField.setForeground(Color.GRAY);
        searchEmployeeTextField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchEmployeeTextField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        center.add(searchEmployeeTextField, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        employeeTable = new JTable(tableModel);
        employeeTable.setRowHeight(26);
        employeeTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        employeeTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        employeeTable.setSelectionBackground(new Color(100, 149, 237));
        employeeTable.setGridColor(new Color(210, 210, 210));

        // Color-code payment status
        employeeTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                                                           boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if ("Pending".equals(v)) setForeground(new Color(200, 60, 60));
                else                     setForeground(new Color(34, 139, 34));
                return this;
            }
        });

        loadTable(employees);

        JScrollPane scroll = new JScrollPane(employeeTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        center.add(scroll, BorderLayout.CENTER);

        // Side buttons
        addEmployeeButton = styledButton("Add Employee", new Color(70, 130, 180));
        logoutButton      = styledButton("Logout",       new Color(180, 60, 60));

        JPanel side = new JPanel(new GridLayout(2, 1, 0, 8));
        side.setBackground(new Color(43, 45, 48));
        side.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
        side.add(addEmployeeButton);
        side.add(logoutButton);
        center.add(side, BorderLayout.EAST);

        return center;
    }

    // ── BOTTOM: VIEW PAYROLL ──────────────────────────────────────────────────
    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(43, 45, 48));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        viewPayrollButton = styledButton("VIEW PAYROLL", new Color(60, 60, 60));
        viewPayrollButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        viewPayrollButton.setPreferredSize(new Dimension(0, 38));
        panel.add(viewPayrollButton, BorderLayout.CENTER);

        return panel;
    }

    // ── Wire all actions ──────────────────────────────────────────────────────
    private void wireActions() {

        // Search: live filter
        searchEmployeeTextField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (searchEmployeeTextField.getText().equals("[Search Employee...]")) {
                    searchEmployeeTextField.setText("");
                    searchEmployeeTextField.setForeground(Color.BLACK);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (searchEmployeeTextField.getText().isEmpty()) {
                    searchEmployeeTextField.setText("[Search Employee...]");
                    searchEmployeeTextField.setForeground(Color.GRAY);
                    loadTable(employees);
                }
            }
        });

        searchEmployeeTextField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String query = searchEmployeeTextField.getText().trim().toLowerCase();
                if (query.isEmpty() || query.equals("[search employee...]")) {
                    loadTable(employees);
                    return;
                }
                ArrayList<String[]> filtered = new ArrayList<>();
                for (String[] emp : employees) {
                    if (emp[0].toLowerCase().contains(query) ||
                            emp[1].toLowerCase().contains(query)) {
                        filtered.add(emp);
                    }
                }
                loadTable(filtered);
            }
        });

        // Add Employee
        addEmployeeButton.addActionListener(e -> showAddEmployeeDialog());


        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this, "Are you sure you want to logout?",
                    "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new login().setVisible(true);
                dispose();
            }
        });


        viewPayrollButton.addActionListener(e -> {
            new payroll().setVisible(true);    // ← opens payroll form
            dispose();
        });
    }

    // ── Add Employee dialog ───────────────────────────────────────────────────
    private void showAddEmployeeDialog() {
        JTextField nameField       = new JTextField();
        JComboBox<String> attCombo = new JComboBox<>(new String[]{"Present", "Absent"});
        JTextField salaryField     = new JTextField();
        JComboBox<String> payCombo = new JComboBox<>(new String[]{"Paid", "Pending"});

        Object[] fields = {
                "Name:",                  nameField,
                "Attendance:",            attCombo,
                "Salary (e.g. 5000):",    salaryField,
                "Payment Status:",        payCombo
        };

        int result = JOptionPane.showConfirmDialog(
                this, fields, "Add New Employee", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name   = nameField.getText().trim();
            String salary = salaryField.getText().trim();

            if (name.isEmpty() || salary.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Salary cannot be empty.");
                return;
            }
            if (!salary.startsWith("$")) salary = "$" + salary;

            String id = String.valueOf(employees.size() + 1);
            employees.add(new String[]{
                    id, name,
                    (String) attCombo.getSelectedItem(),
                    salary,
                    (String) payCombo.getSelectedItem()
            });

            loadTable(employees);
            refreshStats();
            JOptionPane.showMessageDialog(this, "Employee \"" + name + "\" added successfully!");
        }
    }

    // ── Refresh summary stats ─────────────────────────────────────────────────
    private void refreshStats() {
        int present = 0, pending = 0;
        double salaryPaid = 0;
        for (String[] emp : employees) {
            if ("Present".equals(emp[2])) present++;
            if ("Pending".equals(emp[4])) pending++;
            if ("Paid".equals(emp[4])) {
                try {
                    salaryPaid += Double.parseDouble(emp[3].replace("$", "").replace(",", ""));
                } catch (NumberFormatException ignored) {}
            }
        }
        totalEmployeesVal.setText(String.valueOf(employees.size()));
        pendingPaymentsVal.setText(String.valueOf(pending));
        attendanceTodayVal.setText(String.valueOf(present));
        totalSalaryVal.setText(String.format("$%,.0f", salaryPaid));
    }

    // ── Load rows into table ──────────────────────────────────────────────────
    private void loadTable(ArrayList<String[]> data) {
        tableModel.setRowCount(0);
        for (String[] row : data) tableModel.addRow(row);
    }

    // ── Label helpers ─────────────────────────────────────────────────────────
    private JLabel headerLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        return l;
    }
    private JLabel plainLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return l;
    }
    private JLabel valueLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        return l;
    }

    // ── Button helper ─────────────────────────────────────────────────────────
    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(new Color(223, 225, 229));
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new dashboard().setVisible(true));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
