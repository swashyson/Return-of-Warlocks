/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatSystem;

import controllers.FXMLLobbyController;
import dataStorage.DataStorage;
import dataStorage.PlayersStorage;
import dataStorage.informationStorage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 *
 * @author Swashy
 */
public class LocalChatMaster {

    private static final int PORT = 9007;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public LocalChatMaster() {

        startBroadCastSystem();

    }

    public void CreateServer(int port) {

        try {
            serverSocket = new ServerSocket(port);
            PlayersStorage.setMasterSocketPORT(PORT);
            PlayersStorage.setMasterSocketIP(InetAddress.getLocalHost().getHostAddress());
            System.out.println("Creating Master...: " + InetAddress.getLocalHost().getHostAddress());
            PlayersStorage.setPlayernumber(0);

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
                                    BroadCastSystemForMaster.addClientSockets(clientSocket);

                                    System.out.println("Jump to chatHandler");
                                    StartServer(port);

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                    } catch (Exception ex) {
                        System.out.println("Server Closed");
                    }
                    return null;
                }
            }
        };
        new Thread(task).start();
    }

    public void removeLobbyFromServer() {
        PrintWriter out = null;
        try {

            out = new PrintWriter(dataStorage.DataStorage.getChatClientSocket().getOutputStream(), true);
            out.println("||||3" + dataStorage.DataStorage.getUserName() + "'s Lobby");
            out.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void broadCast() {

        PrintWriter out = null;

        int i = 0;
        while (true) {

            if (!BroadCastSystemForMaster.getClientSockets().isEmpty() && !BroadCastSystemForMaster.getBroadCastList().isEmpty()) {
                for (i = 0; i < BroadCastSystemForMaster.getClientSockets().size(); i++) {

                    try {
                        Socket temp = (Socket) BroadCastSystemForMaster.getClientSockets().get(i);
                        out = new PrintWriter(temp.getOutputStream(), true);

                        if (!BroadCastSystemForMaster.getBroadCastList().isEmpty()) {

                            out.println(BroadCastSystemForMaster.getBroadCastList().get(0));
                            out.flush();
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                if (!BroadCastSystemForMaster.getBroadCastList().isEmpty()) {
                    BroadCastSystemForMaster.getBroadCastList().remove(0);
                }
            } else {

                System.out.print("");
                i = 0;
            }
        }

    }

    public static void broadCastPlayerNames() {

        PrintWriter out = null;

        try {
            for (int j = 0; j < BroadCastSystemForMaster.getClientSockets().size(); j++) {
                Socket temp = (Socket) BroadCastSystemForMaster.getClientSockets().get(j);
                out = new PrintWriter(temp.getOutputStream(), true);

                out.println("|||||" + PlayersStorage.getPlayerNames());
                out.flush();

            }
        } catch (Exception ex) {

            ex.printStackTrace();
        }

    }

    public void broadCastRemoveNameFromMaster() {
        PrintWriter out = null;
        try {

            out = new PrintWriter(DataStorage.getLobbyClientSocket().getOutputStream(), true);
            out.println("||||-" + DataStorage.getUserName());
            System.out.println("Send to chatServer with command ||||&" + DataStorage.getUserName());
            out.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void broadCastLobbys() {

        PrintWriter out = null;
        try {

            out = new PrintWriter(PlayersStorage.getServerSocketToMaster().getOutputStream(), true);
            out.println("||||&" + DataStorage.getUserName());
            System.out.println("Send to chatServer with command ||||&" + DataStorage.getUserName());
            out.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

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

    public void sendMasterPort() {

        try {
            PrintWriter out = null;

            out = new PrintWriter(PlayersStorage.getServerSocketToMaster().getOutputStream(), true);
            out.println("||||%" + PlayersStorage.getMasterSocketPORT());
            out.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMasterIP() {

        try {
            PrintWriter out = null;

            out = new PrintWriter(PlayersStorage.getServerSocketToMaster().getOutputStream(), true);
            out.println("||||!" + PlayersStorage.getMasterSocketIP());
            out.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void Disconnect() {

        System.out.println("Disconnected");
        try {
            spamChatIfMasterDisconnected();

            sendRemoveRequestFromLobbyList();
            requestLobbyIpRemove();
            requestLobbyPortRemove();
            serverSocket.close();
            //clientSocket.close();
            BroadCastSystemForMaster.getBroadCastList().clear();
            BroadCastSystemForMaster.getClientSockets().clear();
            PlayersStorage.setPlayernumber(PlayersStorage.getPlayernumber() - 1);
            PlayersStorage.clearAll();
            informationStorage.setDontRetry(1);
            System.out.println("Done");

        } catch (Exception ex) {

            ex.printStackTrace();
        }

    }

    public static void broadCastReadyChecksTrue(int playernumber) {

        PrintWriter out = null;

        for (int j = 0; j < BroadCastSystemForMaster.getClientSockets().size(); j++) {

            try {
                Socket temp = (Socket) BroadCastSystemForMaster.getClientSockets().get(j);
                out = new PrintWriter(temp.getOutputStream(), true);

                out.println("||||q" + playernumber);
                out.flush();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static void broadCastReadyChecksFalse(int playernumber) {

        PrintWriter out = null;

        for (int j = 0; j < BroadCastSystemForMaster.getClientSockets().size(); j++) {

            try {
                Socket temp = (Socket) BroadCastSystemForMaster.getClientSockets().get(j);
                out = new PrintWriter(temp.getOutputStream(), true);

                out.println("||||w" + playernumber);
                out.flush();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static void broadCastStartGame() {

        PrintWriter out = null;

        for (int j = 0; j < BroadCastSystemForMaster.getClientSockets().size(); j++) {

            try {
                Socket temp = (Socket) BroadCastSystemForMaster.getClientSockets().get(j);
                out = new PrintWriter(temp.getOutputStream(), true);

                out.println("||||s");
                out.flush();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private void spamChatIfMasterDisconnected() {
        BroadCastSystemForMaster.addToList("Disconnected Master please disconnect from the server");
    }

    private void sendRemoveRequestFromLobbyList() {
        PrintWriter out = null;
        try {

            out = new PrintWriter(PlayersStorage.getServerSocketToMaster().getOutputStream(), true);
            out.println("||||3" + dataStorage.DataStorage.getUserName() + " 's Lobby");
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(LocalChatMaster.class.getName()).log(Level.SEVERE, null, ex);
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
            BufferedReader in;
            String name = "";
            boolean lock = true;

            try {

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (true) {
                    String test = in.readLine();
                    name = test.substring(5);
                    if (test.contains("|||||")) {
                        PlayersStorage.getPlayerNames().add(name);
                        System.out.println("Master Names recieved:" + PlayersStorage.getPlayerNames().size() + name);
                        LocalChatMaster.broadCastPlayerNames();

                    } else if (test.contains("||||q")) {
                        System.out.println("reseving: " + test + " from slave");
                        //PlayersStorage.setPlayerNameReadyCheck(name);
                        broadCastReadyChecksTrue(Integer.parseInt(name));

                    } else if (test.contains("||||w")) {
                        System.out.println("reseving: " + test + " from slave");
                        //PlayersStorage.setPlayerNameReadyCheck(name);
                        broadCastReadyChecksFalse(Integer.parseInt(name));

                    } else if (test.contains("||||p")) {
                        System.out.println("master getting a ||||p request");
                        sendPlayerNumbersOnMaster();

                    } else if (test.contains("|||ap")){
                        addAndSendPlayersInLobby();
                    }else {
                        System.out.println("Recieved message: " + test);
                        BroadCastSystemForMaster.addToList(test);
                    }

                }
            } catch (SocketException ex) {
                removeIfDisconnected(name);
            } catch (Exception ex2) {
                System.out.println("Jag har får null");
                ex2.printStackTrace();
                lock = false;
                slaveDisconnect(name);
            }
        }

        private void sendPlayerNumbersOnMaster() {
            PrintWriter out = null;

            for (int j = 0; j < BroadCastSystemForMaster.getClientSockets().size(); j++) {

                try {
                    Socket temp = (Socket) BroadCastSystemForMaster.getClientSockets().get(j);
                    out = new PrintWriter(temp.getOutputStream(), true);

                    out.println("||||p" + PlayersStorage.getPlayernumber());
                    out.flush();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void addAndSendPlayersInLobby() {
            PlayersStorage.setPlayersInLobby(PlayersStorage.getPlayersInLobby() + 1);
            PrintWriter out = null;

            for (int j = 0; j < BroadCastSystemForMaster.getClientSockets().size(); j++) {

                try {
                    Socket temp = (Socket) BroadCastSystemForMaster.getClientSockets().get(j);
                    out = new PrintWriter(temp.getOutputStream(), true);

                    out.println("|||ap" + PlayersStorage.getPlayersInLobby());
                    out.flush();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void slaveDisconnect(String name) {
            try {

                System.out.println("TASDASDAS");

                BroadCastSystemForMaster.getBroadCastList().remove(clientSocket);
                BroadCastSystemForMaster.getClientSockets().remove(clientSocket);
                PlayersStorage.getPlayerNames().remove(name);
                PlayersStorage.getLobbyList().remove(name);
                //LocalChatMaster.broadCastPlayerNames();
                //LocalChatMaster.broadCastLobbys();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void removeIfDisconnected(String name) {
            try {

                System.out.println("TASDASDAS");

                PlayersStorage.getServerSocketToMaster().close();
                clientSocket.close();
                BroadCastSystemForMaster.getBroadCastList().remove(clientSocket);
                BroadCastSystemForMaster.getClientSockets().remove(clientSocket);
                PlayersStorage.getPlayerNames().remove(name);
                PlayersStorage.getLobbyList().remove(name);
                //LocalChatMaster.broadCastPlayerNames();
                //LocalChatMaster.broadCastLobbys();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void requestLobbyIpRemove() {
        System.out.println("request remove lobbyipclient");
        PrintWriter out = null;
        try {
            out = new PrintWriter(DataStorage.getChatClientSocket().getOutputStream(), true);
            out.println("||||4" + PlayersStorage.getMasterSocketIP());
            System.out.println("send: " + DataStorage.getChatClientSocket().toString());
            out.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void requestLobbyPortRemove() {
        System.out.println("request remove lobbyipclient");
        PrintWriter out = null;
        try {
            out = new PrintWriter(DataStorage.getChatClientSocket().getOutputStream(), true);
            out.println("||||5" + PlayersStorage.getMasterSocketPORT());
            System.out.println("send: " + DataStorage.getChatClientSocket().toString());
            out.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
