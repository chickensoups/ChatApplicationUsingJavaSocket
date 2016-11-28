/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.response;

import chatapplication.entity.User;

/**
 *
 * @author VuongKM
 */
public class SendMessageR extends Response {

    public User user;
    public String content;
}
