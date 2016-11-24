/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.client.component;

import chatapplication.client.util.DecorationUtil;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author VuongKM
 */
public class LoginPanel extends JPanel {

    public JTextField usernameTextField;
    public JButton loginButton;
    public JButton logoutButton;

    public LoginPanel() {
        usernameTextField = new JTextField(50);
        loginButton = new JButton("Login");
        logoutButton = new JButton("Logout");

        add(usernameTextField);
        add(loginButton);
        add(logoutButton);

        usernameTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (usernameTextField.getText().isEmpty()) {
                    loginButton.setEnabled(false);
                } else {
                    loginButton.setEnabled(true);
                }
            }
        });

        loginButton.setEnabled(false);
        logoutButton.setEnabled(false);

        DecorationUtil.setComponentNiceBorder(this, "Login form");
    }

    public void loggedIn() {
        loginButton.setEnabled(false);
        usernameTextField.setEnabled(false);
        logoutButton.setEnabled(true);
        validate();
        repaint();
    }

    public void loggedOut() {
        logoutButton.setEnabled(false);
        loginButton.setEnabled(true);
        usernameTextField.setEnabled(true);
        validate();
        repaint();
    }
}
