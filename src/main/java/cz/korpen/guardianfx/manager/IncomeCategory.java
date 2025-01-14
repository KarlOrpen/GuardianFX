package cz.korpen.guardianfx.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class IncomeCategory {
    private static final AtomicInteger idCounter = new AtomicInteger(0); // Static AtomicInteger for generating unique IDs

    private String categoryName;
    private int id;
    private List<Income> incomeList;

    public IncomeCategory(String categoryName) {
        this.id = idCounter.incrementAndGet();
        this.categoryName = categoryName;
        incomeList = new ArrayList<>();
    }// Generate yearly report for this category
    public List<Income> giveYearlyReport(int year) {
        return incomeList.stream()
                .filter(receipt -> receipt.getDate().getYear() == year)
                .collect(Collectors.toList());
    }

    // Calculate total cost for this category in a given year
    public double calculateTotalCost(int year) {
        return incomeList.stream()
                .filter(receipt -> receipt.getDate().getYear() == year)
                .mapToDouble(Income::getAmount)
                .sum();
    }

    // Add a receipt to the category
    public void addIncome(Income income) {
        incomeList.add(income);
    }

    // Remove a receipt from the category
    public void removeIncome(Income income) {
        incomeList.remove(income);
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

    public List<Income> getIncomes() {
        return new ArrayList<>(incomeList); // Return an immutable copy
    }

    @Override
    public String toString() {
        return categoryName;
    }
}

