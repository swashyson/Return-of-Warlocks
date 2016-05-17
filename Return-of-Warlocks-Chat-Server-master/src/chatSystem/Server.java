/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatSystem;

import controllers.FXMLDocumentControllerChat;
import dataStorage.NamesAndLobbysStorage;
import dataStorage.realDataStorage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

/**
 *
 * @author Swashy
 */
public class Server {

    private static final int PORT = 9006;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    String userName = "";

    public Server() {

        startBroadCastSystem();

    }

    public void startBroadCastSystem() {

        Task task = new Task<Void>() {
            @Override
            public Void call() {
                broadCast();
                return null;
            }
        };
        new Thread(task).start();
    }

    public void CreateServer(int port) {

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Creating Server...");

        } catch (IOException e) {
            System.err.println("Error in creation of the server socket");
            System.exit(0);
        }
    }

    public void StartServer(int port) {

        Task task = new Task<Void>() {
            @Override
            public Void call() {
                while (true) {

                    try {

                        clientSocket = serverSocket.accept();

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    chatHandler chath = new chatHandler(clientSocket);
                                    chath.start();
                                    broadCastSystem.addClientSockets(clientSocket);

                                    System.out.println("Jump to chatHandler");
                                    StartServer(port);

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }
            }
        };
        new Thread(task).start();

    }

    public void broadCast() {

        PrintWriter out = null;

        int i = 0;
        while (true) {

            if (!broadCastSystem.getClientSockets().isEmpty() && !broadCastSystem.getBroadCastList().isEmpty()) {
                for (i = 0; i < broadCastSystem.getClientSockets().size(); i++) {

                    try {
                        Socket temp = (Socket) broadCastSystem.getClientSockets().get(i);
                        out = new PrintWriter(temp.getOutputStream(), true);

                        if (!broadCastSystem.getBroadCastList().isEmpty()) {

                            out.println(broadCastSystem.getBroadCastList().get(0));
                            out.flush();
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                if (!broadCastSystem.getBroadCastList().isEmpty()) {
                    broadCastSystem.getBroadCastList().remove(0);
                }
            } else {

                System.out.print("");
                i = 0;
            }
        }

    }

    public static void broadCastPlayerNames() {

        PrintWriter out = null;

        System.out.println("HELLO " + NamesAndLobbysStorage.getNames().size());

        for (int j = 0; j < broadCastSystem.getClientSockets().size(); j++) {

            try {
                Socket temp = (Socket) broadCastSystem.getClientSockets().get(j);
                out = new PrintWriter(temp.getOutputStream(), true);

                out.println("|||||" + NamesAndLobbysStorage.getNames());
                System.out.println("sending a ||||| message to " + NamesAndLobbysStorage.getNames());
                out.flush();

            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void broadCastLobbys() {

        PrintWriter out = null;

        for (int j = 0; j < broadCastSystem.getClientSockets().size(); j++) {

            try {
                Socket temp = (Socket) broadCastSystem.getClientSockets().get(j);
                out = new PrintWriter(temp.getOutputStream(), true);
                out.println("||||&" + NamesAndLobbysStorage.getLobbys());
                out.flush();

            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void broadCastLobbyMasterPORT() {

        PrintWriter out = null;

        for (int j = 0; j < broadCastSystem.getClientSockets().size(); j++) {

            try {
                Socket temp = (Socket) broadCastSystem.getClientSockets().get(j);
                out = new PrintWriter(temp.getOutputStream(), true);

                if (!NamesAndLobbysStorage.getMasterPORT().isEmpty()) {
                    out.println("||||%" + NamesAndLobbysStorage.getMasterPORT());
                    out.flush();
                }

            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void broadCastLobbyMasterIP() {

        PrintWriter out = null;

        for (int j = 0; j < broadCastSystem.getClientSockets().size(); j++) {

            try {
                Socket temp = (Socket) broadCastSystem.getClientSockets().get(j);
                out = new PrintWriter(temp.getOutputStream(), true);

                if (!NamesAndLobbysStorage.getMasterIP().isEmpty()) {
                    out.println("||||!" + NamesAndLobbysStorage.getMasterIP());
                    out.flush();
                }

            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private static class chatHandler extends Thread {

        private Socket clientSocket;

        private chatHandler(Socket clientSocket) {
            super("chatHandler");
            this.clientSocket = clientSocket;

        }

        @Override
        public void run() {
            System.out.println("Test");
            BufferedReader in;
            String name = "";
            String realName = "";
            try {

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (true) {

                    String test = in.readLine();
                    if (test.contains("|||||")) { //add  name request and send back name list
                        System.out.println("got a ||||| message ");
                        name = test.substring(5);
                        realName = test.substring(5);
                        NamesAndLobbysStorage.getNames().add(name);
                        Server.broadCastPlayerNames();

                    } else if (test.contains("||||&")) { // add lobby request and send back lobbylist

                        name = test.substring(5);
                        realDataStorage.appendTextArea("Client '" + name + "' Created a new Lobby" + " at Socket: " + clientSocket + "\n");
                        NamesAndLobbysStorage.getLobbys().add(name.replace("[", "").replace("]", "") + " 's Lobby");
                        Server.broadCastLobbys();

                    } else if (test.contains("||||%")) {

                        name = test.substring(5);
                        NamesAndLobbysStorage.getMasterPORT().add(name);
                        realDataStorage.appendTextArea("Created the lobby with MasterPort: " + name + "\n");
                        Server.broadCastLobbyMasterPORT();

                    } else if (test.contains("||||!")) {

                        name = test.substring(5);
                        NamesAndLobbysStorage.getMasterIP().add(name);
                        realDataStorage.appendTextArea("Created the lobby with MasterIP: " + name + "\n");
                        Server.broadCastLobbyMasterIP();

                    } else if (test.contains("||||1")) {// name request
                        System.out.println("name request");
                        broadCastPlayerNames();

                    } else if (test.contains("||||2")) {
                        System.out.println("LobbyListRequest");
                        Server.broadCastLobbys();

                    } else if (test.contains("||||3")) { // disconnect lobby request
                        name = test.substring(5);
                        System.out.println("remove lobby request");
                        System.out.println("||||3: " + name);
                        disconnnectLobby(name);
                        System.out.println(clientSocket.getRemoteSocketAddress() + "remote");

                    } else if (test.contains("||||4")) {
                        name = test.substring(5);
                        System.out.println("remove ip request IP:" + name);
                        removeMasterIP(name);

                    } else if (test.contains("||||5")) {
                        name = test.substring(5);
                        System.out.println("remove port request port:" + name);
                        removeMasterPort(name);

                    } else if (test.contains("||||6")) {
                        name = test.substring(5);
                        System.out.println("request IP for lobbys" + name);
                        broadCastLobbyMasterIP();

                    } else if (test.contains("||||7")) {
                        name = test.substring(5);
                        System.out.println("request PORT for lobbys" + name);
                        broadCastLobbyMasterPORT();

                    } else {
                        realDataStorage.appendTextArea(test + "\n");
                        broadCastSystem.addToList(test);
                    }
                }
            } catch (SocketException ex) {
                removeIfDisconnected(realName);
            } catch (Exception ex2) {

                ex2.printStackTrace();
            }
        }

        private void disconnnectLobby(String name) {
            System.out.println(NamesAndLobbysStorage.getLobbys().get(0));
            NamesAndLobbysStorage.getLobbys().remove(name);

        }

        private void removeMasterPort(String port) {
            NamesAndLobbysStorage.getMasterPORT().remove(port);
            System.out.println("remove port: " + port);
        }

        private void removeMasterIP(String ip) {
            NamesAndLobbysStorage.getMasterIP().remove(ip);
            System.out.println("remove iP: " + ip);
        }

        private void removeIfDisconnected(String name) {
            try {
                clientSocket.close();
                broadCastSystem.getBroadCastList().remove(clientSocket);
                broadCastSystem.getClientSockets().remove(clientSocket);
                NamesAndLobbysStorage.getNames().remove(name);
                NamesAndLobbysStorage.getLobbys().remove(name + " 's Lobby");
                NamesAndLobbysStorage.getMasterPORT().remove(name);
                NamesAndLobbysStorage.getMasterIP().remove(name);
                realDataStorage.appendTextArea("Client '" + name + "' Disconnected" + " at Socket: " + clientSocket + "\n");
                Server.broadCastLobbys();
                Server.broadCastPlayerNames();
                Server.broadCastLobbyMasterPORT();
                Server.broadCastLobbyMasterIP();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

}
