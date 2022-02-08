package com.example.chat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnectionService extends Thread {

    private static Socket socket;
    private static BufferedReader reader;
    private static PrintWriter writer;

    public ClientConnectionService() {
        try {
            this.socket = new Socket("localhost", 9999);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String sendMessage(String msg) {
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

    public void resolveLogin(JSONObject input) {
        DataStore.email = input.get("email").toString();
        DataStore.isLoggedIn = true;
    }

    public void resolveOperation(JSONObject input) {
        String operation = input.get("operation").toString();
        switch (operation) {
            case "login":
                resolveLogin(input);
        }
    }

    public  void run() {
        // listen to events from server
        String inputLine;
        try {
            while ((inputLine = reader.readLine()) != null) {
                JSONObject inputJSON = new JSONObject(inputLine);
                resolveOperation(inputJSON);
                writer.println(inputLine);
                System.out.println("Wiadomosc od serwera: " + inputLine);
            }

        } catch(Exception e) {
            System.err.println(e);
        }
    }

}
