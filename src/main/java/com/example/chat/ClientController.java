package com.example.chat;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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
    private Button signup_create;

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
        }
        else{
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
}