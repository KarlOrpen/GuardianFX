package cz.korpen.guardianfx.controllers;

import cz.korpen.guardianfx.FXMLHelper;
import cz.korpen.guardianfx.controllers.dialogs.EditExpenseDialogController;
import cz.korpen.guardianfx.controllers.dialogs.ExpenseDialogController;
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
    private Button addExpenseButton;

    @FXML
    private Spinner<Integer> yearSpinner;

    @FXML
    private ListView<Expense> expenseListView;

    public void initialize() {
        setUpSpinner();
        updateExpenseList(); // Initially populate the list with expenses
    }

    // Method to update the expense list based on the selected year
    private void updateExpenseList() {
        List<Expense> expenses = categoryManager.giveYearlyCostReport(selectedYear);

        // Set the items to the ListView
        ObservableList<Expense> expenseObservableList = FXCollections.observableArrayList(expenses);
        expenseListView.setItems(expenseObservableList);
    }

    public void setUpSpinner() {
        // Initialize the spinner to select years
        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                LocalDate.now().minusYears(25).getYear(), // Min year
                LocalDate.now().plusYears(2).getYear(),  // Max year
                LocalDate.now().getYear() // Default value
        ));

        // Listen for changes to the selected year in the spinner
        yearSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedYear = newValue; // Set the selected year
            updateExpenseList(); // Update the expense list when the year changes
        });
    }

    @FXML
    private void handleAddExpense() {
        ExpenseDialogController dialogController = FXMLHelper.showDialog(
                "/cz/korpen/guardianfx/expense_dialog.fxml",
                resources,
                resources.getString("expenseDialogTitle")
        );

        if (dialogController != null) {
            dialogController.setExpenseListView(expenseListView);
        }
        updateExpenseList();
    }
    @FXML
    public void editDelete(ContextMenuEvent event) {
        // Close the currently open context menu, if any
        if (activeContextMenu != null) {
            activeContextMenu.hide();
        }
        selectedItem = expenseListView.getSelectionModel().getSelectedItem();

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
            contextMenu.show(expenseListView, event.getScreenX(), event.getScreenY());
            // Keep a reference to the active context menu
            activeContextMenu = contextMenu;

            // Add an event handler to close the context menu when clicking outside
            expenseListView.getScene().setOnMousePressed(e -> {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/korpen/guardianfx/edit_expense_dialog.fxml"));
            Parent root = loader.load();


            // Get the controller
            EditExpenseDialogController controller = loader.getController();
            controller.setExpense(expense); // Pass the current expense object to the dialog

            // Create a dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Expense");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED); // Remove the title bar

            dialogStage.initOwner(expenseListView.getScene().getWindow()); // Set the parent window
            dialogStage.setScene(new Scene(root));

            // Show the dialog and wait for user input
            dialogStage.showAndWait();

            // After dialog closes, get the updated expense
            expense = controller.getUpdatedExpense();

            // Optionally, refresh the ListView to reflect changes
            expenseListView.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleDelete(Expense expense) {
        boolean confirm = showConfirmationDialog("Delete item?", "Do you really want to delete this item?");
        if (confirm) {
            expense.deleteExpense();
            updateExpenseList();
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