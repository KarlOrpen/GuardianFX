package cz.korpen.guardianfx;

import java.time.LocalDate;

public class Receipt {
    private int id;
    private String title;
    private double cost;
    private LocalDate dateOfPurchase;
    private String imagePath;
    private PurchaseCategory purchaseCategory;

    public Receipt(int id, String title, double cost, LocalDate dateOfPurchase, PurchaseCategory purchaseCategory) {
        this.id = id;
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

    public void setId(int id) {
        this.id = id;
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

    public void setPurchaseCategory(PurchaseCategory purchaseCategory) {
        this.purchaseCategory = purchaseCategory;
    }

    @Override
    public String toString() {
        return "Položka: " + getId() + " " + getTitle() + " " + getCost() + "KČ " + getDateOfPurchase();
    }
}
