package org.example;

import org.example.Login.LoginInterface;

import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");

        frame.setContentPane(new LoginInterface().getMainPanel());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setResizable(true);
        frame.setVisible(true);
    }
}
