package cz.korpen.guardianfx.controllers;

import cz.korpen.guardianfx.FXMLHelper;
import cz.korpen.guardianfx.controllers.dialogs.IncCatDialogController;
import cz.korpen.guardianfx.controllers.dialogs.IncomeDialogController;
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
        // Show the dialog and get the controller
        IncCatDialogController dialogController = FXMLHelper.showDialog(
                "/cz/korpen/guardianfx/inc_cat_dialog.fxml",
                resources,
                resources.getString("incomeCategoryDialogTitle")
        );

        // Check if the controller is not null before interacting with it
        if (dialogController != null) {
            dialogController.setIncomeCategoryListView(incomeCategoryListView);
        }

        updateCategoryList();
    }
    private void updateCategoryList() {
        List<IncomeCategory> categories = categoryManager.getIncomeCategories();

        // Set the items to the ListView
        ObservableList<IncomeCategory> CategoriesObservableList = FXCollections.observableArrayList(categories);
        incomeCategoryListView.setItems(CategoriesObservableList);
    }
}