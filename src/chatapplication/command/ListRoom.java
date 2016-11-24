/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.command;

import chatapplication.entity.User;

/**
 *
 * @author VuongKM
 */
public class ListRoom extends Command {

    public ListRoom(User creator) {
        this.creator = creator;
    }
}
