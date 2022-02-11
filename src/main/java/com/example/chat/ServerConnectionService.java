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
            rs = db.selectStatement("SELECT username FROM users WHERE username<>'"+username+"' AND username NOT in (\n" +
                    "SELECT DISTINCT username FROM friends WHERE friends_id in (SELECT distinct friends_id FROM friends WHERE username='"+username+"'))\n" +
                    "AND username like '%"+friend+"%'");

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
            rs = db.selectStatement("SELECT distinct username from friends WHERE username<>'"+username+"' AND friends_id IN (\n" +
                    "SELECT distinct friends_id FROM friends WHERE friends_id IN \n" +
                    "(SELECT friends_id FROM friends WHERE username='"+username+"' \n" +
                    "AND friends_id NOT IN (SELECT friends_id FROM chat WHERE friends_id IN (SELECT distinct friends_id FROM friends WHERE username='"+username+"'))))");
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
        int i;
        try{
            rs1=db.selectStatement("SELECT distinct friends_id FROM friends WHERE friends_id IN \n" +
                    "(SELECT friends_id FROM friends WHERE username='"+username+"' \n" +
                    "AND friends_id IN (SELECT friends_id FROM friends WHERE username='"+friend+"'))");

            if(rs1.next()==false){
              i=db.insertStatement("INSERT INTO friends(friends_id, username)\n" +
                      "VALUES ((SELECT MAX( friends_id ) FROM friends f) +1, '"+username+"'),\n" +
                      "  ((SELECT MAX( friends_id ) FROM friends f), '"+friend+"')");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    private void tryAddChat(String username, String friend){
        DatabaseConnection db = new DatabaseConnection();
        ResultSet rs1;
        int i;
        try{
            rs1=db.selectStatement("SELECT chat_id FROM chat WHERE friends_id IN (SELECT distinct friends_id FROM friends WHERE friends_id IN \n" +
                    "(SELECT friends_id FROM friends WHERE username='"+username+"' AND friends_id IN \n" +
                    "(SELECT friends_id FROM friends WHERE username='"+friend+"')))");

            if(rs1.next()==false){
                i=db.insertStatement("INSERT into chat(friends_id) SELECT distinct friends_id FROM friends WHERE friends_id IN \n" +
                        "(SELECT friends_id FROM friends WHERE username='"+username+"' AND friends_id IN \n" +
                        "(SELECT friends_id FROM friends WHERE username='"+friend+"'));");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    private JSONArray tryFindfindFriendsWithChat(String username){
        DatabaseConnection db = new DatabaseConnection();
        ResultSet rs;
        //Creating a JSONObject object
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();

        try{
            rs = db.selectStatement("SELECT username FROM users where username IN \n" +
                    "(select distinct username from friends WHERE username<>'"+username+"' AND friends_id IN \n" +
                    "(SELECT friends_id FROM chat WHERE friends_id IN (SELECT distinct friends_id FROM friends WHERE username='"+username+"')))");
            while(rs.next()!=false){
                JSONObject record = new JSONObject();
                record.put("username", rs.getString("username"));
                array.put(record);
            }
            jsonObject.put("friendsWithChat", array);

        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return array;
    }

    private JSONArray tryRefreshListOfMessages(String username,String friend){
        DatabaseConnection db = new DatabaseConnection();
        ResultSet rs;
        //Creating a JSONObject object
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();

        try{
            rs = db.selectStatement("SELECT CONCAT(sent_time,' :  ',username,': ',message_body) as message_overall FROM message WHERE chat_id IN(\n" +
                    "SELECT distinct chat_id FROM chat WHERE friends_id IN (SELECT friends_id FROM friends WHERE friends_id IN \n" +
                    "(SELECT friends_id FROM friends WHERE username='"+username+"' AND friends_id IN \n" +
                    "(SELECT friends_id FROM friends WHERE username='"+friend+"')))) order by message_id asc");
            while(rs.next()!=false){
                JSONObject record = new JSONObject();
                record.put("message_overall", rs.getString("message_overall"));
                array.put(record);
            }
            jsonObject.put("listOfMessages", array);

        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return array;
    }

    public void tryAddMessage(String username,String friend,String message){
        DatabaseConnection db = new DatabaseConnection();
        ResultSet rs1;
        int i;
        System.out.println("Why two times?");
        try{
                i=db.insertStatement("INSERT INTO message(chat_id,username,message_body,sent_time)\n" +
                        "VALUES ((SELECT distinct chat_id FROM chat WHERE friends_id IN (SELECT friends_id FROM friends WHERE friends_id IN \n" +
                        "(SELECT friends_id FROM friends WHERE username='"+username+"' AND friends_id IN \n" +
                        "(SELECT friends_id FROM friends WHERE username='"+friend+"')))), '"+username+"','"+message+"',current_timestamp())");


        }catch (Exception e){
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
                break;
            case "findFriendsWithChat":
                username=json.get("username").toString();
                arr=tryFindfindFriendsWithChat(username);
                response.put("operation", "findFriendsWithChat");
                response.put("friendsWithChat",arr);
                sendMessage(response.toString());
                break;
            case "refreshListOfMessages":
                username=json.get("username").toString();
                friend=json.get("friend").toString();
                arr=tryRefreshListOfMessages(username,friend);
                response.put("operation", "refreshListOfMessages");
                response.put("listOfMessages",arr);
                sendMessage(response.toString());
                break;
            case "newMessage":
                System.out.println("?????Why two times");
                username=json.get("username").toString();
                friend=json.get("friend").toString();
                String message=json.get("message").toString();
                tryAddMessage(username,friend,message);
                break;
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
