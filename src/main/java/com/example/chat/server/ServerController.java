package com.example.chat.server;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ServerController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}