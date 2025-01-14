package cz.korpen.guardianfx;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomeScreenController {

    private CategoryManager categoryManager;
    private int selectedYear;
    @FXML
    private Button categoryButton;

    @FXML
    private Button homeButton;

    @FXML
    private TableView<Receipt> lastReceiptsTable;

    @FXML
    private Button receiptButton;

    @FXML
    private Button reportButton;

    @FXML
    private BarChart<String, Number> reportChart;

    @FXML
    private Label reportLabel;

    @FXML
    private Spinner<Integer> yearSpinner;

    @FXML
    private TableColumn<Receipt, Integer> idColumn;
    @FXML
    private TableColumn<Receipt, String> titleColumn;
    @FXML
    private TableColumn<Receipt, Double> costColumn;
    @FXML
    private TableColumn<Receipt, String> dateColumn;
    @FXML
    private TableColumn<Receipt, String> categoryColumn;

    public void initialize() {
        categoryManager = new CategoryManager();
        selectedYear = LocalDate.now().getYear();
        addTestReceipts();
        updateLabel();
        populateChart(selectedYear);
        initializeTableColumns();

        // Initialize the spinner to select years
        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(LocalDate.now().minusYears(25).getYear(), LocalDate.now().plusYears(2).getYear(), LocalDate.now().getYear()));
        yearSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedYear = newValue;
            updateLabel();
            populateChart(selectedYear);
            populateTable(selectedYear);
        });
    }

    public void addTestReceipts() {
        // Create sample categories and add them to the category manager
        PurchaseCategory food = new PurchaseCategory(1, "FOOD", "Food");
        PurchaseCategory entertainment = new PurchaseCategory(2, "ENTERTAINMENT", "Entertainment");
        categoryManager.addCategory(food);
        categoryManager.addCategory(entertainment);

        // Create sample receipts and add them to the categories
        Receipt hamburger = new Receipt(1, "Hamburger", 100.0, LocalDate.of(2025, 01, 01), food);
        Receipt cola = new Receipt(2, "Coca-cola", 50.0, LocalDate.of(2025, 01, 01), food);
        Receipt movieTicket = new Receipt(3, "Movie Ticket", 200.0, LocalDate.of(2025, 02, 10), entertainment);
        Receipt concertTicket = new Receipt(4, "Concert Ticket", 300.0, LocalDate.of(2025, 03, 15), entertainment);
    }

    private void updateLabel() {
        // Set value for the report label
        double totalCost = categoryManager.getTotalCostForYear(selectedYear);
        reportLabel.setText("Total Spent in " + selectedYear + ": " + totalCost + " CZK");
    }
    private void populateChart(int year) {
        // Clear previous data
        reportChart.getData().clear();

        // Create a series for the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Cost per Month");

        // Iterate through months (1 to 12) to calculate total cost per month
        for (int month = 1; month <= 12; month++) {
            double totalCost = categoryManager.getTotalCostForMonthYear(month, year);

            // Add data to the series
            series.getData().add(new XYChart.Data<>(Month.of(month).name(), totalCost));
        }

        // Add the series to the chart
        reportChart.getData().add(series);
    }

    private void populateTable(int year) {
        // Fetch receipts for the selected year
        List<Receipt> yearlyReceipts = categoryManager.giveYearlyReport(year);

        // Update the table
        ObservableList<Receipt> tableData = FXCollections.observableArrayList(yearlyReceipts);
        lastReceiptsTable.setItems(tableData);
    }

    private void initializeTableColumns() {
        // Inicializace sloupců tabulky
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.");
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        costColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getCost()).asObject());
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(formatter.format(cellData.getValue().getDateOfPurchase())));

        // Přidání kontroly null pro purchaseCategory
        categoryColumn.setCellValueFactory(cellData -> {
            PurchaseCategory category = cellData.getValue().getPurchaseCategory();
            return new SimpleStringProperty(category != null ? category.getCategoryName() : "Unknown Category");
        });

        populateTable(selectedYear);
    }


}


