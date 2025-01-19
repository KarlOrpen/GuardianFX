package cz.korpen.guardianfx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

    public class MenuController {
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
    private Button receiptButton;

    @FXML
    private Button reportButton;

    @FXML
    private AnchorPane rightMenu;

    // Method to initialize the default screen
    @FXML
    public void initialize() {
        loadFXML("/cz/korpen/guardianfx/home_pane.fxml"); // Load default content (Home screen)
    }

    // Method to load different FXML files into the center area
    private void loadFXML(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Node content = loader.load(); // Load the FXML file
            centerArea.getChildren().clear(); // Clear previous content
            centerArea.getChildren().add(content); // Add new content
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            loadFXML(fxmlFile);
        }
    }

}