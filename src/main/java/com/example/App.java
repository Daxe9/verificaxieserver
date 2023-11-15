package com.example;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ArrayList<String> randomWords = new ArrayList<>();
        randomWords.add("server");
        randomWords.add("client");
        randomWords.add("computer");
        randomWords.add("astruso");
        randomWords.add("schifiltoso");
        randomWords.add("spaghetti");
        randomWords.add("tiramisu");
        randomWords.add("gomitolo");
        randomWords.add("libro");
        randomWords.add("sproloquio");
        randomWords.add("mellifluo");

        try {
            String randomWord = randomWords.get((int) (Math.random() * randomWords.size()));
            System.out.println("Random Word: " + randomWord);

            HashMap<Socket, Server> map = new HashMap<>();
            // ArrayList<Socket> sockets = new ArrayList<>();
            // ArrayList<Server> clients = new ArrayList<>();
            ServerSocket serverSocket = new ServerSocket(6969);
            System.out.println("Server started at 6969");

            while (map.size() < 3) {
                Socket connection = serverSocket.accept();
                Server server = new Server(connection, map, randomWord);
                map.put(connection, server);
                // clients.add(server);
                // sockets.add(connection);
            }

            for (Map.Entry<Socket, Server> entry : map.entrySet()) {
                Socket key = entry.getKey();
                Server value = entry.getValue();
                DataOutputStream out = new DataOutputStream(key.getOutputStream());
                out.writeBytes("f\n");
                out.close();
                value.start();
            }
            // for (Socket socket : sockets) {
            //     DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            //     out.writeBytes("f\n");
            //     out.close();
            // } 

            // for (Server server : clients) {
            //     server.start();                
            // }
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

}
