package cz.korpen.guardianfx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class BaseMenuController {

    @FXML
    protected Button homeButton;
    @FXML
    protected Button categoryButton;
    @FXML
    protected Button receiptButton;
    @FXML
    protected Button reportButton;
    @FXML
    protected Button incomeButton;
    @FXML
    protected Button incCategoryButton;

    private Button clickedButton;

    public void initialize() {
        // Attach the same event handler to all buttons
        homeButton.setOnAction(this::handleButtonAction);
        categoryButton.setOnAction(this::handleButtonAction);
        receiptButton.setOnAction(this::handleButtonAction);
        reportButton.setOnAction(this::handleButtonAction);
        incomeButton.setOnAction(this::handleButtonAction);
        incCategoryButton.setOnAction(this::handleButtonAction);
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        clickedButton = (Button) event.getSource();

        // Map button ID to FXML file
        String fxmlFile = switch (clickedButton.getId()) {
            case "homeButton" -> "/cz/korpen/guardianfx/home_screen.fxml";
            case "receiptButton" -> "/cz/korpen/guardianfx/receipt_screen.fxml";
            case "categoryButton" -> "/cz/korpen/guardianfx/rec_cat_screen.fxml";
            case "reportButton" -> "/cz/korpen/guardianfx/report_screen.fxml";
            case "incomeButton" -> "/cz/korpen/guardianfx/income_screen.fxml";
            case "incCategoryButton" -> "/cz/korpen/guardianfx/inc_cat_screen.fxml";
            default -> null;
        };

        if (fxmlFile != null) {
            switchToScreen(fxmlFile);
        }
    }

    private void switchToScreen(String fxmlFile) {
        try {
            // Load the desired FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

            // Load the FXML and get the root node
            Parent root = loader.load();

            // Get the current primary stage
            Stage stage = (Stage) clickedButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle potential errors, such as FXML not found or loading issues
        }
    }
}
