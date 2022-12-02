module es.iesfranciscodelosrios.redSocial {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.logging;
    requires java.xml.bind;
    requires java.sql;
    requires java.sql.rowset;

    opens redSocial.controllers to javafx.fxml;
    opens redSocial.utils.Connection to java.xml.bind;
    exports redSocial.controllers;
    exports redSocial;
    opens redSocial to javafx.fxml;
}