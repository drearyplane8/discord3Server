package com.example.discord2server;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Discord2ServerController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}