import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientThread {
    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientName;
    private Thread readThread;
    private Thread writeThread;

    public ClientThread(String serverName, int port) {
        try {
            System.out.println("Trying to connect to "
                    + serverName + ":" + port + "...");
            clientSocket = new Socket(serverName, port);
            System.out.println("Connected to " + serverName + ":" + port);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            reader = new BufferedReader(new InputStreamReader(System.in));

            readThread = new ClientReadMessages();
            readThread.start();
            writeThread = new ClientWriteMessages();
            writeThread.start();

        } catch (IOException e) {
            System.out.println(e);
            close();
        }
    }

    private void close() {
        try {
            clientSocket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private class ClientReadMessages extends Thread {
        @Override
        public void run() {
            String word;
            try {
                while (!interrupted()) {
                    word = in.readLine();
                    System.out.println(word);
                }
            } catch (IOException e) {
                //System.out.println(e);
                writeThread.interrupt();
                close();                
                interrupt();
            }
        }
    }

    private class ClientWriteMessages extends Thread {
        @Override
        public void run() {
            String word;
            try {
                System.out.print("Your name: ");
                clientName = reader.readLine();
                out.write(clientName + "\n");
                out.flush();

                while (!interrupted()) {
                    word = reader.readLine();
                    out.write(word + "\n");
                    out.flush();
                    if (word.equals("LOGOUT")) {
                        break;
                    }
                }
            } catch (IOException e) {
                //System.out.println(e);
                readThread.interrupt();
                close();                
                interrupt();
            }
            close();
            readThread.interrupt();
        }
    }
}
