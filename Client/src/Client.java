
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    private static final int PORT = 9999;
    private static final String SERVER = "Localhost";
    private static int tries = 0;

    //DataInputStream inputFromServer;
    //DataOutputStream outputToServer;
    PrintWriter outPutToServer1;
    BufferedReader inPutFromServer1;

    Socket socket;

    public static void main(String[] args) {
        String server = SERVER;
        int port = PORT;

        if (args.length >= 1) {
            server = args[0];
        }
        if (args.length >= 2) {
            port = Integer.parseInt(args[1]);
        }

        new Client(server, port);
    }

    public Client(String server, int port) {

        try {
            // create a socket to connect to the server
            tries++;
            System.out.println("Attempting to connect to " + SERVER + ":" + PORT);
            socket = new Socket(server, port);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Your Ip: " + socket.getInetAddress());

            out.println(socket.getInetAddress());
            out.flush();

            System.out.println("Connecion succeed");

            while (true) {
                Scanner sc = new Scanner(System.in);
                System.out.println(">>>>>");
                String typo = sc.nextLine();

                out.println(typo);
                out.flush();

                String syntax = in.readLine();

                if (!syntax.equals("")) {
                    System.out.println("Current directory: " + syntax);
                }

            }

        } catch (IOException e) {
            //e.printStackTrace();
            retryException(server, port);
        }

    }

    public void retryException(String server, int port) {

        System.out.println("Failed to connect");
        System.out.println("Attempting to reconnect... " + tries);
        if (tries < 5) {
            new Client(server, port);
        } else {
            System.out.println("Failed.");
        }
    }

    private void shutDownHook(Socket socket, DataInputStream inputFromServer, DataOutputStream outputToServer) {
        System.out.println("Applying safe exit hook");
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    socket.close();
                    inputFromServer.close();
                    outputToServer.close();
                    socket.close();
                    System.out.println("Connection Closed.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        ));
    }

}
