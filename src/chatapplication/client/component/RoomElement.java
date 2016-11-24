/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.client.component;

import chatapplication.entity.Room;
import chatapplication.entity.User;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author VuongKM
 */
public class RoomElement extends JPanel {

    public JButton creator;
    public JLabel roomName;
    public JLabel userCount;

    public RoomElement(Room room) {
        if (room == null) {
            this.creator = new JButton("Application");
            this.roomName = new JLabel("Default Room");
            this.userCount = new JLabel(0 + "");
        } else {
            this.creator = new JButton(room.creator.name);
            this.roomName = new JLabel(room.name);
            this.userCount = new JLabel(userCount + "");
        }

        add(this.roomName);
        add(this.creator);
        add(this.userCount);
    }
}
