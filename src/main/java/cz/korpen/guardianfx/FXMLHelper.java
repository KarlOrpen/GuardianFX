package cz.korpen.guardianfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ResourceBundle;

public class FXMLHelper {

    // Method to load FXML for main views
    public static Parent loadFXML(String fxmlPath, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader(FXMLHelper.class.getResource(fxmlPath));
            if (resources != null) {
                loader.setResources(resources);
            }
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to load FXML for dialogs and optionally retrieve the controller
    public static <T> T showDialog(String fxmlPath, ResourceBundle resources, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(FXMLHelper.class.getResource(fxmlPath));
            loader.setResources(resources);

            Parent root = loader.load();

            if (root != null) {
                Scene scene = new Scene(root);
                Stage dialogStage = new Stage();
                dialogStage.setTitle(title);
                dialogStage.initStyle(StageStyle.UNDECORATED);
                dialogStage.setScene(scene);
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.showAndWait();

                // Return the controller of the loaded FXML
                return loader.getController();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Return null if loading fails
    }

    public static void loadScreen(String fxmlPath, ResourceBundle resources, AnchorPane centerArea) {
        try {
            Node content = FXMLHelper.loadFXML(fxmlPath, resources);
            if (content != null) {
                centerArea.getChildren().clear();
                centerArea.getChildren().add(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
