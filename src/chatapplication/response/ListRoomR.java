/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.response;

import chatapplication.entity.Room;
import java.util.HashSet;

/**
 *
 * @author VuongKM
 */
public class ListRoomR extends Response {

    public HashSet<Room> rooms;
}
