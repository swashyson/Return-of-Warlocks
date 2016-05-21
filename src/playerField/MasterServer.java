/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playerField;

import dataStorage.PlayersStorage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 *
 * @author Johan Nilsson
 */
public class MasterServer {

    private static final int PORT = 9008;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private Thread t;

    public void CreateServer(int port) {

        try {
            serverSocket = new ServerSocket(port);
            PlayersStorage.setMasterSocketPORT(PORT);
            PlayersStorage.setMasterSocketIP(InetAddress.getLocalHost().getHostAddress());
            System.out.println("Creating Master Ingame...: " + InetAddress.getLocalHost().getHostAddress());
            PlayersStorage.setPlayernumber(0);

        } catch (IOException e) {
            System.err.println("Error in creation of the server socket");
            System.exit(0);
        }
    }

    public void broadCastPlayersPos(String message) {
        broadCastMessageToPlayers(message);
    }

    public void broadCastMessageToPlayers(String message) {
        PrintWriter pw = null;
        try {
            for (int j = 0; j < BroadCastSystemForMasterIngame.getClientSockets().size(); j++) {
                Socket temp = (Socket) BroadCastSystemForMasterIngame.getClientSockets().get(j);
                pw = new PrintWriter(temp.getOutputStream());

                pw.print(message);
                pw.flush();

            }
        } catch (Exception e) {
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

                                    MasterServer.ObjectHandler object = new MasterServer.ObjectHandler(clientSocket);
                                    object.start();
                                    BroadCastSystemForMasterIngame.getClientSockets().add(clientSocket);

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

    static boolean checkIfAllIsConnected() {

        if (PlayersStorage.getPlayersInLobby() == BroadCastSystemForMasterIngame.getClientSockets().size()) {
            return true;
        } else {

            return false;
        }

    }

    public void tickRate(int time) {

        t = new Thread(new Runnable() {

            @Override
            public void run() {
                Ticker ticker = new Ticker(time); // 32 ticks per second

                ticker.addTickListener(new TickListener() {

                    @Override
                    public void onTick(float deltaTime) {
                        if (allOtherPlayers.getPlayerLocations() != null) {
                            //broadCastPlayersPos(allOtherPlayers.getPlayerLocations().get(0).toString());
                            //allOtherPlayers.getPlayerLocations().remove(0);
                        }

                    }
                });

                while (true) {
                    ticker.update();
                }

            }
        });
        t.start();
    }

    private static class ObjectHandler extends Thread {

        private Socket clientSocket;

        private ObjectHandler(Socket clientSocket) {
            super("chatHandler");
            this.clientSocket = clientSocket;

        }

        @Override
        public void run() {
            BufferedReader in = null;
            String readMessage;
            String readdata;
            try {
                //in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                readMessage = in.readLine();
                readdata = readMessage.substring(5);
                if (readMessage.contains("||pos")) {
                    allOtherPlayers.getPlayerLocations().add(readdata);

                }

            } catch (Exception ex) {

                ex.printStackTrace();
            }

        }
    }

}
