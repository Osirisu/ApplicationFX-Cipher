module com.example.appcipher {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.appcipher to javafx.fxml;
    exports com.example.appcipher;
}