package com.example.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientService extends Thread {

    private ArrayList<ClientService> clients;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ClientService(Socket socket, ArrayList<ClientService> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void run() {
        System.out.println("aaa");
    }

}
