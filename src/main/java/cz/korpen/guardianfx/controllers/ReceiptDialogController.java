package cz.korpen.guardianfx.controllers;

import cz.korpen.guardianfx.CategoryManager;
import cz.korpen.guardianfx.PurchaseCategory;
import cz.korpen.guardianfx.Receipt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ReceiptDialogController {

    @FXML
    private Button addReceiptButton;

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<PurchaseCategory> categoryComboBox;

    @FXML
    private TextField costTextField;

    @FXML
    private DatePicker dateOfPurchasePicker;

    @FXML
    private Button discardButton;

    @FXML
    private Button selectImageButton;

    @FXML
    private TextField titleTextField;

    @FXML
    void addReceipt(ActionEvent event) {
        String title = titleTextField.getText();
        double cost = 0;
        boolean isValid = true;

        // Validate cost field
        if (costTextField.getText() == null || costTextField.getText().isEmpty()) {
            System.out.println("Cost field is empty. Please enter a valid number.");
            isValid = false;
        } else {
            try {
                cost = Double.parseDouble(costTextField.getText());
                if (cost < 0) {
                    System.out.println("Cost cannot be negative. Please enter a positive number.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                isValid = false;
            }
        }

        // Validate date field
        LocalDate selectedDate = null;
        if (dateOfPurchasePicker.getValue() == null) {
            System.out.println("No date selected. Please select a valid date.");
            isValid = false;
        } else {
            selectedDate = dateOfPurchasePicker.getValue();
        }

        // Validate category
        PurchaseCategory purchaseCategory = null;
        if (categoryComboBox.getValue() == null) {
            System.out.println("No category selected. Please select a category.");
            isValid = false;
        } else {
            purchaseCategory = categoryComboBox.getValue();
        }

        if (isValid) {
            Receipt receipt = new Receipt(title, cost, selectedDate, purchaseCategory);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Receipt added successfully!");
            alert.showAndWait();

            // Close the dialog
            Stage stage = (Stage) addReceiptButton.getScene().getWindow();
            stage.close();
        }
    }


    @FXML
    void closeDialog(ActionEvent event) {
        boolean confirm = showConfirmationDialog("Confirmation", "Are you sure you want to cancel?");
        if (confirm) {
            // Close the dialog window
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }


    @FXML
    void pickFilePath(ActionEvent event) {

    }

    public void initialize() {
        dateOfPurchasePicker.setValue(LocalDate.now());
        populateComboBox();
        CategoryManager categoryManager = CategoryManager.getInstance();
    }

    private void populateComboBox() {
        List<PurchaseCategory> categories = CategoryManager.getInstance().getCategories();

        // Add categories to the ComboBox
        categoryComboBox.getItems().setAll(categories);

        // Optionally, set the default selection (if needed)
        if (!categories.isEmpty()) {
            categoryComboBox.setValue(categories.get(0)); // Select the first category by default
        }
    }
    public boolean showConfirmationDialog(String title, String message) {
        // Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional, can be used for a header
        alert.setContentText(message);

        // Show the dialog and wait for the user's response
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK; // Returns true if "Yes" (OK) is selected
    }

}

