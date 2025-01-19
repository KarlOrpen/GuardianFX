package cz.korpen.guardianfx.manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ExpenseCategory {
    private static final AtomicInteger idCounter = new AtomicInteger(0); // Static AtomicInteger for generating unique IDs
    private int id;
    private String categoryName;
    private String description;
    private List<Expense> expenses;

    public ExpenseCategory(String categoryName, String description) {
        this.id = idCounter.incrementAndGet();
        this.categoryName = categoryName;
        this.description = description;
        this.expenses = new ArrayList<>();
    }

    // Sort expenses by date
    public List<Expense> sortCategoryByDate() {
        return expenses.stream()
                .sorted(Comparator.comparing(Expense::getDateOfPurchase))
                .collect(Collectors.toList());
    }

    // Generate yearly report for this category
    public List<Expense> giveYearlyReport(int year) {
        return expenses.stream()
                .filter(receipt -> receipt.getDateOfPurchase().getYear() == year)
                .collect(Collectors.toList());
    }

    // Calculate total cost for this category in a given year
    public double calculateTotalCost(int year) {
        return expenses.stream()
                .filter(receipt -> receipt.getDateOfPurchase().getYear() == year)
                .mapToDouble(Expense::getCost)
                .sum();
    }

    // Add a expense to the category
    public void addReceipt(Expense expense) {
        expenses.add(expense);
    }

    // Remove a expense from the category
    public void removeReceipt(Expense expense) {
        expenses.remove(expense);
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Expense> getReceipts() {
        return new ArrayList<>(expenses); // Return an immutable copy
    }

    @Override
    public String toString() {
        return categoryName;
    }
}
