package org.example.ADMIN;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Login.LoginInterface;
import org.example.Utils.EROLE;
import org.example.Utils.Employee;
import org.example.Utils.EmploymentStatus;
import org.example.Utils.Login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AdminInterface {
    private JPanel mainPanel;
    private JTable employeeTable;
    private JButton addEmployeeButton;
    private JButton deleteEmployeeButton;
    private JButton updateEmployeeButton;
    private JButton logoutButton;
    private JDialog addEmployeeDialog;
    private JTextField fullNameField, employeeIdField, jobTitleField, departmentField, contactInfoField, addressField , usernameField,passwordField;
    private JComboBox<String> employmentStatusComboBox;
    private JComboBox<String> employmentRoleComboBox;

    public AdminInterface() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel welcomePanel = new JPanel();
        JLabel label = new JLabel("Welcome to ADMIN Dashboard", JLabel.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 24));
        welcomePanel.add(label);

        mainPanel.add(welcomePanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addEmployeeButton = new JButton("Add Employee");
        deleteEmployeeButton = new JButton("Delete Employee");
        updateEmployeeButton = new JButton("Update Employee");
        logoutButton = new JButton("Logout");

        Dimension buttonSize = new Dimension(120, 30);
        addEmployeeButton.setPreferredSize(buttonSize);
        deleteEmployeeButton.setPreferredSize(buttonSize);
        updateEmployeeButton.setPreferredSize(buttonSize);
        logoutButton.setPreferredSize(buttonSize);

        logoutButton.setBackground(Color.ORANGE);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);

        buttonPanel.add(addEmployeeButton);
        buttonPanel.add(deleteEmployeeButton);
        buttonPanel.add(updateEmployeeButton);
        buttonPanel.add(logoutButton);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.add(searchPanel);
        actionPanel.add(buttonPanel);

        mainPanel.add(actionPanel, BorderLayout.CENTER);

        String[] columns = {"ID", "Full Name", "Employee ID", "Job Title", "Department", "Employment Status", "Contact Info", "Address","User Name","Role"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        employeeTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(employeeTable);

        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = searchField.getText().toLowerCase().trim();
                filterTable(query);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                frame.dispose();
                openLOGINInterface();
            }
        });

        addEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddEmployeeForm();
            }
        });

        deleteEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDeleteEmployeeDialog();
            }
        });

        updateEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUpdateEmployeeDialog();
            }
        });

        loadEmployees();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void filterTable(String query) {
        DefaultTableModel tableModel = (DefaultTableModel) employeeTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        employeeTable.setRowSorter(sorter);

        if (query.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
        }
    }
    private void openLOGINInterface() {
        JFrame hrFrame = new JFrame("HR Dashboard");
        hrFrame.setContentPane(new LoginInterface().getMainPanel());
        hrFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hrFrame.setSize(800, 600);
        hrFrame.setResizable(true);
        hrFrame.setVisible(true);
    }
    private void showAddEmployeeForm() {
        addEmployeeDialog = new JDialog();
        addEmployeeDialog.setTitle("Add Employee");
        addEmployeeDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        addEmployeeDialog.add(new JLabel("Full Name:"), gbc);
        fullNameField = new JTextField(20);
        gbc.gridx = 1;
        addEmployeeDialog.add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        addEmployeeDialog.add(new JLabel("Employee ID:"), gbc);
        employeeIdField = new JTextField(20);
        gbc.gridx = 1;
        addEmployeeDialog.add(employeeIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addEmployeeDialog.add(new JLabel("Job Title:"), gbc);
        jobTitleField = new JTextField(20);
        gbc.gridx = 1;
        addEmployeeDialog.add(jobTitleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        addEmployeeDialog.add(new JLabel("Department:"), gbc);
        departmentField = new JTextField(20);
        gbc.gridx = 1;
        addEmployeeDialog.add(departmentField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        addEmployeeDialog.add(new JLabel("Contact Info:"), gbc);
        contactInfoField = new JTextField(20);
        gbc.gridx = 1;
        addEmployeeDialog.add(contactInfoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        addEmployeeDialog.add(new JLabel("Address:"), gbc);
        addressField = new JTextField(20);
        gbc.gridx = 1;
        addEmployeeDialog.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        addEmployeeDialog.add(new JLabel("Employment Status:"), gbc);
        String[] statuses = {"ACTIVE", "INACTIVE", "ON_LEAVE", "TERMINATED", "RESIGNED"};
        employmentStatusComboBox = new JComboBox<>(statuses);
        gbc.gridx = 1;
        addEmployeeDialog.add(employmentStatusComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        addEmployeeDialog.add(new JLabel("UserName:"), gbc);
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        addEmployeeDialog.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        addEmployeeDialog.add(new JLabel("Password:"), gbc);
        passwordField = new JTextField(20);
        gbc.gridx = 1;
        addEmployeeDialog.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        addEmployeeDialog.add(new JLabel("Employment Roles:"), gbc);
        String[] roles = {"RH", "ADMIN", "MANAGER", "EMPLOYEE"};
        employmentRoleComboBox = new JComboBox<>(roles);
        gbc.gridx = 1;
        addEmployeeDialog.add(employmentRoleComboBox, gbc);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveEmployee();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.CENTER;
        addEmployeeDialog.add(saveButton, gbc);

        addEmployeeDialog.pack();
        addEmployeeDialog.setVisible(true);
    }
    private void showDeleteEmployeeDialog() {
        String employeeIdString = JOptionPane.showInputDialog(null, "Enter Employee ID to delete:");

        if (employeeIdString != null && !employeeIdString.isEmpty()) {
            try {
                Long employeeId = Long.parseLong(employeeIdString);
                deleteEmployee(employeeId);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Employee ID");
            }
        }
    }
    private void showUpdateEmployeeDialog() {
        String employeeIdString = JOptionPane.showInputDialog(null, "Enter Employee ID to update:");

        if (employeeIdString != null && !employeeIdString.isEmpty()) {
            try {
                Long employeeId = Long.parseLong(employeeIdString);
                loadEmployeeForUpdate(employeeId);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Employee ID");
            }
        }
    }

    private void loadEmployeeForUpdate(Long employeeId) {
        Employee employee = getEmployeeFromAPI(employeeId);

        if (employee != null) {
            fullNameField.setText(employee.getFullName());
            employeeIdField.setText(employee.getEmployeeId());
            jobTitleField.setText(employee.getJobTitle());
            departmentField.setText(employee.getDepartment());
            contactInfoField.setText(employee.getContactInfo());
            addressField.setText(employee.getAddress());
            employmentStatusComboBox.setSelectedItem(employee.getEmploymentStatus().name());
            usernameField.setText(employee.getLogin().getUsername());
            employmentRoleComboBox.setSelectedItem(employee.getLogin().getRole().name());

            addEmployeeDialog.setTitle("Update Employee");

            for (Component component : addEmployeeDialog.getContentPane().getComponents()) {
                if (component instanceof JButton && ((JButton) component).getText().equals("Save")) {
                    addEmployeeDialog.getContentPane().remove(component);
                }
            }

            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateEmployee(employeeId);
                    addEmployeeDialog.dispose();
                }
            });

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridy = 10;
            gbc.anchor = GridBagConstraints.CENTER;
            addEmployeeDialog.add(saveButton, gbc);

            addEmployeeDialog.revalidate();
            addEmployeeDialog.repaint();

            addEmployeeDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Employee not found!");
        }
    }

    private Employee getEmployeeFromAPI(Long employeeId) {
        Employee employee = null;
        try {
            URL url = new URL("http://localhost:8080/api/employees/" + employeeId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Gson gson = new Gson();
                employee = gson.fromJson(response.toString(), Employee.class);
            } else {
                JOptionPane.showMessageDialog(null, "Employee not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employee;
    }

    private void saveEmployee() {
        String fullName = fullNameField.getText().trim();
        String employeeId = employeeIdField.getText().trim();
        String jobTitle = jobTitleField.getText().trim();
        String department = departmentField.getText().trim();
        String contactInfo = contactInfoField.getText().trim();
        String address = addressField.getText().trim();
        String employmentStatusString = (String) employmentStatusComboBox.getSelectedItem();
        String username= usernameField.getText().trim();
        String password= passwordField.getText().trim();
        String employeeRole= (String) employmentRoleComboBox.getSelectedItem();

        if (fullName.isEmpty() || employeeId.isEmpty() || jobTitle.isEmpty() || department.isEmpty() || contactInfo.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all the fields.");
            return;
        }

        if (employmentStatusString == null || employmentStatusString.trim().isEmpty()) {
            employmentStatusString = "ACTIVE";
        }

        if (employeeRole == null || employeeRole.trim().isEmpty()) {
            employmentStatusString = "EMPLOYEE";
        }

        EmploymentStatus employmentStatus = EmploymentStatus.valueOf(employmentStatusString);
        EROLE erole = EROLE.valueOf(employeeRole);
        Employee employee = new Employee(fullName, employeeId, jobTitle, department, new java.util.Date(), employmentStatus, contactInfo, address);

        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String hireDateFormatted = dateFormat.format(employee.getHireDate());

        Login login = new Login(null, username, password, erole);

        String jsonInputString = "{"
                + "\"employee\": {"
                + "\"fullName\": \"" + employee.getFullName() + "\","
                + "\"employeeId\": \"" + employee.getEmployeeId() + "\","
                + "\"jobTitle\": \"" + employee.getJobTitle() + "\","
                + "\"department\": \"" + employee.getDepartment() + "\","
                + "\"employmentStatus\": \"" + employee.getEmploymentStatus() + "\","
                + "\"contactInfo\": \"" + employee.getContactInfo() + "\","
                + "\"address\": \"" + employee.getAddress() + "\","
                + "\"hireDate\": \"" + hireDateFormatted + "\""
                + "},"
                + "\"login\": {"
                + "\"username\": \"" + login.getUsername() + "\","
                + "\"password\": \"" + login.getPassword() + "\","
                + "\"role\": \"" + login.getRole().name() + "\""
                + "}"
                + "}";

        try {
            URL url = new URL("http://localhost:8080/api/employees/addEmployee");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStream os = connection.getOutputStream();
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                JOptionPane.showMessageDialog(null, "Employee added successfully");
                addEmployeeDialog.dispose();
                loadEmployees();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add employee");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while adding employee");
        }
    }

    private void loadEmployees() {
        java.util.List<Employee> employees = getEmployeesFromAPI();
        DefaultTableModel tableModel = (DefaultTableModel) employeeTable.getModel();
        tableModel.setRowCount(0);

        for (Employee employee : employees) {
            Object[] rowData = {
                    employee.getId(),
                    employee.getFullName(),
                    employee.getEmployeeId(),
                    employee.getJobTitle(),
                    employee.getDepartment(),
                    employee.getEmploymentStatus(),
                    employee.getContactInfo(),
                    employee.getAddress(),
                    employee.getLogin().getUsername(),
                    employee.getLogin().getRole()
            };

            tableModel.addRow(rowData);
        }
    }


    private java.util.List<Employee> getEmployeesFromAPI() {
        java.util.List<Employee> employeeList = new ArrayList<>();
        try {
            URL url = new URL("http://localhost:8080/api/employees");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gson = new Gson();
            employeeList = gson.fromJson(response.toString(), new TypeToken<List<Employee>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    private void deleteEmployee(Long employeeId) {
        System.out.println("Attempting to delete employee with ID: " + employeeId);

        try {
            URL url = new URL("http://localhost:8080/api/employees/" + employeeId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(null, "Employee deleted successfully");
                loadEmployees();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete employee");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while deleting employee");
        }
    }

    private void updateEmployee(Long employeeId) {
        Employee updatedEmployee = new Employee();
        Login login =new Login();
        updatedEmployee.setFullName(fullNameField.getText());
        updatedEmployee.setEmployeeId(employeeIdField.getText());
        updatedEmployee.setJobTitle(jobTitleField.getText());
        updatedEmployee.setDepartment(departmentField.getText());
        updatedEmployee.setContactInfo(contactInfoField.getText());
        updatedEmployee.setAddress(addressField.getText());
        updatedEmployee.setEmploymentStatus(EmploymentStatus.valueOf((String) employmentStatusComboBox.getSelectedItem()));

        try {
            URL url = new URL("http://localhost:8080/api/employees/" + employeeId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            Gson gson = new Gson();
            String jsonInputString = gson.toJson(updatedEmployee);

            OutputStream os = connection.getOutputStream();
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(null, "Employee updated successfully");
                loadEmployees();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update employee");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while updating employee");
        }
    }
}
