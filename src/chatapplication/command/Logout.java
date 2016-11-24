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
public class Logout extends Command {

    public Logout(User creator) {
        this.name = Config.Command.LOGOUT.toString();
        this.creator = creator;
    }
}
