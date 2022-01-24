package com.example.discord2server;

import javafx.scene.control.Alert;

public class Globals {

    public static Discord2ServerLoader loader;

    public static String URL = "";
    public static String USERNAME = "";
    public static String PASSWORD = "";

    public static void ShowErrorBox(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void ShowErrorBoxNonBlocking(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

}
