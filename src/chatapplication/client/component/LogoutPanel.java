/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.client.component;

import chatapplication.command.Logout;
import chatapplication.execute.Client;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author VuongKM
 */
public class LogoutPanel extends JPanel {

    public JLabel helloMessage;
    public JButton logoutButton;

    public LogoutPanel(Client client) {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        helloMessage = new JLabel("<html>Hello <font color='red'>" + client.currentUser.name + "</font></html>");
        logoutButton = new JButton("Logout");

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Logout logout = new Logout(client.currentUser);
                    client.out.writeObject(logout);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        add(helloMessage);
        add(logoutButton);
    }
}
