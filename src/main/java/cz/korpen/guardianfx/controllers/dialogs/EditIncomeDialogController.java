package cz.korpen.guardianfx.controllers.dialogs;

import cz.korpen.guardianfx.manager.CategoryManager;
import cz.korpen.guardianfx.manager.Income;
import cz.korpen.guardianfx.manager.IncomeCategory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class EditIncomeDialogController extends ItemDialogController<Income, IncomeCategory> {

    ListView<Income> incomeListView;
    Income income;

    @FXML
    void editIncome(ActionEvent event) {
        String title = titleTextField.getText();
        double amount = 0;
        boolean isValid = true;

        // Validate amount field
        if (valueTextField.getText() == null || valueTextField.getText().isEmpty()) {
            System.out.println("Amount field is empty. Please enter a valid number.");
            isValid = false;
        } else {
            try {
                amount = Double.parseDouble(valueTextField.getText());
                if (amount < 0) {
                    System.out.println("Amount cannot be negative. Please enter a positive number.");
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
        IncomeCategory incomeCategory = null;
        if (categoryComboBox.getValue() == null) {
            System.out.println("No category selected. Please select a category.");
            isValid = false;
        } else {
            incomeCategory = categoryComboBox.getValue();
        }

        if (isValid) {
            income.setTitle(titleTextField.getText());
            income.setAmount(Double.parseDouble(valueTextField.getText()));
            income.setDate(datePicker.getValue());
            income.changeCategory(income.getIncomeCategory(), categoryComboBox.getValue());

            // Add new expense to the ListView in the main window
            if (incomeListView != null) {
                incomeListView.getItems().add(income); // Add the new expense
            }
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Income added successfully!");
            alert.showAndWait();

            // Close the dialog
            Stage stage = (Stage) actionButton.getScene().getWindow();
            stage.close();
        }
    }

    public void initialize() {
        populateComboBox();
    }

    private void populateComboBox() {
        List<IncomeCategory> categories = CategoryManager.getInstance().getIncomeCategories();
        populateComboBox(categories);
    }
    // Setter for the ListView reference
    public void setIncomeListView(ListView<Income> incomeListView) {
        this.incomeListView = incomeListView;
    }

    public void setIncome(Income income) {
        this.income = income;

        titleTextField.setText(income.getTitle());
        valueTextField.setText(String.valueOf(income.getAmount()));
        datePicker.setValue(income.getDate());
        categoryComboBox.setValue(income.getIncomeCategory());
    }

    public Income getUpdatedIncome() {
        // Update the income object with the new values
        income.setTitle(titleTextField.getText());
        income.setAmount(Double.parseDouble(valueTextField.getText()));
        income.setDate(datePicker.getValue());
        return income;
    }
}