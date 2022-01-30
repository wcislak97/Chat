package com.example.chat;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;
import com.example.chat.DatabaseConnection;

import java.io.IOException;

public class Client extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Server.class.getResource("welcome.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 800);
        stage.setTitle("Chat Messenger");
        stage.setScene(scene);
        stage.show();

    }


    public static void main(String[] args) {
        launch();

        /*
        DatabaseConnection mySQL=new DatabaseConnection();

            mySQL.selectStatement("SELECT * FROM table_name WHERE condition");
            mySQL.insertStatement("INSERT INTO table_name (col1,col2) VALUES (val1, val2)");
            mySQL.updateStatement("UPDATE table_name SET col1=val1, col2=val2 WHERE condition");
        */

    }
}