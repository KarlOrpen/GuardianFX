package cz.korpen.guardianfx.controllers.dialogs;

import cz.korpen.guardianfx.manager.CategoryManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

public abstract class BaseDialogController<T, C> {

    CategoryManager categoryManager = CategoryManager.getInstance();
    protected ListView<T> listView;
    protected T entity;

    @FXML
    protected TextField titleTextField;

    @FXML
    protected Button actionButton;

    @FXML
    protected Button backButton;
    // Common method to show a confirmation dialog
    @FXML
    protected boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // Method for closing the dialog (could be reused across all dialog controllers)
    @FXML
    protected void closeDialog(ActionEvent event) {
        boolean confirm = showConfirmationDialog("Confirmation", "Are you sure you want to cancel?");
        if (confirm) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}

