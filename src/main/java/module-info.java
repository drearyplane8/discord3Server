module com.example.discord2server {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.discord2server to javafx.fxml;
    exports com.example.discord2server;
}