/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStorage;

import javafx.scene.control.TextArea;

/**
 *
 * @author Swashy
 */
final public class DataStorage {

    static String userName;
    static TextArea allChat;

    public static TextArea getAllChat() {
        return allChat;
    }

    public static void setAllChat(TextArea allChat) {
        DataStorage.allChat = allChat;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        DataStorage.userName = userName;
    }

    public DataStorage() {

    }

}
