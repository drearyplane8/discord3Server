package com.example.discord2server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Discord2ServerLoader extends Application {

    public Stage mainstage;

    @Override
    public void start(Stage stage) throws IOException {

        Globals.loader = this;
        this.mainstage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(Discord2ServerLoader.class.getResource("mainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setTitle("Discord2 Server");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();




    }

    public static void main(String[] args) {
        launch();
    }
}