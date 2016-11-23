/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.entity;

/**
 *
 * @author VuongKM
 */
public class Message {
    public String content;
    public User creator;
    
    public Message(String content, User creator) {
        this.content = content;
        this.creator = creator;
    }
}
