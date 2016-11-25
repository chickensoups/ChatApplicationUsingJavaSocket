/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.client.component;

import chatapplication.client.util.DecorationUtil;
import chatapplication.command.Login;
import chatapplication.entity.User;
import chatapplication.execute.Client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public LoginPanel(Client client) {
        usernameTextField = new JTextField(50);
        loginButton = new JButton("Login");

        add(usernameTextField);
        add(loginButton);

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

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    client.currentUser = new User(client.uniqueId, usernameTextField.getText());
                    Login login = new Login(client.currentUser);
                    client.out.writeObject(login);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        loginButton.setEnabled(false);

        DecorationUtil.setComponentNiceBorder(this, "Login form");
    }
}
