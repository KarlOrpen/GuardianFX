package cz.korpen.guardianfx;

import java.util.List;

public class YearlyReport {
    private List<Receipt> receipts;
    private double totalCost;

    public YearlyReport(List<Receipt> receipts, double totalCost) {
        this.receipts = receipts;
        this.totalCost = totalCost;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public double getTotalCost() {
        return totalCost;
    }

    @Override
    public String toString() {
        return "Total Cost: " + totalCost + ", Receipts: " + receipts;
    }
}
