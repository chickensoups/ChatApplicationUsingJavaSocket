/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.Response;

import java.io.Serializable;

/**
 *
 * @author VuongKM
 */
public class Response implements Serializable {

    public String name;
    public int status = 200;
    public String message = "OK";

    @Override
    public String toString() {
        return status + " - " + name + " - " + message;
    }
}
