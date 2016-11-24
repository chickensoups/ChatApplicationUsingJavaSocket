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
public class SendMessage extends Command {

    public String content;

    public SendMessage(User creator, String content) {
        this.name = Config.Command.SEND_MESSAGE.toString();
        this.creator = creator;
        this.content = content;
    }
}
