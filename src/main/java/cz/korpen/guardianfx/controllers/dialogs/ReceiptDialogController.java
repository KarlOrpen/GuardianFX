package cz.korpen.guardianfx.controllers.dialogs;

import cz.korpen.guardianfx.manager.CategoryManager;
import cz.korpen.guardianfx.manager.PurchaseCategory;
import cz.korpen.guardianfx.manager.Receipt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class ReceiptDialogController extends ItemDialogController<Receipt, PurchaseCategory> {

    ListView<Receipt> receiptListView;

    @FXML
    private Button selectImageButton;

    @FXML
    void addReceipt(ActionEvent event) {
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
            Receipt receipt = new Receipt(title, cost, selectedDate, purchaseCategory);

            if (receiptListView != null) {
                receiptListView.getItems().add(receipt);
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
    @FXML
    void pickFilePath(ActionEvent event) {

    }

    public void initialize() {
        datePicker.setValue(LocalDate.now());
        populateComboBox();
    }

    private void populateComboBox() {
        List<PurchaseCategory> categories = CategoryManager.getInstance().getPurchaseCategories();
        populateComboBox(categories);
    }

    public void setReceiptListView(ListView<Receipt> receiptListView) {
        this.receiptListView = receiptListView;
    }
}

