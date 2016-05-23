/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStorage;

import java.net.Socket;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

/**
 *
 * @author Swashy
 */
final public class DataStorage {

    static String userName;
    static TextArea allChat;
    static ListView playerList;
    static ObservableList playerListAdapter = FXCollections.observableArrayList();
    static ListView lobbyList;
    static ObservableList lobbyListAdapter = FXCollections.observableArrayList();
    static Socket chatClientSocket;
    static Socket lobbyClientSocket; 
    
    static BooleanProperty startTheGame = new SimpleBooleanProperty(false);

    public static BooleanProperty getStartTheGame() {
        return startTheGame;
    }

    public static void setStartTheGame(BooleanProperty startTheGame) {
        DataStorage.startTheGame = startTheGame;
    }


    public static void setLobbyClientSocket(Socket lobbyClientSocket) {
        DataStorage.lobbyClientSocket = lobbyClientSocket;
    }

    public static Socket getLobbyClientSocket() {
        return lobbyClientSocket;
    }

    public static ListView getLobbyList() {
        return lobbyList;
    }

    public static void setLobbyList(ListView lobbyList) {
        DataStorage.lobbyList = null;
        DataStorage.lobbyList = lobbyList;
    }

    public static ObservableList getLobbyListAdapter() {
        return lobbyListAdapter;
    }

    public static void setLobbyListAdapter(ObservableList lobbyListAdapter) {
        DataStorage.lobbyListAdapter = lobbyListAdapter;
    }

    public static Socket getChatClientSocket() {
        return chatClientSocket;
    }

    public static void setChatClientSocket(Socket clientSocket) {
        DataStorage.chatClientSocket = clientSocket;
    }

    public static ObservableList getPlayerListAdapter() {
        return playerListAdapter;
    }

    public static void newObservableList() {

        playerListAdapter = null;
        playerListAdapter = FXCollections.observableArrayList();

    }

    public static void newObservableListLobbys() {

        lobbyListAdapter = null;
        lobbyListAdapter = FXCollections.observableArrayList();

    }

    public static void setPlayerListAdapter(ObservableList playerListAdapter) {
        DataStorage.playerListAdapter = playerListAdapter;
    }

    public static ListView getPlayerList() {
        return playerList;
    }

    public static void setPlayerList(ListView playerList) {
        DataStorage.playerList = null;
        DataStorage.playerList = playerList;
    }

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
