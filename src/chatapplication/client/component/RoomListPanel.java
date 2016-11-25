/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.client.component;

import chatapplication.client.util.DecorationUtil;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
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
    public JLabel noRoomMessage;

    public RoomListPanel() {

        createRoomFormPanel = new JPanel();
        roomNameTextField = new JTextField(10);
        createRoomButton = new JButton("Create Room");
        roomListContentPanel = new JPanel();

        createRoomFormPanel.setLayout(new FlowLayout());
        createRoomFormPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        createRoomFormPanel.add(roomNameTextField);
        createRoomFormPanel.add(createRoomButton);

        noRoomMessage = new JLabel("Do not have any room!");
        noRoomMessage.setVisible(false);
        roomListContentPanel.setLayout(new GridLayout(10, 1));

        setLayout(new BorderLayout());
        add(createRoomFormPanel, BorderLayout.PAGE_START);
        add(noRoomMessage, BorderLayout.PAGE_END);
        add(new JScrollPane(roomListContentPanel), BorderLayout.CENTER);

        DecorationUtil.setComponentNiceBorder(this, "Room list");
    }
}
