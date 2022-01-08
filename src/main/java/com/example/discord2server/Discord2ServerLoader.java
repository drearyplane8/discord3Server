package com.example.discord2server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Discord2ServerLoader extends Application {

    public Stage mainstage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Discord2ServerLoader.class.getResource("mainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        this.mainstage = stage;
        Globals.loader = this;
    }

    @Override
    public void stop() throws Exception {
        Globals.controller.startShutdownProcedures();
    }

    public static void main(String[] args) {
        launch();
    }
}