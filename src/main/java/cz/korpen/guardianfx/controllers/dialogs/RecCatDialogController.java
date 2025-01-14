package cz.korpen.guardianfx.controllers.dialogs;

import cz.korpen.guardianfx.manager.CategoryManager;
import cz.korpen.guardianfx.manager.Income;
import cz.korpen.guardianfx.manager.PurchaseCategory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class RecCatDialogController extends BaseDialogController {

    CategoryManager categoryManager = CategoryManager.getInstance();
    ListView<PurchaseCategory> purchaseCategoryListView;

    @FXML
    private Button addReceiptButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField titleTextField;

    @FXML
    void addReceiptCategory(ActionEvent event) {
        if (titleTextField.getText().isEmpty() || titleTextField.getText() == null) {
            System.out.println("Title cannot be empty.");
        } else {
            String title = titleTextField.getText();
            PurchaseCategory purchaseCategory = new PurchaseCategory(title, descriptionTextField.getText());
            categoryManager.addPurchaseCategory(purchaseCategory); // Show success message

            // Add new receipt to the ListView in the main window
            if (purchaseCategoryListView != null) {
                purchaseCategoryListView.getItems().add(purchaseCategory); // Add the new receipt
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Category added successfully!");
            alert.showAndWait();

            // Close the dialog
            Stage stage = (Stage) addReceiptButton.getScene().getWindow();
            stage.close();
        }
    }
    // Setter for the ListView reference
    public void setPurchaseCategoryListView(ListView<PurchaseCategory> purchaseCategoryListView) {
        this.purchaseCategoryListView = purchaseCategoryListView;
    }
}
