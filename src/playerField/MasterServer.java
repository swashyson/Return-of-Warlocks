/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playerField;

import chatSystem.BroadCastSystemForMaster;
import dataStorage.PlayersStorage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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

    private boolean gameOverLockMasterServer = false;
    private int delay = 0;

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
                        broadCastPlayers();
                        broadCastFireBalls();
                        checkForGameOver();
                        delay = delay + 1;

                    }
                });

                while (true) {
                    ticker.update();
                    try {
                        t.sleep(1);
                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

            }
        });
        t.start();
    }

    public static void broadCastPlayers() {

        PrintWriter out = null;

        try {
            for (int i = 0; i < BroadCastSystemForMasterIngame.getBroadCastList().size(); i++) {
                for (int j = 0; j < BroadCastSystemForMasterIngame.getClientSockets().size(); j++) {
                    Socket temp = (Socket) BroadCastSystemForMasterIngame.getClientSockets().get(j);

                    out = new PrintWriter(temp.getOutputStream(), true);
                    out.println("||||." + BroadCastSystemForMasterIngame.getBroadCastList().get(0));
                    out.flush();

                }
                BroadCastSystemForMasterIngame.getBroadCastList().remove(0);
            }
        } catch (Exception ex) {

            ex.printStackTrace();
        }

    }

    public static void broadCastFireBalls() {

        PrintWriter out = null;

        try {
            for (int i = 0; i < BroadCastSystemForMasterIngame.getBroadCastListFireball().size(); i++) {
                for (int j = 0; j < BroadCastSystemForMasterIngame.getClientSockets().size(); j++) {
                    Socket temp = (Socket) BroadCastSystemForMasterIngame.getClientSockets().get(j);

                    out = new PrintWriter(temp.getOutputStream(), true);
                    out.println("||||f" + BroadCastSystemForMasterIngame.getBroadCastListFireball().get(0));
                    out.flush();

                }
                BroadCastSystemForMasterIngame.getBroadCastListFireball().remove(0);
            }
        } catch (Exception ex) {

            ex.printStackTrace();
        }

    }

    public void checkForGameOver() {

        //System.out.println("Antal Döda: " + BroadCastSystemForMasterIngame.getDeathList().size());
        //System.out.println("Antal spelare ingame: " + BroadCastSystemForMasterIngame.getClientSockets().size());
        if (BroadCastSystemForMasterIngame.getDeathList().size() == BroadCastSystemForMasterIngame.getClientSockets().size() && gameOverLockMasterServer == false && delay > 1000) {

            System.out.println("gameOver");
            broadCastGameOver();

        }

    }

    public void broadCastGameOver() {

        PrintWriter out = null;

        try {
            for (int j = 0; j < BroadCastSystemForMasterIngame.getClientSockets().size(); j++) {
                Socket temp = (Socket) BroadCastSystemForMasterIngame.getClientSockets().get(j);

                out = new PrintWriter(temp.getOutputStream(), true);
                out.println("||||d" + BroadCastSystemForMasterIngame.getDeathList().get(BroadCastSystemForMasterIngame.getDeathList().size() - 1));
                out.flush();

            }
            gameOverLockMasterServer = true;
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
            BufferedReader in;
            String syntax = "";

            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                while (true) {
                    String test = in.readLine();

                    if (test.contains("||||.")) {
                        syntax = test.substring(5);
                        BroadCastSystemForMasterIngame.getBroadCastList().add(syntax);

                    }
                    if (test.contains("||||f")) {
                        syntax = test.substring(5);
                        BroadCastSystemForMasterIngame.getBroadCastListFireball().add(syntax);

                    }
                    if (test.contains("||||d")) {

                        syntax = test.substring(5);
                        BroadCastSystemForMasterIngame.getDeathList().add(syntax);

                    }
                }

            } catch (Exception ex) {

                ex.printStackTrace();
            }

        }
    }

}
