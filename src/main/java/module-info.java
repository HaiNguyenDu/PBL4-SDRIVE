module com.example.sgroupdrive {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.example.sgroupdrive to javafx.fxml;
    exports com.example.sgroupdrive;
    exports Controller;
    opens Controller to javafx.fxml;
}