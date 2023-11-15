package com.example;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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

            ArrayList<Socket> sockets = new ArrayList<>();
            ServerSocket serverSocket = new ServerSocket(6969);
            System.out.println("Server started at 6969");
            
            while (true) {
                Socket connection = serverSocket.accept();
                Server server = new Server(connection, sockets, randomWord);
                server.start();
            }


            
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

}
