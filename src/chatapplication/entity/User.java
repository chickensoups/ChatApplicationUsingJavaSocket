/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.entity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author VuongKM
 */
public class User implements Serializable {

    public String id;
    public String name;
    public ObjectOutputStream out;
    public ObjectInputStream in;
    public Room currentRoom;

    public User() {

    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
