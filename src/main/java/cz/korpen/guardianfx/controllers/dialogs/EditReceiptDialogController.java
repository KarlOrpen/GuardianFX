package cz.korpen.guardianfx.controllers.dialogs;

import cz.korpen.guardianfx.manager.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class EditReceiptDialogController extends ItemDialogController<Receipt, PurchaseCategory> {

    ListView<Receipt> receiptListView;
    Receipt receipt;

    @FXML
    private Button selectImageButton;

    @FXML
    void pickFilePath(ActionEvent event) {

    }

    @FXML
    void editReceipt(ActionEvent event) {
        String title = titleTextField.getText();
        double cost = 0;
        boolean isValid = true;

        // Validate cost field
        if (valueTextField.getText() == null || valueTextField.getText().isEmpty()) {
            System.out.println("Cost field is empty. Please enter a valid number.");
            isValid = false;
        } else {
            try {
                cost = Double.parseDouble(valueTextField.getText());
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
        if (datePicker.getValue() == null) {
            System.out.println("No date selected. Please select a valid date.");
            isValid = false;
        } else {
            selectedDate = datePicker.getValue();
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
            receipt.setTitle(titleTextField.getText());
            receipt.setCost(Double.parseDouble(valueTextField.getText()));
            receipt.setDateOfPurchase(datePicker.getValue());
            receipt.changeCategory(receipt.getPurchaseCategory(), categoryComboBox.getValue());

            // Add new receipt to the ListView in the main window
            if (receiptListView != null) {
                receiptListView.getItems().add(receipt); // Add the new receipt
            }
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Receipt added successfully!");
            alert.showAndWait();

            // Close the dialog
            Stage stage = (Stage) actionButton.getScene().getWindow();
            stage.close();
        }
    }

    public void initialize() {
        populateComboBox();
    }

    private void populateComboBox() {
        List<PurchaseCategory> categories = CategoryManager.getInstance().getPurchaseCategories();
        populateComboBox(categories);
    }

    // Setter for the ListView reference
    public void setPurchaseListView(ListView<Receipt> receiptListView) {
        this.receiptListView = receiptListView;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;

        titleTextField.setText(receipt.getTitle());
        valueTextField.setText(String.valueOf(receipt.getCost()));
        datePicker.setValue(receipt.getDateOfPurchase());
        categoryComboBox.setValue(receipt.getPurchaseCategory());
    }

    public Receipt getUpdatedReceipt() {
        // Update the income object with the new values
        receipt.setTitle(titleTextField.getText());
        receipt.setCost(Double.parseDouble(valueTextField.getText()));
        receipt.setDateOfPurchase(datePicker.getValue());
        return receipt;
    }

}
