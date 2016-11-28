/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.client.component;

import chatapplication.client.util.DecorationUtil;
import chatapplication.command.LeaveRoom;
import chatapplication.command.SendMessage;
import chatapplication.execute.Client;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author VuongKM
 */
public class InRoomPanel extends JPanel {

    public JPanel messagePanel;
    public JTextField messageTextField;
    public JButton sendMessageButton;
    public JButton leaveRoomButton;

    public JPanel chatArea;

    public InRoomPanel(Client client) {
        messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        messageTextField = new JTextField(50);
        messageTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    SendMessage sendMessage = new SendMessage(client.currentUser, messageTextField.getText());
                    client.out.writeObject(sendMessage);
                    messageTextField.setText("");
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        messageTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (messageTextField.getText().isEmpty()) {
                    sendMessageButton.setEnabled(false);
                } else {
                    sendMessageButton.setEnabled(true);
                }
            }
        });

        sendMessageButton = new JButton("Send");
        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SendMessage sendMessage = new SendMessage(client.currentUser, messageTextField.getText());
                    client.out.writeObject(sendMessage);
                    messageTextField.setText("");
                    sendMessageButton.setEnabled(false);
                } catch (IOException ex) {
                    Logger.getLogger(InRoomPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        leaveRoomButton = new JButton("Leave");
        leaveRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    LeaveRoom leaveRoom = new LeaveRoom(client.currentUser, client.currentUser.currentRoom);
                    client.out.writeObject(leaveRoom);
                } catch (IOException ex) {
                    Logger.getLogger(InRoomPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        messagePanel.add(messageTextField);
        messagePanel.add(sendMessageButton);
        messagePanel.add(leaveRoomButton);

        setLayout(new BorderLayout());
        chatArea = new JPanel();
        chatArea.setLayout(new GridLayout(100, 1));
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(messagePanel, BorderLayout.PAGE_END);

        DecorationUtil.setComponentNiceBorder(this, "Chat room");
    }
}
