package com.example.chat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientConnectionService extends Thread {

    private static Socket socket;
    private static BufferedReader reader;
    private static PrintWriter writer;

    public ClientConnectionService() {
        try {
            this.socket = new Socket("localhost", 9997);
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

        DataStore.username = input.get("username").toString();
        if (input.get("status").toString().equals("login_ok"))
            DataStore.isLoggedIn = true;
        else
            DataStore.isLoggedIn = false;
    }

    public void resolveFindFriends(JSONObject input) {
        DataStore.findFriends.clear();


        JSONArray jArray = input.getJSONArray("nonFriendList");
         for (int ii = 0; ii < jArray.length(); ii++) {
           System.out.println(jArray.getJSONObject(ii).getString("username"));
         DataStore.findFriends.add(jArray.getJSONObject(ii).getString("username"));
        }
//        DataStore.findFriends.add("pls");
//        DataStore.findFriends.add("work");
    }

    public void resolveOperation(JSONObject input) {
        String operation = input.get("operation").toString();
        switch (operation) {
            case "login":
                resolveLogin(input);
                break;
            case "findFriends":
                resolveFindFriends(input);
                break;

        }
    }

    public void run() {
        // listen to events from server
        String inputLine;
        try {
            while ((inputLine = reader.readLine()) != null) {
                JSONObject inputJSON = new JSONObject(inputLine);
                resolveOperation(inputJSON);
                writer.println(inputLine);
                System.out.println("Wiadomosc od serwera: " + inputLine);
            }

        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
