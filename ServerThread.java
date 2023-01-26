import java.net.Socket;
import java.time.LocalTime;
import java.util.Date;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class ServerThread extends Thread {
    private String clientName;
    private Date connectedTime;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        start();
    }

    @Override
    public void run() {
        String word;
        try {
            clientName = in.readLine();
            connectedTime = new Date();

            send("Hello, " + clientName + "!\n"
                + "PENGU CHAT usage:\n"
                + " - @username<blank>message sends a DM to the respective \n"
                + "   client and only this client. When there is no client \n"
                + "   with this name known to the server, the sender just \n"
                + "   receives a corresponding error message by the server. \n\n"
                + " - If the client sends WHOIS, (s)he and only (s)he, receives \n"
                + "   a list of all currently connected clients and since when they \n"
                + "   are connected.\n\n"
                + " - If a client sends LOGOUT, the connection of this client is closed\n"
                + "   and all streams and of both sides are also closed.\n\n"
                + " - If a client sends PINGU, all currently connected clients receive \n"
                + "   an important fact about penguins (what ever that might be :)).");
            for (ServerThread client : ChatServer.getClients()) {
                if (client != this) {
                    client.send("@" +  clientName + " connected to chat!");
                }
            }

            while (!socket.isClosed()) {
                word = in.readLine();
                if (word != null) {
                    if (word.equals("LOGOUT")) {
                        ChatServer.disconnectClient(this);
                        for (ServerThread client : ChatServer.getClients()) {
                            if (client != this) {
                                client.send("@" +  clientName + " disconnected from chat!");
                            }
                        }
                        break;
                    } else if (word.equals("WHOIS")) {
                        String message = "Now following penguins are in chat:\n";
                        for (ServerThread client : ChatServer.getClients()) {
                            if (client != this) {
                                message += client.clientName + " [" + connectedTime.toString() + "]\n";
                            }
                        }
                        send(message);
                    } else if (word.equals("PINGU")) {
                        String message = "IMPORTANT fact about penguins:\n"
                                + "Penguins live at the South Pole!\n";
                        for (ServerThread client : ChatServer.getClients()) {
                            client.send(message);
                        }
                    } else if (word.startsWith("@")) {
                        int pos = word.indexOf(" ");
                        String name = word.substring(1, pos);
                        String message = word.substring(pos + 1);
                        boolean isFound = false;
                        for (ServerThread client : ChatServer.getClients()) {
                            if (client != this && client.clientName.equals(name)) {
                                client.send("@" + clientName + ": " + message);
                                isFound = true;
                                break;
                            }
                        }
                        if (!isFound) {
                            send("Pinguin @" + name + " not found.");
                        }
                    } else {
                        for (ServerThread client : ChatServer.getClients()) {
                            if (client != this) {
                                client.send("@" + clientName + ": " + word);
                            }
                        }
                        System.out.println(word);
                    }
                }

            }
        } catch (IOException e) {
            System.out.print(e);
        }
        close();
    }

    private void send(String word) {
        try {
            out.write("[" + LocalTime.now().toString().substring(0, 8) + "] " + word + "\n");
            out.flush();
        } catch (IOException e) {
        }
    }

    private void close() {
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {

        }
    }

    public String getClientName() {
        return clientName;
    }
}
