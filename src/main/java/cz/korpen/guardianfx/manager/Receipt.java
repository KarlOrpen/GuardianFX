package cz.korpen.guardianfx.manager;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class Receipt {
    private static final AtomicInteger idCounter = new AtomicInteger(0); // Static AtomicInteger for generating unique IDs

    private final int id;
    private String title;
    private double cost;
    private LocalDate dateOfPurchase;
    private String imagePath;
    private PurchaseCategory purchaseCategory;

    public Receipt(String title, double cost, LocalDate dateOfPurchase, PurchaseCategory purchaseCategory) {
        this.id = idCounter.incrementAndGet();
        this.title = title;
        this.cost = cost;
        this.dateOfPurchase = dateOfPurchase;
        this.purchaseCategory = purchaseCategory;
        purchaseCategory.addReceipt(this);
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

    public void setDateOfPurchase(LocalDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public PurchaseCategory getPurchaseCategory() {
        return purchaseCategory;
    }

    public void deleteReceipt() {
        purchaseCategory.removeReceipt(this);
    }


    public void changeCategory(PurchaseCategory originalCategory, PurchaseCategory newCategory) {
        newCategory.addReceipt(this);
        originalCategory.removeReceipt(this);
        this.purchaseCategory = newCategory;
    }

    @Override
    public String toString() {
        return "Položka: " + getId() + " " + getTitle() + " " + getCost() + "KČ " + getDateOfPurchase();
    }
}
