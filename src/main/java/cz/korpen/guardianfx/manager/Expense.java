package cz.korpen.guardianfx.manager;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class Expense {
    private static final AtomicInteger idCounter = new AtomicInteger(0); // Static AtomicInteger for generating unique IDs

    private final int id;
    private String title;
    private double cost;
    private LocalDate dateOfPurchase;
    private String imagePath;
    private ExpenseCategory expenseCategory;

    public Expense(String title, double cost, LocalDate dateOfPurchase, ExpenseCategory expenseCategory) {
        this.id = idCounter.incrementAndGet();
        this.title = title;
        this.cost = cost;
        this.dateOfPurchase = dateOfPurchase;
        this.expenseCategory = expenseCategory;
        expenseCategory.addReceipt(this);
        this.imagePath = "";
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDate(LocalDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ExpenseCategory getExpenseCategory() {
        return expenseCategory;
    }

    public void deleteExpense() {
        expenseCategory.removeReceipt(this);
    }


    public void changeCategory(ExpenseCategory originalCategory, ExpenseCategory newCategory) {
        newCategory.addReceipt(this);
        originalCategory.removeReceipt(this);
        this.expenseCategory = newCategory;
    }

    @Override
    public String toString() {
        return "Položka: " + getId() + " " + getTitle() + " " + getCost() + "KČ " + getDateOfPurchase();
    }
}
