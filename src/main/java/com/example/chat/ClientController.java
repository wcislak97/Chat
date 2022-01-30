package com.example.chat;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import com.example.chat.*;
import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ClientController {
//
//    @FXML
//    private ListView listViewChatMessages;
//    @FXML
//    private Button btnSend;
//    @FXML
//    private TextArea txtAreaMessage;
//


    @FXML
    private Button sign_in;

    @FXML
    private Button signup_create;  //create an account & log in

    @FXML
    private Label have_account_label;

    @FXML
    private Label signUpToChat_label;

    @FXML
    private Label startForFree_label;

    @FXML
    private VBox sign_in_vbox;

    @FXML
    private TextField signup_email;

    @FXML
    private TextField signup_name;

    @FXML
    private TextField signup_surname;

    @FXML
    private PasswordField signup_password;

//    @FXML
//    protected void onSendButtonClicked(ActionEvent event) {
//        String message = txtAreaMessage.getText();
//
//        listViewChatMessages.getItems().addAll(message);
//
//    }

    @FXML
    protected void onSigninButtonClicked(ActionEvent event) {
        ObservableList<Node> vbox = sign_in_vbox.getChildren();

        if (vbox.contains(signup_name)) {
            sign_in.setText("Sign up!");
            signUpToChat_label.setText("Sign in to Chat Messenger");
            signup_create.setText("Log in");
            have_account_label.setText("Don't have account?");
            vbox.remove(signup_surname);
            vbox.remove(signup_name);
        } else {
            sign_in.setText("Sign in!");
            signUpToChat_label.setText("Sign up to Chat Messenger");
            have_account_label.setText("Already have account?");
            vbox.remove(signup_password);
            vbox.add(signup_name);
            vbox.add(signup_surname);
            vbox.add(signup_password);
            vbox.remove(signup_create);
            vbox.add(signup_create);
            signup_create.setText("Create an account!");

        }

    }

    @FXML
    protected void onsignup_createButtonClicked(ActionEvent event) throws SQLException {
        ObservableList<Node> vbox = sign_in_vbox.getChildren();
        DatabaseConnection db = new DatabaseConnection();
        ResultSet rs;
        if (sign_in.getText() == "Sign up!") {

            //sign in

            String email = signup_email.getText();
            String pw = signup_password.getText();

            //if user info blank or empty
            if (email.isEmpty() || pw.isEmpty() || email.isBlank() || pw.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No sign in information provided");
                alert.showAndWait();
            } else {
                try{

                rs = db.selectStatement("SELECT * FROM user WHERE username='" + email + "' and password='" + pw+"'");

                    //if no user like that exist
                    if (rs.next() == false) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "This username and password does not exist");
                        alert.showAndWait();
                    }
                    else{

                        //if user exists open chat

                        //SET SCENE
                        //ZACZNIJ PRACE SERWERA?

                    }
                }
                catch(SQLException e){
                    e.printStackTrace();
                }

            }


        } else {
            System.out.println("Sign up page");

            //sign up

            String email = signup_email.getText();
            String pw = signup_password.getText();
            String name=signup_name.getText();
            String surname=signup_surname.getText();
            int i1;
            int i2;

            if (email.isEmpty() || pw.isEmpty() || email.isBlank() || pw.isBlank() || name.isBlank() || name.isEmpty() || surname.isEmpty() || surname.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Some info is lacking");
                alert.showAndWait();
            }
            else{

                try{

                    rs = db.selectStatement("SELECT * FROM user WHERE username='" + email + "'");

                    //if no user like that exist
                    if (rs.next() == true) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "This username already exists");
                        alert.showAndWait();
                    }
                    else{
                        i1=db.insertStatement("INSERT INTO user (username,password) VALUES ('"+email+"', '"+pw+"')");
                        i2=db.insertStatement("INSERT INTO user_details (username,name,surname) VALUES ('"+email+"', '"+name+"', '"+surname+"')");

                        //i1 and i2 true if success
                        System.out.println(i1);
                        System.out.println(i2);


                        sign_in.setText("Sign up!");
                        signUpToChat_label.setText("Sign in to Chat Messenger");
                        signup_create.setText("Log in");
                        have_account_label.setText("Don't have account?");
                        vbox.remove(signup_surname);
                        vbox.remove(signup_name);
                    }
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }

        }

    }

}