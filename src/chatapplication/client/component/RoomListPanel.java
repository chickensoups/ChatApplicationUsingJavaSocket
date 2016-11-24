/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.client.component;

import chatapplication.execute.Client;
import chatapplication.client.util.DecorationUtil;
import chatapplication.command.CreateRoom;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class RoomListPanel extends JPanel {

    public JPanel createRoomFormPanel; // Panel for create room components
    public JTextField roomNameTextField;
    public JButton createRoomButton;
    public JPanel roomListContentPanel;

    public RoomListPanel() {

        createRoomFormPanel = new JPanel();
        roomNameTextField = new JTextField(30);
        createRoomButton = new JButton("Create Room");
        roomListContentPanel = new JPanel();

        createRoomFormPanel.setLayout(new FlowLayout());
        createRoomFormPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        createRoomFormPanel.add(roomNameTextField);
        createRoomFormPanel.add(createRoomButton);

        roomListContentPanel.setLayout(new GridLayout(10, 1));

        setLayout(new BorderLayout());
        add(createRoomFormPanel, BorderLayout.PAGE_START);
        add(new JScrollPane(roomListContentPanel), BorderLayout.CENTER);
        
        DecorationUtil.setComponentNiceBorder(this, "Room list");
    }
}
