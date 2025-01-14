package cz.korpen.guardianfx;

import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryManager {
    private List<PurchaseCategory> categories;

    public CategoryManager() {
        this.categories = new ArrayList<>();
    }

    // Add a new category
    public void addCategory(PurchaseCategory category) {
        categories.add(category);
    }

    // Remove a category
    public void removeCategory(PurchaseCategory category) {
        categories.remove(category);
    }

    // Get all categories
    public List<PurchaseCategory> getCategories() {
        return new ArrayList<>(categories); // Return an immutable copy
    }

    // Generate yearly report for all categories
    public Map<PurchaseCategory, List<Receipt>> generateYearlyReportsByCategory(int year) {
        return categories.stream()
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
        return categories.stream()
                .collect(Collectors.toMap(
                        category -> category,
                        category -> category.calculateTotalCost(year)
                ));
    }

    // Calculate total costs across all categories for a given year
    public double calculateTotalCostAcrossCategories(int year) {
        return categories.stream()
                .mapToDouble(category -> category.calculateTotalCost(year))
                .sum();
    }

    // Get a sorted list of all receipts across categories by date
    public List<Receipt> getAllReceiptsSortedByDate() {
        return categories.stream()
                .flatMap(category -> category.getReceipts().stream()) // Combine all receipts
                .sorted(Comparator.comparing(Receipt::getDateOfPurchase)) // Sort by date
                .collect(Collectors.toList());
    }

    public double getTotalCostForYear(int year) {
        double totalCost = 0.0;
        for (PurchaseCategory category : categories) {
            for (Receipt receipt : category.getReceipts()) {
                if (receipt.getDateOfPurchase().getYear() == year) {
                    totalCost += receipt.getCost();  // Sum the cost of receipts in the selected year
                }
            }
        }
        return totalCost;
    }

    public double getTotalCostForMonthYear(int month, int year) {
        return categories.stream()
                .flatMap(category -> category.getReceipts().stream()) // Flatten all receipts
                .filter(receipt -> receipt.getDateOfPurchase().getMonthValue() == month && receipt.getDateOfPurchase().getYear() == year) // Filter by month and year
                .mapToDouble(Receipt::getCost) // Extract the cost
                .sum(); // Sum up the costs
    }

    public List<Receipt> giveYearlyReport(int year) {
        return categories.stream()
                .flatMap(category -> category.getReceipts().stream())
                .filter(receipt -> receipt.getDateOfPurchase().getYear() == year)
                .collect(Collectors.toList());
    }
}
