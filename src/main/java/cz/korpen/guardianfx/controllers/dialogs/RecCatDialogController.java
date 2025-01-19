package cz.korpen.guardianfx.controllers.dialogs;

import cz.korpen.guardianfx.manager.ExpenseCategory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RecCatDialogController extends BaseDialogController {

    ListView<ExpenseCategory> purchaseCategoryListView;

    @FXML
    private TextField descriptionTextField;

    @FXML
    void addReceiptCategory(ActionEvent event) {
        if (titleTextField.getText().isEmpty() || titleTextField.getText() == null) {
            System.out.println("Title cannot be empty.");
        } else {
            String title = titleTextField.getText();
            ExpenseCategory expenseCategory = new ExpenseCategory(title, descriptionTextField.getText());
            categoryManager.addPurchaseCategory(expenseCategory); // Show success message

            // Add new expense to the ListView in the main window
            if (purchaseCategoryListView != null) {
                purchaseCategoryListView.getItems().add(expenseCategory); // Add the new expense
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Category added successfully!");
            alert.showAndWait();

            // Close the dialog
            Stage stage = (Stage) actionButton.getScene().getWindow();
            stage.close();
        }
    }
    // Setter for the ListView reference
    public void setPurchaseCategoryListView(ListView<ExpenseCategory> purchaseCategoryListView) {
        this.purchaseCategoryListView = purchaseCategoryListView;
    }
}
