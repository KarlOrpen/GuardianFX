package cz.korpen.guardianfx.manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryManager {
    private List<ExpenseCategory> purchaseCategories;
    private List<IncomeCategory> incomeCategories;

    // Private constructor to prevent instantiation
    private CategoryManager() {
        this.purchaseCategories = new ArrayList<>();
        this.incomeCategories = new ArrayList<>();
        IncomeCategory salary = new IncomeCategory("Salary");
        ExpenseCategory food = new ExpenseCategory("FOOD", "Food");
        ExpenseCategory entertainment = new ExpenseCategory("ENTERTAINMENT", "Entertainment");
        addPurchaseCategory(food);
        addPurchaseCategory(entertainment);
        addIncomeCategory(salary);

    }

    // Inner static class responsible for holding the singleton instance
    private static class SingletonHelper {
        // This will be loaded when it is referenced (lazy initialization)
        private static final CategoryManager INSTANCE = new CategoryManager();
    }

    // Public method to access the singleton instance
    public static CategoryManager getInstance() {
        return SingletonHelper.INSTANCE;
    }

    // Add a new category
    public void addPurchaseCategory(ExpenseCategory expenseCategory) {
        purchaseCategories.add(expenseCategory);
    }

    public void addIncomeCategory(IncomeCategory incomeCategory) {
        incomeCategories.add(incomeCategory);
    }

    // Remove a category
    public void removePurchaseCategory(ExpenseCategory expenseCategory) {
        purchaseCategories.remove(expenseCategory);
    }

    public void removeIncomeCategory(IncomeCategory incomeCategory) {
        purchaseCategories.remove(incomeCategory);
    }


    // Get all purchaseCategories
    public List<ExpenseCategory> getPurchaseCategories() {
        return new ArrayList<>(purchaseCategories); // Return an immutable copy
    }

    public List<IncomeCategory> getIncomeCategories() {
        return new ArrayList<>(incomeCategories); // Return an immutable copy
    }

    // Generate yearly report for all purchaseCategories
    public Map<ExpenseCategory, List<Expense>> generateYearlyReportsByCategory(int year) {
        return purchaseCategories.stream()
                .flatMap(category -> category.giveYearlyReport(year).stream()
                        .map(receipt -> Map.entry(category, receipt))) // Map each receipt to its category
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,                      // Group by category
                        Collectors.mapping(Map.Entry::getValue, // Collect receipts per category
                                Collectors.toList())
                ));
    }

    // Calculate total costs per category for a given year
    public Map<String, Double> calculateTotalCostPerCategory(int year) {
        return purchaseCategories.stream()
                .collect(Collectors.toMap(
                        ExpenseCategory::getCategoryName,  // Use category name as the key
                        category -> category.calculateTotalCost(year) // Total cost as the value
                ));
    }

    // Calculate total costs per category for a given year
    public Map<String, Double> calculateTotalIncomePerCategory(int year) {
        return incomeCategories.stream()
                .collect(Collectors.toMap(
                        IncomeCategory::getCategoryName,  // Use category name as the key
                        category -> category.calculateTotalIncome(year) // Total cost as the value
                ));
    }

    // Calculate total costs across all purchaseCategories for a given year
    public double calculateTotalCostAcrossCategories(int year) {
        return purchaseCategories.stream()
                .mapToDouble(category -> category.calculateTotalCost(year))
                .sum();
    }

    // Get a sorted list of all receipts across purchaseCategories by date
    public List<Expense> getAllReceiptsSortedByDate() {
        return purchaseCategories.stream()
                .flatMap(category -> category.getReceipts().stream()) // Combine all receipts
                .sorted(Comparator.comparing(Expense::getDateOfPurchase)) // Sort by date
                .collect(Collectors.toList());
    }

    public double getTotalCostForYear(int year) {
        double totalCost = 0.0;
        for (ExpenseCategory category : purchaseCategories) {
            for (Expense expense : category.getReceipts()) {
                if (expense.getDateOfPurchase().getYear() == year) {
                    totalCost += expense.getCost();  // Sum the cost of receipts in the selected year
                }
            }
        }
        return totalCost;
    }

    public double getTotalIncomeForYear(int year) {
        double totalIncome = 0.0;
        for (IncomeCategory category : incomeCategories) {
            for (Income income : category.getIncomes()) {
                if (income.getDate().getYear() == year) {
                    totalIncome += income.getAmount();  // Sum the cost of receipts in the selected year
                }
            }
        }
        return totalIncome;
    }

    public double getTotalCostForMonthYear(int month, int year) {
        return purchaseCategories.stream()
                .flatMap(category -> category.getReceipts().stream()) // Flatten all receipts
                .filter(receipt -> receipt.getDateOfPurchase().getMonthValue() == month && receipt.getDateOfPurchase().getYear() == year) // Filter by month and year
                .mapToDouble(Expense::getCost) // Extract the cost
                .sum(); // Sum up the costs
    }

    public double getTotalIncomeForMonthYear(int month, int year) {
        return incomeCategories.stream()
                .flatMap(category -> category.getIncomes().stream()) // Flatten all receipts
                .filter(receipt -> receipt.getDate().getMonthValue() == month && receipt.getDate().getYear() == year) // Filter by month and year
                .mapToDouble(Income::getAmount) // Extract the cost
                .sum(); // Sum up the costs
    }

    public List<Expense> giveYearlyCostReport(int year) {
        return purchaseCategories.stream()
                .flatMap(category -> category.getReceipts().stream())
                .filter(receipt -> receipt.getDateOfPurchase().getYear() == year)
                .collect(Collectors.toList());
    }

    public List<Income> giveYearlyIncomeReport(int year) {
        return incomeCategories.stream()
                .flatMap(category -> category.getIncomes().stream())
                .filter(receipt -> receipt.getDate().getYear() == year)
                .collect(Collectors.toList());
    }

}
