/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatSystem;

import controllers.FXMLMainChatController;
import dataStorage.DataStorage;
import dataStorage.informationStorage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
public class AllChatToLocal {

    private static final int PORT = 9006;
    private static String SERVER = "Localhost";
    private Socket clientSocket;

    public AllChatToLocal() {

        clientSocket = DataStorage.getChatClientSocket();
        saveServerInformation();
    }

    public final void saveServerInformation() {

        informationStorage.setServerIP(SERVER);
    }

    public void sendMessage(String message) {

        PrintWriter out = null;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(DataStorage.getUserName() + ": " + message);
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

                        }else if(test.contains("||||&")){
                        
                            LobbysSplitterAndAdder();
                            
                        } else {
                            DataStorage.getAllChat().appendText(test + "\n");

                        }

                    }

                    private void LobbysSplitterAndAdder() {

                        DataStorage.newObservableListLobbys();

                        String name = test.substring(5);
                        String[] splitArray = name.split(",");
                        for (String s : splitArray) {
                            DataStorage.getLobbyListAdapter().add(s);
                        }
                        DataStorage.getLobbyList().setItems(DataStorage.getLobbyListAdapter());

                        DataStorage.newObservableListLobbys();
                    }

                    private void playerNamesSplitterAndAdder() {

                        DataStorage.newObservableList();

                        System.out.println("DISPLAY NAME " + DataStorage.getPlayerListAdapter().size());

                        String name = test.substring(5);

                        String[] splitArray = name.split(",");
                        for (String s : splitArray) {
                            DataStorage.getPlayerListAdapter().add(s);
                        }
                        DataStorage.getPlayerList().setItems(DataStorage.getPlayerListAdapter());
                    }
                });

            }
        } catch (SocketException ex) {
            try {
                System.out.println("Lost connection 2");
                clientSocket.close();
            } catch (IOException ex1) {
                Logger.getLogger(AllChatToLocal.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }

    }

}
