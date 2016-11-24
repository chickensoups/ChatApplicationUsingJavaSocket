/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.client;

import chatapplication.entity.User;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author VuongKM
 */
public class MessageElement extends JPanel {

    public JButton username;
    public JLabel message;

    public MessageElement(User user, String message) {
        this.username = new JButton(user.name);
        this.message = new JLabel(message);

        this.username.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: add logic when click into username button
            }
        });
        add(this.username);
        add(this.message);
    }
}
