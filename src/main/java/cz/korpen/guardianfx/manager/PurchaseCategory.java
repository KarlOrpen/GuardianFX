package cz.korpen.guardianfx.manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PurchaseCategory {
    private static final AtomicInteger idCounter = new AtomicInteger(0); // Static AtomicInteger for generating unique IDs
    private int id;
    private String categoryName;
    private String description;
    private List<Receipt> receipts;

    public PurchaseCategory(String categoryName, String description) {
        this.id = idCounter.incrementAndGet();
        this.categoryName = categoryName;
        this.description = description;
        this.receipts = new ArrayList<>();
    }

    // Sort receipts by date
    public List<Receipt> sortCategoryByDate() {
        return receipts.stream()
                .sorted(Comparator.comparing(Receipt::getDateOfPurchase))
                .collect(Collectors.toList());
    }

    // Generate yearly report for this category
    public List<Receipt> giveYearlyReport(int year) {
        return receipts.stream()
                .filter(receipt -> receipt.getDateOfPurchase().getYear() == year)
                .collect(Collectors.toList());
    }

    // Calculate total cost for this category in a given year
    public double calculateTotalCost(int year) {
        return receipts.stream()
                .filter(receipt -> receipt.getDateOfPurchase().getYear() == year)
                .mapToDouble(Receipt::getCost)
                .sum();
    }

    // Add a receipt to the category
    public void addReceipt(Receipt receipt) {
        receipts.add(receipt);
    }

    // Remove a receipt from the category
    public void removeReceipt(Receipt receipt) {
        receipts.remove(receipt);
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

    public List<Receipt> getReceipts() {
        return new ArrayList<>(receipts); // Return an immutable copy
    }

    @Override
    public String toString() {
        return categoryName;
    }
}
