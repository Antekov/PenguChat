import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
    private static Socket clientSocket;
    private static String serverName = "localhost";
    private static int port = 9003;
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) {
        try {
            try {
                System.out.println("Trying to connect...");
                clientSocket = new Socket(serverName, port);
                System.out.println("Connected to " + serverName + ":" + port);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                reader = new BufferedReader(new InputStreamReader(System.in));

                String word = reader.readLine();
                out.write(word);
                out.flush();

            } catch (IOException e) {
                System.out.println(e);
            } 
            finally {
                clientSocket.close();
                in.close();
                out.close();
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
