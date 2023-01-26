public class ChatClient {
    private static String serverName = "localhost";
    private static int port = 3000;
   
    public static void main(String[] args) {
        if (args.length > 0) {
            serverName = args[0];
        }
        if (args.length > 1) {
            try {
                port = Integer.parseInt(args[1].trim());
            } catch (NumberFormatException e) {
                System.out.println("Port is not correct. Defaul port 3000 will be used.");
            }
        }
        new ClientThread(serverName, port);
    }
}
