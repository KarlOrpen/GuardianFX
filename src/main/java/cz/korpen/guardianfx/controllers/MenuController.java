package cz.korpen.guardianfx.controllers;

import cz.korpen.guardianfx.FXMLHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ResourceBundle;

public class MenuController {
    ResourceBundle resources = ResourceBundle.getBundle("cz.korpen.guardianfx.messages");

    private Button clickedButton;

    @FXML
    private ListView<?> activeListView;

    @FXML
    public AnchorPane centerArea;

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
    private Button reportButton;

    @FXML
    private AnchorPane rightMenu;

    // Method to initialize the default screen
    @FXML
    public void initialize() {
        FXMLHelper.loadScreen("/cz/korpen/guardianfx/home_pane.fxml", resources, centerArea);
        disableCurrentButton("/cz/korpen/guardianfx/home_pane.fxml");
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        clickedButton = (Button) event.getSource();

        // Map button ID to FXML file
        String fxmlFile = switch (clickedButton.getId()) {
            case "homeButton" -> "/cz/korpen/guardianfx/home_pane.fxml";
            case "expenseButton" -> "/cz/korpen/guardianfx/expense_pane.fxml";
            case "categoryButton" -> "/cz/korpen/guardianfx/exp_cat_pane.fxml";
            case "reportButton" -> "/cz/korpen/guardianfx/report_pane.fxml";
            case "incomeButton" -> "/cz/korpen/guardianfx/income_pane.fxml";
            case "incCategoryButton" -> "/cz/korpen/guardianfx/inc_cat_pane.fxml";
            default -> null;
        };

        if (fxmlFile != null) {
            FXMLHelper.loadScreen(fxmlFile, resources, centerArea);
        }
        resetButtonState();
        disableCurrentButton(fxmlFile);
    }
    private void disableCurrentButton(String fxmlFile) {
        // Disable the button corresponding to the loaded screen
        switch (fxmlFile) {
            case "/cz/korpen/guardianfx/home_pane.fxml":
                homeButton.setDisable(true);
                break;
            case "/cz/korpen/guardianfx/expense_pane.fxml":
                expenseButton.setDisable(true);
                break;
            case "/cz/korpen/guardianfx/exp_cat_pane.fxml":
                categoryButton.setDisable(true);
                break;
            case "/cz/korpen/guardianfx/report_pane.fxml":
                reportButton.setDisable(true);
                break;
            case "/cz/korpen/guardianfx/income_pane.fxml":
                incomeButton.setDisable(true);
                break;
            case "/cz/korpen/guardianfx/inc_cat_pane.fxml":
                incCategoryButton.setDisable(true);
                break;
        }
    }

    public void resetButtonState() {
        // Re-enable all buttons when needed, like after screen transition
        homeButton.setDisable(false);
        expenseButton.setDisable(false);
        categoryButton.setDisable(false);
        reportButton.setDisable(false);
        incomeButton.setDisable(false);
        incCategoryButton.setDisable(false);
    }

}