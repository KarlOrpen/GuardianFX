package cz.korpen.guardianfx.controllers.dialogs;

import cz.korpen.guardianfx.manager.CategoryManager;
import cz.korpen.guardianfx.manager.IncomeCategory;
import cz.korpen.guardianfx.manager.PurchaseCategory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class IncCatDialogController extends BaseDialogController {

    ListView<IncomeCategory> incomeCategoryListView;

    @FXML
    private TextField descriptionTextField;

    @FXML
    void addReceiptCategory(ActionEvent event) {
        if (titleTextField.getText().isEmpty() || titleTextField.getText() == null) {
            System.out.println("Title cannot be empty.");
        } else {
            String title = titleTextField.getText();
            IncomeCategory incomeCategory = new IncomeCategory(title);
            categoryManager.addIncomeCategory(incomeCategory); // Show success message

            // Add new receipt to the ListView in the main window
            if (incomeCategoryListView != null) {
                incomeCategoryListView.getItems().add(incomeCategory); // Add the new receipt
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
    public void setIncomeCategoryListView(ListView<IncomeCategory> incomeCategoryListView) {
        this.incomeCategoryListView = incomeCategoryListView;
    }
}
