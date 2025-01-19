package cz.korpen.guardianfx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.LocalDate;
import java.util.Map;

public class ReportScreenController extends BaseController {

    private boolean expensesChecked;
    private boolean incomesChecked;
    private boolean bankAccountsChecked;
    private int selectedYear;

    @FXML
    private Spinner<Integer> yearSpinner;
    @FXML
    private CheckBox bankAccountsCheckBox;

    @FXML
    private CheckBox expensesCheckBox;

    @FXML
    private CheckBox incomesCheckBox;

    @FXML
    private TextFlow reportTextFlow;

    @FXML
    void bankAccountsChecked(ActionEvent event) {
        bankAccountsChecked = bankAccountsCheckBox.isSelected();
    }

    @FXML
    void expensesChecked(ActionEvent event) {
        expensesChecked = expensesCheckBox.isSelected();
        initialize();
    }

    @FXML
    void incomesChecked(ActionEvent event) {
        incomesChecked = incomesCheckBox.isSelected();
        initialize();
    }

    public void initialize() {
        initializeSpinner();
        int selectedYear = yearSpinner.getValue();

        // Clear the reportTextFlow before writing new content
        reportTextFlow.getChildren().clear();

        if (expensesCheckBox.isSelected()) {
            writeYearlyExpenses(selectedYear, reportTextFlow);
        }
        if (incomesCheckBox.isSelected()) {
            writeYearlyIncomes(selectedYear, reportTextFlow);
        }
    }

    private void writeYearlyExpenses(int selectedYear, TextFlow textFlow) {
        Map<String, Double> totalExpenses = categoryManager.calculateTotalCostPerCategory(selectedYear);
        // Generate Text nodes for each category and total cost
        totalExpenses.forEach((categoryName, totalCost) -> {
            Text text = new Text("Category: " + categoryName + ", Total Expense: " + totalCost + "\n");
            text.setStyle("-fx-font-size: 14px; -fx-fill: red;"); // Style the text
            textFlow.getChildren().add(text); // Add the text to the TextFlow
        });
    }
    private void writeYearlyIncomes(int selectedYear, TextFlow textFlow) {
        Map<String, Double> totalExpenses = categoryManager.calculateTotalIncomePerCategory(selectedYear);
        // Generate Text nodes for each category and total cost
        totalExpenses.forEach((categoryName, totalAmount) -> {
            Text text = new Text("Category: " + categoryName + ", Total Income: " + totalAmount + "\n");
            text.setStyle("-fx-font-size: 14px; -fx-fill: green;"); // Style the text
            textFlow.getChildren().add(text); // Add the text to the TextFlow
        });
    }

    public void initializeSpinner() {
        // Initialize the spinner to select years
        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(LocalDate.now().minusYears(25).getYear(), LocalDate.now().plusYears(2).getYear(), LocalDate.now().getYear()));
        yearSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedYear = newValue;
        });
    }
}

