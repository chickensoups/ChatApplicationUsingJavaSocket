/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.command;

import java.io.Serializable;
import chatapplication.entity.User;

/**
 *
 * @author VuongKM
 */
public class Command implements Serializable {

    public String name;
    public User creator;

    public boolean isAllow() {
        return true;
    }
}
