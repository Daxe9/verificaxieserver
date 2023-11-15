package com.example;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    Socket connection;
    BufferedReader in;
    DataOutputStream out;
    ArrayList<Socket> otherConnections;
    String randomWord;
    String[] randomWordArr;
    String guessedWord;
    String[] guessedWordArr;
    int attempts = 0;

    public Server(Socket connection, ArrayList<Socket> otherConnections, String randomWord) throws IOException {
        this.randomWord = randomWord;
        this.randomWordArr = randomWord.split("");
        this.connection = connection;
        this.otherConnections = otherConnections;
        this.guessedWord = "";
    
        for (int i = 0; i < randomWord.length(); ++i) {
            this.guessedWord = this.guessedWord.concat("*");
        }
        this.guessedWordArr = this.guessedWord.split("");

        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new DataOutputStream(connection.getOutputStream());
    }

    /*
     * CLIENT:
     * a -> sceglie lettera
     * b -> indovina parola
     * c -> disconnette
     * 
     * SERVER:
     * b -> chiude connessione
     * c -> errore, ripeti,
     * d -> corretto
     * e -> incorretto
     */

    private String getClientResponse() throws IOException {
        String temp = in.readLine();
        if (temp == null) {
            connection.close();
            System.err.println("Wrong input");
            System.exit(1);
        }
        String result = temp.trim().toLowerCase();
        return result;
    }

    private String getGuessedWord() {
        String result = "";
        for (int i = 0; i < guessedWordArr.length; i++) {
            result = result.concat(guessedWordArr[i]);
        }
        return result;
    }

    private boolean isRevealed() {
        for (int i = 0; i < guessedWordArr.length; i++) {
            if (guessedWordArr[i].equals("*")) {
                return false;
            } 
        }
        return true;
    }

    public void run() {
        try {
            String clientResponse = "";
            out.writeBytes(getGuessedWord() + "\n");
            System.out.println("Wrote to client: " + getGuessedWord());

            do {
                clientResponse = getClientResponse();
                System.out.println("Client said: " + clientResponse);
                attempts++;
                switch (clientResponse) {
                    case "a":
                        String inputLetter = getClientResponse(); 
                        String firstLetter = String.valueOf(inputLetter.charAt(0));

                        for (int i = 0; i < randomWordArr.length ; ++i) {
                            if (randomWordArr[i].equals(firstLetter)) {
                                guessedWordArr[i] = firstLetter;
                            }

                        }

                        if (isRevealed()) {
                            out.writeBytes("d\n");
                            out.writeBytes(String.valueOf(attempts) + "\n");
                            endGame();
                            return;
                        } else {
                            out.writeBytes(getGuessedWord() + "\n");

                        }

                        break;
                
                    case "b":
                        String inputWord = getClientResponse(); 
                        if (inputWord.equals(randomWord)) {
                            out.writeBytes("d\n");
                            out.writeBytes(String.valueOf(attempts) + "\n");
                            endGame();
                            return;
                        } else {
                            out.writeBytes("e\n");
                        }

                        break;

                    case "c":
                        connection.close();
                        break;
                    default:
                        break;
                }

            } while (clientResponse != "c");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    private void endGame() {
        try {
            for (Socket socket : otherConnections) {
                DataOutputStream out = new DataOutputStream(socket.getOutputStream()); 
                out.writeBytes("b\n");
                out.close();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
