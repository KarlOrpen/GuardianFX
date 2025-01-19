package cz.korpen.guardianfx.controllers;

import com.fasterxml.jackson.databind.ser.Serializers;
import cz.korpen.guardianfx.controllers.dialogs.EditIncomeDialogController;
import cz.korpen.guardianfx.controllers.dialogs.IncomeDialogController;
import cz.korpen.guardianfx.manager.CategoryManager;
import cz.korpen.guardianfx.manager.Income;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class IncomeScreenController extends BaseController {

    int selectedYear = LocalDate.now().getYear();
    private Income selectedItem;
    private ContextMenu activeContextMenu; // Keeps track of the currently active context menu


    @FXML
    private MenuController menuController;

    @FXML
    private ListView<Income> incomeListView;

    @FXML
    private Spinner<Integer> incomeYearSpinner;

    @FXML
    void handleAddIncome(ActionEvent event) {
        try {
            // Load the receipt dialog FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/korpen/guardianfx/income_dialog.fxml"));

            // Create the dialog's root node
            Parent root = loader.load();

            // Get the controller of the dialog (optional)
            IncomeDialogController dialogController = loader.getController();

            dialogController.setIncomeListView(incomeListView);
            // Create a new scene for the dialog
            Scene dialogScene = new Scene(root);

            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Income");
            dialogStage.setScene(dialogScene);

            // Disable interaction with the main window while the dialog is open
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED); // Remove the title bar

            // Show the dialog
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle potential errors like FXML loading issues
        }
    }



    public void initialize() {
        setUpSpinner();
        updateIncomeList();
    }
    public void setUpSpinner() {
        // Initialize the spinner to select years
        incomeYearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                LocalDate.now().minusYears(25).getYear(), // Min year
                LocalDate.now().plusYears(2).getYear(),  // Max year
                LocalDate.now().getYear() // Default value
        ));

        // Listen for changes to the selected year in the spinner
        incomeYearSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedYear = newValue; // Set the selected year
            updateIncomeList(); // Update the receipt list when the year changes
        });
    }

    private void updateIncomeList() {
        List<Income> incomes = categoryManager.giveYearlyIncomeReport(selectedYear);

        // Set the items to the ListView
        ObservableList<Income> incomeObservableList = FXCollections.observableArrayList(incomes);
        incomeListView.setItems(incomeObservableList);
    }

    @FXML
    public void editDelete(ContextMenuEvent event) {
        // Close the currently open context menu, if any
        if (activeContextMenu != null) {
            activeContextMenu.hide();
        }
        selectedItem = incomeListView.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {

        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(e -> handleEdit(selectedItem));

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> handleDelete(selectedItem));

        MenuItem closeMenu = new MenuItem("Back");
        closeMenu.setOnAction(e -> contextMenu.hide());

        contextMenu.getItems().addAll(editItem, deleteItem, closeMenu);

        // Show the context menu at the mouse click location
        contextMenu.show(incomeListView, event.getScreenX(), event.getScreenY());
        // Keep a reference to the active context menu
        activeContextMenu = contextMenu;

        // Add an event handler to close the context menu when clicking outside
        incomeListView.getScene().setOnMousePressed(e -> {
            if (activeContextMenu != null) {
                activeContextMenu.hide();
                activeContextMenu = null;
            }
        });
    }
}
    private void handleEdit(Income income) {
        try {
            // Load the dialog FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/korpen/guardianfx/edit_income_dialog.fxml"));
            Parent root = loader.load();

            // Get the controller
            EditIncomeDialogController controller = loader.getController();
            controller.setIncome(income); // Pass the current income object to the dialog

            // Create a dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Income");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(incomeListView.getScene().getWindow()); // Set the parent window
            dialogStage.setScene(new Scene(root));

            // Show the dialog and wait for user input
            dialogStage.showAndWait();

            // After dialog closes, get the updated income
            income = controller.getUpdatedIncome();

            // Optionally, refresh the ListView to reflect changes
            incomeListView.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleDelete(Income income) {
        boolean confirm = showConfirmationDialog("Delete item?", "Do you really want to delete this item?");
        if (confirm) {
            income.deleteIncome();
            updateIncomeList();
        }
    }

    private boolean showConfirmationDialog(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}


