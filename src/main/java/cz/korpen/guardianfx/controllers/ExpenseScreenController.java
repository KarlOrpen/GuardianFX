package cz.korpen.guardianfx.controllers;

import cz.korpen.guardianfx.controllers.dialogs.EditReceiptDialogController;
import cz.korpen.guardianfx.controllers.dialogs.ReceiptDialogController;
import cz.korpen.guardianfx.manager.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class ExpenseScreenController extends BaseController {
    int selectedYear = LocalDate.now().getYear();
    private Expense selectedItem;
    private ContextMenu activeContextMenu;

    @FXML
    private Button addReceiptButton;

    @FXML
    private Spinner<Integer> receiptYearSpinner;

    @FXML
    private ListView<Expense> receiptListView;

    public void initialize() {
        setUpSpinner();
        updateReceiptList(); // Initially populate the list with receipts
    }

    // Method to update the receipt list based on the selected year
    private void updateReceiptList() {
        List<Expense> expenses = categoryManager.giveYearlyCostReport(selectedYear);

        // Set the items to the ListView
        ObservableList<Expense> expenseObservableList = FXCollections.observableArrayList(expenses);
        receiptListView.setItems(expenseObservableList);
    }

    public void setUpSpinner() {
        // Initialize the spinner to select years
        receiptYearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                LocalDate.now().minusYears(25).getYear(), // Min year
                LocalDate.now().plusYears(2).getYear(),  // Max year
                LocalDate.now().getYear() // Default value
        ));

        // Listen for changes to the selected year in the spinner
        receiptYearSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedYear = newValue; // Set the selected year
            updateReceiptList(); // Update the receipt list when the year changes
        });
    }

    @FXML
    private void handleAddReceipt() {
        try {
            // Load the receipt dialog FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/korpen/guardianfx/receipt_dialog.fxml"));

            // Create the dialog's root node
            Parent root = loader.load();

            // Get the controller of the dialog (optional)
            ReceiptDialogController dialogController = loader.getController();
            dialogController.setReceiptListView(receiptListView);
            // Create a new scene for the dialog
            Scene dialogScene = new Scene(root);

            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Expense");
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
    @FXML
    public void editDelete(ContextMenuEvent event) {
        // Close the currently open context menu, if any
        if (activeContextMenu != null) {
            activeContextMenu.hide();
        }
        selectedItem = receiptListView.getSelectionModel().getSelectedItem();

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
            contextMenu.show(receiptListView, event.getScreenX(), event.getScreenY());
            // Keep a reference to the active context menu
            activeContextMenu = contextMenu;

            // Add an event handler to close the context menu when clicking outside
            receiptListView.getScene().setOnMousePressed(e -> {
                if (activeContextMenu != null) {
                    activeContextMenu.hide();
                    activeContextMenu = null;
                }
            });
        }
    }
    private void handleEdit(Expense expense) {
        try {
            // Load the dialog FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/korpen/guardianfx/edit_receipt_dialog.fxml"));
            Parent root = loader.load();


            // Get the controller
            EditReceiptDialogController controller = loader.getController();
            controller.setReceipt(expense); // Pass the current expense object to the dialog

            // Create a dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Expense");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED); // Remove the title bar

            dialogStage.initOwner(receiptListView.getScene().getWindow()); // Set the parent window
            dialogStage.setScene(new Scene(root));

            // Show the dialog and wait for user input
            dialogStage.showAndWait();

            // After dialog closes, get the updated expense
            expense = controller.getUpdatedReceipt();

            // Optionally, refresh the ListView to reflect changes
            receiptListView.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleDelete(Expense expense) {
        boolean confirm = showConfirmationDialog("Delete item?", "Do you really want to delete this item?");
        if (confirm) {
            expense.deleteExpense();
            updateReceiptList();
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