package com.example.chat;
import java.sql.*;
import java.io.*;

public class DatabaseConnection {
    String url = "jdbc:mysql://localhost:3306";
    String username = "root";
    String password = "root";
    String driver = "com.mysql.cj.jdbc.Driver";
    String dbName = "chatapp";

    Connection con;
    ResultSet rs;
    Statement statement;
    PreparedStatement preStatement;

    ResultSet selectStatement(String queryText){
        String query=queryText;
        ResultSet rs=null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url + '/' + dbName, username, password);
            statement = con.createStatement();
            rs=statement.executeQuery(query);

        } catch (SQLException e) {
            System.out.println("Blad podczas wybierania danych");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Nie znaleziono sterownikow do bazy danych");
        }

        return rs;
    }

    int updateStatement(String queryText) {
        String query = queryText;
        int res=0;

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url + '/' + dbName, username, password);
            statement = con.createStatement();
            res=statement.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println("Blad podczas aktualizowania danych");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Nie znaleziono sterownikow do bazy danych");
        }

        return res;
    }

    int insertStatement(String queryText){
        String query = queryText;
        int res=0;

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url + '/' + dbName, username, password);
            statement = con.createStatement();
            res=statement.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println("Blad podczas wstawianie danych");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Nie znaleziono sterownikow do bazy danych");
        }

        return res;
    }

}
