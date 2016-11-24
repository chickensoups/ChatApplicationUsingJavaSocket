/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.command;

import chatapplication.entity.Room;
import chatapplication.entity.User;
import chatapplication.util.Config;

/**
 *
 * @author VuongKM
 */
public class JoinRoom extends Command {

    public Room room;

    public JoinRoom(User creator, Room room) {
        this.name = Config.Command.JOIN_ROOM.toString();
        this.creator = creator;
        this.room = room;
    }
}
