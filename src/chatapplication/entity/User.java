/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.entity;

import java.io.ObjectOutputStream;

/**
 *
 * @author VuongKM
 */
public class User {
    public String name;
    public ObjectOutputStream out;
    public Room currentRoom;
    
    public User() {
        
    }
    
    public User(String name) {
        this.name = name;
    }
}
