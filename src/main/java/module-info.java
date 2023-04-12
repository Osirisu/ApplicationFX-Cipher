module com.example.appcipher {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.bouncycastle.provider;
    requires java.sql;

    opens com.example.appcipher to javafx.fxml;
    exports com.example.appcipher;
}