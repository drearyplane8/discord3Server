package com.example.discord2server;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.regex.Pattern;

public class Discord2ServerController {

    private final int INIT_PORT_VALUE = 54321;

    //FXML items
    public Button centreButton;
    public Text publicIPText;
    public Text localIPText;
    public TextField portField;
    public Text connectedClientsTitleText;
    public VBox connectedClientsVBox;

    //keep a reference to this, so we can shut it down when we close the GUI and therefore the main thread.
    private IncomingConnectionsHandler incomingConnectionsHandler;


    public void initialize() {

        Globals.controller = this;

        localIPText.setText("Local IP: " + GetLocalIPAddress());
        publicIPText.setText("Public IP: " + GetPublicIPAddress());

        //thanks Emily from StackOverflow for this monstrous one-liner
        //it means we can only type integers into the port field.
        TextFormatter<Integer> formatter = new TextFormatter<>(
                new IntegerStringConverter(),
                INIT_PORT_VALUE,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null);
        portField.setTextFormatter(formatter);

    }

    public void OnStartConnectionButtonPressed() {
        centreButton.setOnAction(e -> OnMainButtonPressedWhileConnectionRunning());

        System.out.println("Attempting to start IncomingConnectionsHandler");
        //create a new thread running an IncomingConnectionsHandler.
        //pass the value of the portField to the constructor of the IncomingConncetionsHandler
        //so it knows what port to set up in.
        //no error handling is needed - the text formatter ensures there can only be an integer in this field
        //and amazingly java recognises that. complements to the JVM
        incomingConnectionsHandler = new IncomingConnectionsHandler(Integer.parseInt(portField.getText()));
        new Thread(incomingConnectionsHandler).start();
    }

    public void OnMainButtonPressedWhileConnectionRunning() {
        //create an alert box to make sure the server op is ok with closing the server.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Shutdown");
        alert.setHeaderText("Are you sure you want to shut the server down?");

        //show the alert box and store the result
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) startShutdownProcedures();
        //otherwise just return
    }


    private String GetLocalIPAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.err.println("Failed to get local IP\n" + e);
        }
        return "Error";
    }

    private String GetPublicIPAddress() {
        try {
            //get a connection to ifconfig.me, my personal favourite IP site.
            //do the streams magic and fart out hopefully an IP address.
            URL url = new URL("https://ifconfig.me");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            return reader.readLine().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }

    public void startShutdownProcedures() {
        //this isnt necessary per se, but spamming stderr on a standard shutdown is idk probably bad
        if (incomingConnectionsHandler != null) {
            incomingConnectionsHandler.interrupt();
        }

        Globals.loader.mainstage.close();
    }


}