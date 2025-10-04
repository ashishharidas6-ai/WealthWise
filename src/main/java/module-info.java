module com.smartfinance {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires com.dlsc.formsfx;
    requires java.desktop;
    requires com.opencsv;
    requires org.json;
    requires java.net.http;

    opens com.smartfinance to javafx.fxml;
    opens com.smartfinance.Controller.Admin to javafx.fxml;
    opens com.smartfinance.Controller.Client to javafx.fxml;
    exports com.smartfinance;
    exports com.smartfinance.Controller;
    exports com.smartfinance.Controller.Admin;
    exports com.smartfinance.Controller.Client;
    exports com.smartfinance.Models;
    exports com.smartfinance.Views ;

}
