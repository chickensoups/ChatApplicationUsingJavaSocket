/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.command;

import chatapplication.entity.User;
import chatapplication.util.Config;

/**
 *
 * @author VuongKM
 */
public class CreateRoom extends Command {

    public String roomName;

    public CreateRoom(User creator, String roomName) {
        this.name = Config.Command.CREATE_ROOM.toString();
        this.creator = creator;
        this.roomName = roomName;
    }
}
