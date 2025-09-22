module com.example.wealthwise {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires com.dlsc.formsfx;
    requires java.desktop;

    opens com.example.wealthwise to javafx.fxml;
    exports com.example.wealthwise;
    exports com.example.wealthwise.Controller;
    exports com.example.wealthwise.Controller.Admin;
    exports com.example.wealthwise.Controller.Client;
    exports com.example.wealthwise.Models;
    exports com.example.wealthwise.Views ;

}