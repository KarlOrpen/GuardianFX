package cz.korpen.guardianfx.controllers;

import cz.korpen.guardianfx.FXMLHelper;
import cz.korpen.guardianfx.controllers.dialogs.ExpCatDialogController;
import cz.korpen.guardianfx.manager.ExpenseCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.List;

public class ExpenseCategoryScreenController extends BaseController {

    @FXML
    private Button addExpenseCategoryButton;

    @FXML
    private Button categoryButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button incCategoryButton;

    @FXML
    private Button incomeButton;

    @FXML
    private Button expenseButton;

    @FXML
    private ListView<ExpenseCategory> expenseCategoryListView;

    @FXML
    private Button reportButton;

    public void initialize() {
        updateCategoryList();
    }

    @FXML
    void handleAddExpenseCategory(ActionEvent event) {
        ExpCatDialogController dialogController = FXMLHelper.showDialog("/cz/korpen/guardianfx/exp_cat_dialog.fxml", resources, resources.getString("expenseCategoryDialogTitle"));

        if (dialogController != null){
            dialogController.setPurchaseCategoryListView(expenseCategoryListView);
        }
        updateCategoryList();
    }
    private void updateCategoryList() {
        List<ExpenseCategory> categories = categoryManager.getExpenseCategories();

        // Set the items to the ListView
        ObservableList<ExpenseCategory> purchaseCategoriesObservableList = FXCollections.observableArrayList(categories);
        expenseCategoryListView.setItems(purchaseCategoriesObservableList);
    }
}
