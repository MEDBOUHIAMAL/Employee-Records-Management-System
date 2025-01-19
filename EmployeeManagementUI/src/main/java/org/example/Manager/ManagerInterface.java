package org.example.Manager;


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

public class ManagerInterface {
    private JPanel mainPanel;
    private JTable employeeTable;
    private String department;
    private JButton updateEmployeeButton;
    private JButton logoutButton;
    private JDialog addEmployeeDialog;
    private JComboBox<String> employmentStatusComboBox;

    public ManagerInterface(String department) {
        this.department = department;
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel welcomePanel = new JPanel();
        JLabel label = new JLabel("Welcome to Manager Dashboard", JLabel.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 24));
        welcomePanel.add(label);

        mainPanel.add(welcomePanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        updateEmployeeButton = new JButton("Update Employee");
        logoutButton = new JButton("Logout");

        Dimension buttonSize = new Dimension(120, 30);
        updateEmployeeButton.setPreferredSize(buttonSize);
        logoutButton.setPreferredSize(buttonSize);

        logoutButton.setBackground(Color.ORANGE);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);


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

        String[] columns = {"ID", "Full Name", "Job Title", "Department", "Employment Status"};
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
            addEmployeeDialog = new JDialog();
            addEmployeeDialog.setTitle("Update Employee");
            addEmployeeDialog.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            addEmployeeDialog.add(new JLabel("Employment Status:"), gbc);

            String[] statuses = {"ACTIVE", "INACTIVE", "ON_LEAVE", "TERMINATED", "RESIGNED"};
            employmentStatusComboBox = new JComboBox<>(statuses);
            employmentStatusComboBox.setSelectedItem(employee.getEmploymentStatus().name());
            gbc.gridx = 1;
            addEmployeeDialog.add(employmentStatusComboBox, gbc);

            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateEmployee(employeeId);
                }
            });
            gbc.gridx = 1;
            gbc.gridy = 1;
            addEmployeeDialog.add(saveButton, gbc);

            addEmployeeDialog.pack();
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



    private void loadEmployees() {
        java.util.List<Employee> employees = getEmployeesByDepartment(department);
        DefaultTableModel tableModel = (DefaultTableModel) employeeTable.getModel();
        tableModel.setRowCount(0); // Clear previous rows

        for (Employee employee : employees) {
            Object[] rowData = {
                    employee.getId(),
                    employee.getFullName(),

                    employee.getJobTitle(),
                    employee.getDepartment(),
                    employee.getEmploymentStatus(),

            };

            tableModel.addRow(rowData);
        }
    }

    private List<Employee> getEmployeesByDepartment(String department) {
        List<Employee> employeeList = new ArrayList<>();
        try {
            URL url = new URL("http://localhost:8080/api/employees/department?department=" + department);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Gson gson = new Gson();
                employeeList = gson.fromJson(response.toString(), new TypeToken<List<Employee>>() {}.getType());
            } else {
                JOptionPane.showMessageDialog(null, "Failed to fetch employees for the department");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while fetching employees for the department");
        }
        return employeeList;
    }

    private void updateEmployee(Long employeeId) {
        try {
            String jsonInputString = "{ \"employmentStatus\": \"" + employmentStatusComboBox.getSelectedItem() + "\" }";

            URL url = new URL("http://localhost:8080/api/employees/manager/" + employeeId + "?managerDepartment=" + department);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStream os = connection.getOutputStream();
            os.write(jsonInputString.getBytes("utf-8"));
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(null, "Employment status updated successfully");
                addEmployeeDialog.dispose();
                loadEmployees();
            } else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                JOptionPane.showMessageDialog(null, "You are not allowed to update this employee", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update employment status");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while updating employment status");
        }
    }


}

