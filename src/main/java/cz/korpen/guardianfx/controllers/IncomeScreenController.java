package cz.korpen.guardianfx.controllers;

import cz.korpen.guardianfx.controllers.dialogs.IncomeDialogController;
import cz.korpen.guardianfx.manager.CategoryManager;
import cz.korpen.guardianfx.manager.Income;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class IncomeScreenController extends BaseMenuController {

    CategoryManager categoryManager = CategoryManager.getInstance();
    int selectedYear = LocalDate.now().getYear();

    @FXML
    private Button addIncomeButton;

    @FXML
    private Button categoryButton;

    @FXML
    private Button homeButton;

    @FXML
    private ListView<Income> incomeListView;

    @FXML
    private Spinner<Integer> incomeYearSpinner;

    @FXML
    private Button receiptButton;

    @FXML
    private Button reportButton;

    @FXML
    void handleAddIncome(ActionEvent event) {
        try {
            // Load the receipt dialog FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/korpen/guardianfx/income_dialog.fxml"));

            // Create the dialog's root node
            Parent root = loader.load();

            // Get the controller of the dialog (optional)
            IncomeDialogController dialogController = loader.getController();

            dialogController.setIncomeListView(incomeListView);
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



    public void initialize() {
        setUpSpinner();
        updateIncomeList();
    }
    public void setUpSpinner() {
        // Initialize the spinner to select years
        incomeYearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                LocalDate.now().minusYears(25).getYear(), // Min year
                LocalDate.now().plusYears(2).getYear(),  // Max year
                LocalDate.now().getYear() // Default value
        ));

        // Listen for changes to the selected year in the spinner
        incomeYearSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedYear = newValue; // Set the selected year
            updateIncomeList(); // Update the receipt list when the year changes
        });
    }

    private void updateIncomeList() {
        List<Income> incomes = categoryManager.giveYearlyIncomeReport(selectedYear);

        // Set the items to the ListView
        ObservableList<Income> incomeObservableList = FXCollections.observableArrayList(incomes);
        incomeListView.setItems(incomeObservableList);
    }

}
