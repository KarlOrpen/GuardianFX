package cz.korpen.guardianfx;

import cz.korpen.guardianfx.controllers.HomeScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("cz.korpen.guardianfx.messages");
        Parent root = FXMLHelper.loadFXML("/cz/korpen/guardianfx/menu.fxml", resources);
        if (root != null) {
            Scene scene = new Scene(root, 640, 400);
            stage.setTitle("EnGarde!");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}