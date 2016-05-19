/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playerField;

import chatSystem.BroadCastSystemForMaster;
import chatSystem.LocalChatMaster;
import dataStorage.PlayersStorage;
import dataStorage.allPlayersForMasterInGame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.shape.Circle;

/**
 *
 * @author Swashy
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

                                    ObjectHandler object = new ObjectHandler(clientSocket);
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

                        broadCastPlayers();

                    }
                });

                while (true) {
                    ticker.update();
                }

            }
        });
        t.start();
    }

    public static void broadCastPlayers() {

        try {
            for (int i = 0; i < BroadCastSystemForMasterIngame.getBroadCastList().size(); i++) {
                for (int j = 0; j < BroadCastSystemForMasterIngame.getClientSockets().size(); j++) {
                    Socket temp = (Socket) BroadCastSystemForMasterIngame.getClientSockets().get(j);
                    ObjectOutputStream oos = new ObjectOutputStream(temp.getOutputStream());

                    oos.writeObject(BroadCastSystemForMasterIngame.getBroadCastList().get(i));
                    oos.flush();

                }
                BroadCastSystemForMasterIngame.getBroadCastList().remove(i);
            }
        } catch (Exception ex) {

            ex.printStackTrace();
        }

    }

    private static class ObjectHandler extends Thread {

        private Socket clientSocket;

        private ObjectHandler(Socket clientSocket) {
            super("chatHandler");
            this.clientSocket = clientSocket;

        }

        @Override
        public void run() {
            try {
                boolean lock = false;
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                while (true) {

                    BroadCastSystemForMasterIngame.getBroadCastList().add(ois.readObject());
                    System.out.println("SIZE: " + BroadCastSystemForMasterIngame.getBroadCastList().size());
                }
            } catch (Exception ex) {

                ex.printStackTrace();
            }

        }
    }
}
