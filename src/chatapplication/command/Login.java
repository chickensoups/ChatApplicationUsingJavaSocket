/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.command;

import chatapplication.util.Config;

/**
 *
 * @author VuongKM
 */
public class Login extends Command {

    public String id;
    public String userName;

    public Login(String id, String userName) {
        this.name = Config.Command.LOGIN.toString();
        this.id = id;
        this.userName = userName;
    }
}
