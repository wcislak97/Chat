package com.example.chat;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.transform.Result;
import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ServerConnectionService extends Thread {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ServerConnectionService(Socket socket) {
        try {
            this.socket = socket;
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

    private boolean tryLogin(String username, String password) {
        DatabaseConnection db = new DatabaseConnection();
        ResultSet rs;
        boolean res=false;
        try {
            rs = db.selectStatement("SELECT * FROM users WHERE username='" + username + "' and password='" + password + "'");
            //if no user like that exist
            if (rs.next() == false) {
                res=false;
            }
            else{
                System.out.println("Zalogowano");
                res=true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return res;

    }

    private JSONArray tryFindFriend(String username,String friend){
        DatabaseConnection db = new DatabaseConnection();
        ResultSet rs;
        //Creating a JSONObject object
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();


        try{
            rs = db.selectStatement("SELECT username FROM users WHERE username<>'"+username+"' AND username not in \n" +
                    "(SELECT username2 from friends where username1='"+username+"') \n" +
                    "AND username NOT IN (SELECT username1 from friends where username2='"+username+"')" +
                    "AND username LIKE '%"+friend+"%'");

            while(rs.next()!=false){

                JSONObject record = new JSONObject();
                record.put("username", rs.getString("username"));
                array.put(record);
            }
            jsonObject.put("nonFriendList", array);

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return array;
    }

    private JSONArray tryFindFriendsNoChat(String username){
        DatabaseConnection db = new DatabaseConnection();
        ResultSet rs;
        //Creating a JSONObject object
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();

        try{
            rs = db.selectStatement("select username2 as username \n" +
                    "from friends WHERE username1='"+username+"' \n" +
                    "AND username2 not in (SELECT distinct sender_2 FROM chat where sender_1='"+username+"') \n" +
                    "AND username2 not in (SELECT distinct sender_1 FROM chat where sender_2='"+username+"')\n" +
                    "UNION\n" +
                    "select username1 as username \n" +
                    "from friends WHERE username2='test' \n" +
                    "AND username1 not in (SELECT distinct sender_2 FROM chat where sender_1='"+username+"') \n" +
                    "AND username1 not in (SELECT distinct sender_1 FROM chat where sender_2='"+username+"')");
            while(rs.next()!=false){

                JSONObject record = new JSONObject();
                record.put("username", rs.getString("username"));
                array.put(record);
            }
            jsonObject.put("friendsNoChat", array);

        }
        catch(SQLException e){
            e.printStackTrace();
        }


        return array;
    }

    private void tryAddFriend(String username, String friend){
        DatabaseConnection db = new DatabaseConnection();
        ResultSet rs1;
        ResultSet rs2;
        int i;
        try{
            rs1=db.selectStatement("SELECT * from friends where username1='"+username+"' and username2='"+friend+"'");
            rs2=db.selectStatement("SELECT * from friends where username1='"+friend+"' and username2='"+username+"'");

            if(rs1.next()==false && rs2.next()==false){
              i=db.insertStatement("INSERT INTO friends(username1,username2) VALUES('"+username+"','"+friend+"')");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    private void tryAddChat(String username, String friend){
        DatabaseConnection db = new DatabaseConnection();
        ResultSet rs1;
        ResultSet rs2;
        int i;
        try{
            rs1=db.selectStatement("SELECT distinct sender_2 from chat where sender_1='"+username+"' and sender_2='"+friend+"'");
            rs2=db.selectStatement("SELECT distinct sender_1 from chat where sender_1='"+friend+"' and sender_2='"+username+"'");

            if(rs1.next()==false && rs2.next()==false){
                i=db.insertStatement("INSERT INTO chat(sender_1,sender_2) VALUES('"+username+"','"+friend+"')");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void resolveOpearation(JSONObject json) {
        String operation = json.get("operation").toString();
        String username;
        String password;
        String friend;
        JSONObject response = new JSONObject();
        JSONArray arr=new JSONArray();

        switch (operation) {
            case "login":
                username = json.get("username").toString();
                password = json.get("password").toString();
                boolean isLoggedIn = tryLogin(username, password);
                if (isLoggedIn) {
                    response.put("operation", "login");
                    response.put("status", "login_ok");
                    response.put("username", username);
                    sendMessage(response.toString());
                }
                else{
                    response.put("operation","login");
                    response.put("status","login_notok");
                    response.put("username",username);
                    sendMessage(response.toString());
                }
                break;
            case "findFriends":
                username = json.get("username").toString();
                friend = json.get("friend").toString();
                arr = tryFindFriend(username, friend);
                response.put("operation", "findFriends");
                response.put("nonFriendList",arr);
                sendMessage(response.toString());
                break;
            case "addFriend":
                username=json.get("username").toString();
                friend=json.get("friend").toString();
                tryAddFriend(username,friend);
                break;
            case "findFriendsNoChat":
                arr=new JSONArray();
                username = json.get("username").toString();
                arr = tryFindFriendsNoChat(username);
                response.put("operation", "findFriendsNoChat");
                response.put("friendsNoChat",arr);
                sendMessage(response.toString());
                break;
            case "addChat":
                username=json.get("username").toString();
                friend=json.get("friend").toString();
                tryAddChat(username,friend);

        }
    }

    public  void run() {
        // listen to events from client
        String inputLine;
        try {
            while ((inputLine = reader.readLine()) != null) {
                JSONObject inputJSON = new JSONObject(inputLine);
                resolveOpearation(inputJSON);
                writer.println(inputLine);
                System.out.println("Wiadomosc od klienta: " + inputLine);
            }

        } catch(Exception e) {
            System.err.println(e);
        }
    }

}
