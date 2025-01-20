package cz.korpen.guardianfx.controllers;

import cz.korpen.guardianfx.FXMLHelper;
import cz.korpen.guardianfx.controllers.dialogs.EditIncomeDialogController;
import cz.korpen.guardianfx.controllers.dialogs.IncomeDialogController;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

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
        // Show the dialog and get the controller
        IncomeDialogController dialogController = FXMLHelper.showDialog(
                "/cz/korpen/guardianfx/income_dialog.fxml",
                resources,
                resources.getString("incomeDialogTitle")
        );

        // Check if the controller is not null before interacting with it
        if (dialogController != null) {
            dialogController.setIncomeListView(incomeListView);
        }
        updateIncomeList();
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
        // Close any currently open context menu
        if (activeContextMenu != null) {
            activeContextMenu.hide();
            activeContextMenu = null;
        }

        // Get the selected item from the ListView
        selectedItem = incomeListView.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            // Create a new context menu
            ContextMenu contextMenu = new ContextMenu();

            // Create "Edit" menu item
            MenuItem editItem = new MenuItem(resources.getString("edit"));
            editItem.setOnAction(e -> {
                contextMenu.hide(); // Close the context menu
                handleEdit(selectedItem);
            });

            // Create "Delete" menu item
            MenuItem deleteItem = new MenuItem(resources.getString("delete"));
            deleteItem.setOnAction(e -> {
                contextMenu.hide(); // Close the context menu
                handleDelete(selectedItem);
            });

            // Create "Close" menu item
            MenuItem closeMenu = new MenuItem(resources.getString("backButton"));
            closeMenu.setOnAction(e -> contextMenu.hide());

            // Add all items to the context menu
            contextMenu.getItems().addAll(editItem, deleteItem, closeMenu);

            // Show the context menu at the event's screen coordinates
            contextMenu.show(incomeListView, event.getScreenX(), event.getScreenY());
            activeContextMenu = contextMenu;

            // Add a mouse pressed event to close the context menu when clicking elsewhere
            incomeListView.getScene().addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, e -> {
                if (activeContextMenu != null && !contextMenu.isShowing()) {
                    activeContextMenu.hide();
                    activeContextMenu = null;
                }
            });
        }
    }

    private void handleEdit(Income income) {
        // Open the edit dialog and get its controller
        EditIncomeDialogController dialogController = FXMLHelper.showDialog(
                "/cz/korpen/guardianfx/edit_income_dialog.fxml",
                resources,
                resources.getString("editIncome")
        );

        // Ensure the dialogController is not null
        if (dialogController != null) {
            dialogController.setIncome(income);
            dialogController.setIncomeListView(incomeListView);
            updateIncomeList();
        }
    }



    private void handleDelete(Income income) {
        boolean confirm = showConfirmationDialog(resources.getString("delete"), resources.getString("confirmDelete"));
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


