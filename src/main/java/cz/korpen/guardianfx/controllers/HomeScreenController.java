package cz.korpen.guardianfx.controllers;

import cz.korpen.guardianfx.manager.Expense;
import cz.korpen.guardianfx.manager.ExpenseCategory;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

public class HomeScreenController extends BaseController {

    private int selectedYear;

    @FXML
    private TableView<Expense> lastReceiptsTable;

    @FXML
    private BarChart<String, Number> reportChart;

    @FXML
    private Label reportLabel;

    @FXML
    private Spinner<Integer> yearSpinner;

    @FXML
    private TableColumn<Expense, Integer> idColumn;
    @FXML
    private TableColumn<Expense, String> titleColumn;
    @FXML
    private TableColumn<Expense, Double> costColumn;
    @FXML
    private TableColumn<Expense, String> dateColumn;
    @FXML
    private TableColumn<Expense, String> categoryColumn;

    public void initialize() {
        selectedYear = LocalDate.now().getYear();
        initializeSpinner();
        populateChart(selectedYear);
    }


    public void addTestReceipts() {
        // Create sample categories and add them to the category manager
        ExpenseCategory food = new ExpenseCategory("FOOD", "Food");
        ExpenseCategory entertainment = new ExpenseCategory("ENTERTAINMENT", "Entertainment");
        categoryManager.addExpenseCategory(food);
        categoryManager.addExpenseCategory(entertainment);

        // Create sample receipts and add them to the categories
        Expense hamburger = new Expense("Hamburger", 100.0, LocalDate.of(2025, 01, 01), food);
        Expense cola = new Expense("Coca-cola", 50.0, LocalDate.of(2025, 01, 01), food);
        Expense movieTicket = new Expense("Movie Ticket", 200.0, LocalDate.of(2025, 02, 10), entertainment);
        Expense concertTicket = new Expense("Concert Ticket", 300.0, LocalDate.of(2025, 03, 15), entertainment);
    }

    public void initializeSpinner() {
        // Initialize the spinner to select years
        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(LocalDate.now().minusYears(25).getYear(), LocalDate.now().plusYears(2).getYear(), LocalDate.now().getYear()));
        yearSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedYear = newValue;
            updateLabel();
            populateChart(selectedYear);
            populateTable(selectedYear);
        });
    }

    private void updateLabel() {
        // Set value for the report label
        double totalCost = categoryManager.getTotalCostForYear(selectedYear);
        double totalIncome = categoryManager.getTotalIncomeForYear(selectedYear);
        reportLabel.setText("Total balance in " + selectedYear + ": " + (totalIncome - totalCost) + " CZK");
    }
    private void populateChart(int year) {
        // Clear previous data
        reportChart.getData().clear();

        reportChart.getStylesheets().add(getClass().getResource("/cz/korpen/guardianfx/BarChartStyles.css").toExternalForm());

        // Create a series for Total Cost per Month
        XYChart.Series<String, Number> costSeries = new XYChart.Series<>();
        costSeries.setName(resources.getString("expensesBar"));

        // Create a series for Monthly Income
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName(resources.getString("incomesBar"));

        // Iterate through months (1 to 12) to calculate total cost and income per month
        for (int month = 1; month <= 12; month++) {
            // Calculate total cost and income for the month
            double totalCost = categoryManager.getTotalCostForMonthYear(month, year);
            double monthlyIncome = categoryManager.getTotalIncomeForMonthYear(month, year);

            // Add data to the respective series
            costSeries.getData().add(new XYChart.Data<>(Month.of(month).name(), totalCost));
            incomeSeries.getData().add(new XYChart.Data<>(Month.of(month).name(), monthlyIncome));
        }
        // Add both series to the chart
        reportChart.getData().addAll(costSeries, incomeSeries);
    }


    private void populateTable(int year) {
        // Fetch receipts for the selected year
        List<Expense> yearlyExpenses = categoryManager.giveYearlyCostReport(year);

        // Update the table
        ObservableList<Expense> tableData = FXCollections.observableArrayList(yearlyExpenses);
        lastReceiptsTable.setItems(tableData);
    }

    private void initializeTableColumns() {
        // Initialization of table columns
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.");
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        costColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getCost()).asObject());
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(formatter.format(cellData.getValue().getDateOfPurchase())));

        // Null control for purchaseCategory
        categoryColumn.setCellValueFactory(cellData -> {
            ExpenseCategory category = cellData.getValue().getExpenseCategory();
            return new SimpleStringProperty(category != null ? category.getCategoryName() : "Unknown Category");
        });

        populateTable(selectedYear);
    }

    private void applyBarColors(XYChart.Series<String, Number> costSeries, XYChart.Series<String, Number> incomeSeries) {
        // Change color of Total Cost bars (Red)
        for (XYChart.Data<String, Number> data : costSeries.getData()) {
            // Ensure node exists before trying to set the style
            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-bar-fill: red;");
            }
        }

        // Change color of Monthly Income bars (Green)
        for (XYChart.Data<String, Number> data : incomeSeries.getData()) {
            // Ensure node exists before trying to set the style
            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-bar-fill: green;");
            }
        }
    }
}


