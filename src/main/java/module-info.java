module cz.korpen.guardianfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.apache.pdfbox;

    opens cz.korpen.guardianfx to javafx.fxml;
    exports cz.korpen.guardianfx;
    exports cz.korpen.guardianfx.controllers;
    opens cz.korpen.guardianfx.controllers to javafx.fxml;
    exports cz.korpen.guardianfx.manager;
    opens cz.korpen.guardianfx.manager to javafx.fxml;
    exports cz.korpen.guardianfx.controllers.dialogs;
    opens cz.korpen.guardianfx.controllers.dialogs to javafx.fxml;
}