/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStorage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;

/**
 *
 * @author Swashy
 */
final public class PlayersStorage {

    static ArrayList playerNames = new ArrayList();
    static ArrayList lobbyList = new ArrayList();
    static ArrayList masterSocketPortList = new ArrayList();
    static ArrayList masterSocketIPList = new ArrayList();
    static Label player1;
    static Label player2;
    static CheckBox player1ready;
    static int playernumber;
    static int playersInLobby;
    

    public static int getPlayersInLobby() {
        return playersInLobby;
    }

    public static void setPlayersInLobby(int playersInLobby) {
        PlayersStorage.playersInLobby = playersInLobby;
    }

    public static int getPlayernumber() {
        return playernumber;
    }

    public static void setPlayernumber(int playernumber) {
        PlayersStorage.playernumber = playernumber;
    }

    public static CheckBox getPlayer1ready() {
        return player1ready;
    }

    public static void setPlayer1ready(CheckBox player1ready) {
        PlayersStorage.player1ready = player1ready;
    }

    public static CheckBox getPlayer2ready() {
        return player2ready;
    }

    public static void setPlayer2ready(CheckBox player2ready) {
        PlayersStorage.player2ready = player2ready;
    }
    static CheckBox player2ready;
    static Socket serverSocketToMaster;
    static String masterSocketIP;
    static int masterSocketPORT;
    static String masterSocketPORTString;
    static ArrayList displayPlayerNamesFrames = new ArrayList();

    public static void clearAll(){
        playerNames.clear();
        lobbyList.clear();
        masterSocketIPList.clear();
        masterSocketPortList.clear();
        displayPlayerNamesFrames.clear();
    }
    public static ArrayList getDisplayPlayerNamesFrames() {
        return displayPlayerNamesFrames;
    }

    public static void setDisplayPlayerNamesFrames(ArrayList displayPlayerNamesFrames) {
        PlayersStorage.displayPlayerNamesFrames = displayPlayerNamesFrames;
    }

    public static String getMasterSocketPORTString() {
        return masterSocketPORTString;
    }

    public static void setMasterSocketPORTString(String masterSocketPORTString) {
        PlayersStorage.masterSocketPORTString = masterSocketPORTString;
    }

    public static ArrayList getMasterSocketPortList() {
        return masterSocketPortList;
    }

    public static void setMasterSocketPortList(ArrayList masterSocketPortList) {
        PlayersStorage.masterSocketPortList = masterSocketPortList;
    }

    public static ArrayList getMasterSocketIPList() {
        return masterSocketIPList;
    }

    public static void setMasterSocketIPList(ArrayList masterSocketIPList) {
        PlayersStorage.masterSocketIPList = masterSocketIPList;
    }

    public static String getMasterSocketIP() {
        return masterSocketIP;
    }

    public static void setMasterSocketIP(String masterSocketIP) {
        PlayersStorage.masterSocketIP = masterSocketIP;
    }

    public static int getMasterSocketPORT() {
        return masterSocketPORT;
    }

    public static void setMasterSocketPORT(int masterSocketPORT) {
        PlayersStorage.masterSocketPORT = masterSocketPORT;
    }

    public static ArrayList getLobbyList() {
        return lobbyList;
    }

    public static void setLobbyList(ArrayList lobbyList) {
        PlayersStorage.lobbyList = lobbyList;
    }

    public static Socket getServerSocketToMaster() {
        return serverSocketToMaster;
    }

    public static void setServerSocketToMaster(Socket serverSocketToMaster) {
        PlayersStorage.serverSocketToMaster = serverSocketToMaster;
    }

    public static Label getPlayer1() {
        return player1;
    }

    public static void setPlayer1(Label player1) {
        PlayersStorage.player1 = player1;
    }

    public static Label getPlayer2() {
        return player2;
    }

    public static void setPlayer2(Label player2) {
        PlayersStorage.player2 = player2;
    }

    public static ArrayList getPlayerNames() {
        return playerNames;
    }

    public static void setPlayerNames(ArrayList playerNames) {
        PlayersStorage.playerNames = playerNames;
    }

}
