package cz.korpen.guardianfx.controllers.dialogs;

import cz.korpen.guardianfx.manager.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class IncomeDialogController extends BaseDialogController {

    private ListView<Income> incomeListView;
    @FXML
    private Button addIncomeButton;

    @FXML
    private TextField amountTextField;

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<IncomeCategory> categoryComboBox;

    @FXML
    private DatePicker dateOfRecievePicker;

    @FXML
    private TextField titleTextField;

    @FXML
    void addIncome(ActionEvent event) {
        String title = titleTextField.getText();
        double amount = 0;
        boolean isValid = true;

        // Validate amount field
        if (amountTextField.getText() == null || amountTextField.getText().isEmpty()) {
            System.out.println("Amount field is empty. Please enter a valid number.");
            isValid = false;
        } else {
            try {
                amount = Double.parseDouble(amountTextField.getText());
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
        if (dateOfRecievePicker.getValue() == null) {
            System.out.println("No date selected. Please select a valid date.");
            isValid = false;
        } else {
            selectedDate = dateOfRecievePicker.getValue();
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
            Income income = new Income(title, amount, selectedDate, incomeCategory);

            // Add new receipt to the ListView in the main window
            if (incomeListView != null) {
                incomeListView.getItems().add(income); // Add the new receipt
            }
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Income added successfully!");
            alert.showAndWait();

            // Close the dialog
            Stage stage = (Stage) addIncomeButton.getScene().getWindow();
            stage.close();
        }
    }
    public void initialize() {
        dateOfRecievePicker.setValue(LocalDate.now());
        populateComboBox();
        CategoryManager categoryManager = CategoryManager.getInstance();
    }

    private void populateComboBox() {
        List<IncomeCategory> categories = CategoryManager.getInstance().getIncomeCategories();

        // Add categories to the ComboBox
        categoryComboBox.getItems().setAll(categories);

        // Optionally, set the default selection (if needed)
        if (!categories.isEmpty()) {
            categoryComboBox.setValue(categories.get(0)); // Select the first category by default
        }
    }

    // Setter for the ListView reference
    public void setIncomeListView(ListView<Income> incomeListView) {
        this.incomeListView = incomeListView;
    }
}