package cz.korpen.guardianfx.manager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryManager {
    private List<PurchaseCategory> purchaseCategories;
    private List<IncomeCategory> incomeCategories;

    // Private constructor to prevent instantiation
    private CategoryManager() {
        this.purchaseCategories = new ArrayList<>();
        this.incomeCategories = new ArrayList<>();
        IncomeCategory salary = new IncomeCategory("Salary");
        PurchaseCategory food = new PurchaseCategory("FOOD", "Food");
        PurchaseCategory entertainment = new PurchaseCategory("ENTERTAINMENT", "Entertainment");
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
    public void addPurchaseCategory(PurchaseCategory purchaseCategory) {
        purchaseCategories.add(purchaseCategory);
    }

    public void addIncomeCategory(IncomeCategory incomeCategory) {
        incomeCategories.add(incomeCategory);
    }

    // Remove a category
    public void removePurchaseCategory(PurchaseCategory purchaseCategory) {
        purchaseCategories.remove(purchaseCategory);
    }

    public void removeIncomeCategory(IncomeCategory incomeCategory) {
        purchaseCategories.remove(incomeCategory);
    }


    // Get all purchaseCategories
    public List<PurchaseCategory> getPurchaseCategories() {
        return new ArrayList<>(purchaseCategories); // Return an immutable copy
    }

    public List<IncomeCategory> getIncomeCategories() {
        return new ArrayList<>(incomeCategories); // Return an immutable copy
    }

    // Generate yearly report for all purchaseCategories
    public Map<PurchaseCategory, List<Receipt>> generateYearlyReportsByCategory(int year) {
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
    public Map<PurchaseCategory, Double> calculateTotalCostPerCategory(int year) {
        return purchaseCategories.stream()
                .collect(Collectors.toMap(
                        category -> category,
                        category -> category.calculateTotalCost(year)
                ));
    }

    // Calculate total costs across all purchaseCategories for a given year
    public double calculateTotalCostAcrossCategories(int year) {
        return purchaseCategories.stream()
                .mapToDouble(category -> category.calculateTotalCost(year))
                .sum();
    }

    // Get a sorted list of all receipts across purchaseCategories by date
    public List<Receipt> getAllReceiptsSortedByDate() {
        return purchaseCategories.stream()
                .flatMap(category -> category.getReceipts().stream()) // Combine all receipts
                .sorted(Comparator.comparing(Receipt::getDateOfPurchase)) // Sort by date
                .collect(Collectors.toList());
    }

    public double getTotalCostForYear(int year) {
        double totalCost = 0.0;
        for (PurchaseCategory category : purchaseCategories) {
            for (Receipt receipt : category.getReceipts()) {
                if (receipt.getDateOfPurchase().getYear() == year) {
                    totalCost += receipt.getCost();  // Sum the cost of receipts in the selected year
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
                .mapToDouble(Receipt::getCost) // Extract the cost
                .sum(); // Sum up the costs
    }

    public double getTotalIncomeForMonthYear(int month, int year) {
        return incomeCategories.stream()
                .flatMap(category -> category.getIncomes().stream()) // Flatten all receipts
                .filter(receipt -> receipt.getDate().getMonthValue() == month && receipt.getDate().getYear() == year) // Filter by month and year
                .mapToDouble(Income::getAmount) // Extract the cost
                .sum(); // Sum up the costs
    }

    public List<Receipt> giveYearlyCostReport(int year) {
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
