/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatSystem;

import controllers.FXMLMainChatController;
import dataStorage.DataStorage;
import dataStorage.PlayersStorage;
import dataStorage.informationStorage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

/**
 *
 * @author Swashy
 */
public class Chat {

    private static final int PORT = 9006;
    private static String SERVER = "Localhost";
    private Socket clientSocket;

    public Chat() {
        clientSocket = DataStorage.getChatClientSocket();
        saveServerInformation();
    }

    public String getServerIp() {
        return SERVER;
    }

    public final void saveServerInformation() {

        informationStorage.setServerIP(SERVER);
    }
    public void sendMessage(String message){

        PrintWriter out = null;

        try {
            out = new PrintWriter(DataStorage.getChatClientSocket().getOutputStream(), true);
            out.println(DataStorage.getUserName() + ": " + message);
            out.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void requestNameList() {
        PrintWriter out = null;
        try {
            out = new PrintWriter(DataStorage.getChatClientSocket().getOutputStream(), true);
            out.println("||||1");
            out.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void requestIPList() {
        PrintWriter out = null;
        try {
            out = new PrintWriter(DataStorage.getChatClientSocket().getOutputStream(), true);
            out.println("||||6");
            out.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void requestPORTList() {
        PrintWriter out = null;
        try {
            out = new PrintWriter(DataStorage.getChatClientSocket().getOutputStream(), true);
            out.println("||||7");
            out.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void requestLobbyList() {
        PrintWriter out = null;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("||||2");
            out.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void checkForIncommingMessage() {

        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while (true) {
                String test = in.readLine();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (test.contains("|||||")) {
                            playerNamesSplitterAndAdder();

                        } else if (test.contains("||||&")) {

                            LobbysSplitterAndAdder();

                        } else if (test.contains("||||%")) {

                            String name = test.substring(5);
                            PlayersStorage.getMasterSocketPortList().add(name);

                        } else if (test.contains("||||!")) {

                            String name = test.substring(5);
                            PlayersStorage.getMasterSocketIPList().add(name);

                        } else {
                            DataStorage.getAllChat().appendText(test + "\n");

                        }

                    }

                    private void LobbysSplitterAndAdder() {

                        DataStorage.newObservableListLobbys();
                        DataStorage.newObservableList();

                        String name = test.substring(5);
                        String[] splitArray = name.split(",");
                        for (String s : splitArray) {
                            DataStorage.getLobbyListAdapter().add(s);
                        }
                        DataStorage.getLobbyList().setItems(DataStorage.getLobbyListAdapter());

                        DataStorage.newObservableListLobbys();
                        DataStorage.newObservableList();
                    }

                    private void playerNamesSplitterAndAdder() {

                        DataStorage.newObservableListLobbys();
                        DataStorage.newObservableList();;

                        String name = test.substring(5);

                        String[] splitArray = name.split(",");
                        for (String s : splitArray) {
                            DataStorage.getPlayerListAdapter().add(s);
                        }
                        DataStorage.getPlayerList().setItems(DataStorage.getPlayerListAdapter());

                        DataStorage.newObservableListLobbys();
                        DataStorage.newObservableList();
                        DataStorage.getPlayerListAdapter().clear();
                    }
                });

            }
        } catch (SocketException ex) {
            System.out.println("Lost connection 3");
            //ex.printStackTrace();
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }

    }

    public void sendNameToServer() {

        PrintWriter out = null;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("|||||" + DataStorage.getUserName());
            setMasterServer();
        } catch (IOException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setMasterServer() {

        PlayersStorage.setServerSocketToMaster(clientSocket);
    }

    public void selectedServerJoin(int value) {

        if (value != -1 || !PlayersStorage.getMasterSocketIPList().isEmpty() || !PlayersStorage.getMasterSocketPortList().isEmpty()) {
            if (value >= 0) {
                PlayersStorage.setMasterSocketIP(PlayersStorage.getMasterSocketIPList().get(value).toString());
                String portString = (String) PlayersStorage.getMasterSocketPortList().get(value);
                PlayersStorage.setMasterSocketPORTString(portString);
            }

        } else {

        }

    }

}
