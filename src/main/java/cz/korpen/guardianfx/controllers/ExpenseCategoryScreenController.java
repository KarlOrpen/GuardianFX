package cz.korpen.guardianfx.controllers;

import cz.korpen.guardianfx.controllers.dialogs.RecCatDialogController;
import cz.korpen.guardianfx.manager.ExpenseCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class ExpenseCategoryScreenController extends BaseController {

    @FXML
    private Button addReceiptCategoryButton;

    @FXML
    private Button categoryButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button incCategoryButton;

    @FXML
    private Button incomeButton;

    @FXML
    private Button receiptButton;

    @FXML
    private ListView<ExpenseCategory> receiptCategoryListView;

    @FXML
    private Button reportButton;

    public void initialize() {
        updateCategoryList();
    }

    @FXML
    void handleAddReceiptCategory(ActionEvent event) {
        try {
            // Load the receipt dialog FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/korpen/guardianfx/rec_cat_dialog.fxml"));

            // Create the dialog's root node
            Parent root = loader.load();

            // Get the controller of the dialog (optional)
            RecCatDialogController dialogController = loader.getController();

            dialogController.setPurchaseCategoryListView(receiptCategoryListView);
            // Create a new scene for the dialog
            Scene dialogScene = new Scene(root);

            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Income");
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
    private void updateCategoryList() {
        List<ExpenseCategory> categories = categoryManager.getPurchaseCategories();

        // Set the items to the ListView
        ObservableList<ExpenseCategory> purchaseCategoriesObservableList = FXCollections.observableArrayList(categories);
        receiptCategoryListView.setItems(purchaseCategoriesObservableList);
    }
}
