/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.entity;

import chatapplication.command.CreateRoom;
import java.io.Serializable;

/**
 *
 * @author VuongKM
 */
public class Room implements Serializable {

    public String name;
    public User creator;
    public int userCount;

    public Room(String name, User creator) {
        this.name = name;
        this.creator = creator;
        this.userCount = 0;
    }

    public Room(CreateRoom createRoom) {
        this.name = createRoom.roomName;
        this.creator = createRoom.creator;
        this.userCount = 0;
    }
}
