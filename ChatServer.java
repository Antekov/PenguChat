import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ChatServer {
    private static Socket clientSocket;
    private static ServerSocket server;
    private static int port = 3000;
    private static LinkedList<ServerThread> clients = new LinkedList<>();

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0].trim());
            } catch (NumberFormatException e) {
                System.out.println("Port is not correct. Defaul port 3000 will be used.");
            }
        }
        try {
            try {
                server = new ServerSocket(port);
                System.out.println("Server started at port "
                        + port + "...");

                while (true) {
                    clientSocket = server.accept();
                    try {
                        clients.add(new ServerThread(clientSocket));
                    } catch (IOException e) {
                        clientSocket.close();
                    }
                }
            } finally {
                server.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static LinkedList<ServerThread> getClients() {
        return clients;
    }

    public static void disconnectClient(ServerThread client) {
        System.out.println("Disconnected client: " + client.getClientName());
        clients.remove(client);
    }
}