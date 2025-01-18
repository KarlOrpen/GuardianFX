package cz.korpen.guardianfx.manager;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class Income {
    private static final AtomicInteger idCounter = new AtomicInteger(0); // Static AtomicInteger for generating unique IDs
    private String title;
    private double amount;
    private final int id;
    private LocalDate date;
    private IncomeCategory incomeCategory;

    public Income(String title, double amount, LocalDate date, IncomeCategory incomeCategory) {
        this.id = idCounter.incrementAndGet();
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.incomeCategory = incomeCategory;
        incomeCategory.addIncome(this);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void deleteIncome() {
        incomeCategory.removeIncome(this);
    }

    public IncomeCategory getIncomeCategory() {
        return incomeCategory;
    }

    public void changeCategory(IncomeCategory originalCategory, IncomeCategory newCategory) {
        newCategory.addIncome(this);
        originalCategory.removeIncome(this);
    }

    @Override
    public String toString() {
        return "Položka: " + getId() + " " + getTitle() + " " + getAmount() + "KČ " + getDate();
    }
}
