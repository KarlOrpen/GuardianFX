package cz.korpen.guardianfx.controllers;

import cz.korpen.guardianfx.CategoryManager;
import cz.korpen.guardianfx.Receipt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ReceiptScreenController {
    CategoryManager categoryManager = CategoryManager.getInstance();
    int selectedYear = LocalDate.now().getYear();

    @FXML
    private Button addReceiptButton;

    @FXML
    private Button categoryButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button receiptButton;

    @FXML
    private Spinner<Integer> receiptYearSpinner;

    @FXML
    private ListView<Receipt> receiptListView;

    @FXML
    private Button reportButton;

    public void initialize() {
        setUpSpinner();
        updateReceiptList(); // Initially populate the list with receipts
    }

    // Method to update the receipt list based on the selected year
    private void updateReceiptList() {
        List<Receipt> receipts = categoryManager.giveYearlyReport(selectedYear);

        // Set the items to the ListView
        ObservableList<Receipt> receiptObservableList = FXCollections.observableArrayList(receipts);
        receiptListView.setItems(receiptObservableList);
    }

    @FXML
    private void switchToHome() {
        try {
            // Load the home screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/korpen/guardianfx/home_screen.fxml"));

            // Load the FXML and get the root node
            Parent homeRoot = loader.load();

            // Get the current primary stage
            Scene homeScene = new Scene(homeRoot);

            // Set the scene to the primary stage
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(homeScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle potential errors, such as FXML not found or loading issues
        }
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

            // Create a new scene for the dialog
            Scene dialogScene = new Scene(root);

            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Receipt");
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

}
