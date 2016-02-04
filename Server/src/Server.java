
import java.io.*;
import java.net.*;

public class Server {

    private static final int PORT = 9000;
    private ServerSocket serverSocket;
    Socket socket;

    //DataInputStream inputFromClient;
    DataOutputStream outputToClient;
    PrintWriter out;
    BufferedReader in;

    public static void main(String[] args) {
        int port = PORT;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }
        new Server(port);
    }

    public Server(int port) {
        //PrintWriter outToClient1;
        //BufferedReader inPutFromClient1;

        // create a server socket
        try {
            serverSocket = new ServerSocket(port);

            System.out.println("Creating Server...");
        } catch (IOException e) {
            System.err.println("Error in creation of the server socket");;
            System.exit(0);
        }

        while (true) {
            try {
                // listen for a connection
                System.out.println("Waiting for connection...");
                socket = serverSocket.accept();
                shutDownHook(socket);
                System.out.println("Client Connected");
                outputToClient = new DataOutputStream(socket.getOutputStream());
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Connection succssed");

                String test = in.readLine();
                System.out.println("Client connected on IP: " + test);

                Protocol p = new Protocol();

                while (true) {

                    String typo = in.readLine();
                    String[] typoSplit = typo.split(" ");
                    System.out.println("Client: " + typo);
                    String syntax = p.checkForCommands(typoSplit[0]);
                    if(typoSplit[0].equals("List")||typoSplit[0].equals("apa")){
                        //if (!syntax.equals("")) {
                            System.out.println("Server: " + syntax);

                        //}
                        out.println(syntax);
                        out.flush();
                    }else if(typoSplit[0].equals("dl")){
                        outputToClient.writeBytes(syntax);
                    }
                    
                }

            } catch (IOException e) {
                System.out.println("Client failed to connect!");
            }
        }
    }

    private void shutDownHook(Socket socket) {
        System.out.println("Applying safe exit hook");
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    socket.close();
                    serverSocket.close();
                    System.out.println("Connection Closed.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        ));
    }

}
