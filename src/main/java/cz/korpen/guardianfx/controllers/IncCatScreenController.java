package cz.korpen.guardianfx.controllers;

import cz.korpen.guardianfx.controllers.dialogs.IncCatDialogController;
import cz.korpen.guardianfx.manager.IncomeCategory;
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

public class IncCatScreenController extends BaseController{

    @FXML
    private Button addIncomeCategoryButton;

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
    private ListView<IncomeCategory> incomeCategoryListView;

    @FXML
    private Button reportButton;

    @FXML
    public void initialize() {
        updateCategoryList();
    }

    @FXML
    void handleAddIncomeCategory(ActionEvent event) {
        try {
            // Load the receipt dialog FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/korpen/guardianfx/inc_cat_dialog.fxml"));

            // Create the dialog's root node
            Parent root = loader.load();

            // Get the controller of the dialog (optional)
            IncCatDialogController dialogController = loader.getController();

            dialogController.setIncomeCategoryListView(incomeCategoryListView);
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
        List<IncomeCategory> categories = categoryManager.getIncomeCategories();

        // Set the items to the ListView
        ObservableList<IncomeCategory> CategoriesObservableList = FXCollections.observableArrayList(categories);
        incomeCategoryListView.setItems(CategoriesObservableList);
    }
}