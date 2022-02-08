package com.example.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static ArrayList<ClientService> clients = new ArrayList<ClientService>();

    public static void main(String[] args) {
        ServerSocket serverSocket;
        Socket socket;

        try {
            serverSocket = new ServerSocket(9997);
            while(true){
                System.out.println("Czekam na polaczenie...");
                socket = serverSocket.accept();
                System.out.println("Nawiazano polaczenie.");
                ServerConnectionService clientThread = new ServerConnectionService(socket);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}