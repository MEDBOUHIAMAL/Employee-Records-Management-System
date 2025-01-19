package org.example.Login;

import org.example.ADMIN.AdminInterface;
import org.example.HR.HRInterface;
import org.example.Manager.ManagerInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginInterface {
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginInterface() {
        mainPanel = new JPanel();
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (authenticate(username, password)) {
                    JOptionPane.showMessageDialog(null, "Login successful");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private boolean authenticate(String username, String password) {
        try {
            URL url = new URL("http://localhost:8080/api/auth/login");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String data = "username=" + username + "&password=" + password;
            OutputStream os = connection.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String responseString = response.toString();

                String role = null;
                String department = null;

                if (responseString.contains("Role:") && responseString.contains("and Department:")) {
                    role = responseString.substring(
                            responseString.indexOf("Role:") + 6,
                            responseString.indexOf("and Department:")).trim();

                    department = responseString.substring(
                            responseString.indexOf("and Department:") + 15).trim();
                }

                System.out.println("Role: " + role);
                System.out.println("Department: " + department);

                JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                loginFrame.dispose();
                switch (role) {
                    case "RH":
                        openHRInterface();
                        break;
                    case "MANAGER":
                        openManagerInterface(department);
                        break;
                    case "ADMIN":
                        openAdminInterface();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Unknown role", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                }

                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    private void openHRInterface() {
        JFrame hrFrame = new JFrame("HR Dashboard");
        hrFrame.setContentPane(new HRInterface().getMainPanel());
        hrFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hrFrame.setSize(800, 600);
        hrFrame.setResizable(true);
        hrFrame.setVisible(true);
    }

    private void openManagerInterface(String department) {
        JFrame managerFrame = new JFrame("Manager Dashboard");
        managerFrame.setContentPane(new ManagerInterface(department).getMainPanel());
        managerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managerFrame.setSize(800, 600);
        managerFrame.setResizable(true);
        managerFrame.setVisible(true);
    }

    private void openAdminInterface() {
        JFrame adminFrame = new JFrame("Admin Dashboard");
        adminFrame.setContentPane(new AdminInterface().getMainPanel());
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.setSize(800, 600);
        adminFrame.setResizable(true);
        adminFrame.setVisible(true);
    }
    public JPanel getMainPanel() {
        return mainPanel;
    }
}

