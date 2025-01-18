package cz.korpen.guardianfx.controllers.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.util.List;

public abstract class ItemDialogController<T, C> extends BaseDialogController {

    @FXML
    protected TextField valueTextField;

    @FXML
    protected ComboBox<C> categoryComboBox;

    @FXML
    protected DatePicker datePicker;

    @FXML
    public void initialize() {

    }

    // Generic method to populate ComboBox for any type of category
    protected void populateComboBox(List<C> categories) {
        // Add categories to the ComboBox
        categoryComboBox.getItems().setAll(categories);
    }

    protected void updateEntity() {

    }
}
