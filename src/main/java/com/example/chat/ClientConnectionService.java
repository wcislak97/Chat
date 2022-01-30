package com.example.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnectionService extends Thread {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ClientConnectionService() {
        try {
            this.socket = new Socket("localhost", 9999);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String sendMessage(String msg) {
        try {
            writer.println(msg);
            String resp = null;
            resp = reader.readLine();
            return resp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void stopConnection() {
        try {
            reader.close();
            writer.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public  void run() {
        // listen to events from server
    }

}
