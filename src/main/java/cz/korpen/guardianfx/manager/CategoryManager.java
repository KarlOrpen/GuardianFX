package cz.korpen.guardianfx.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryManager {
    private final List<ExpenseCategory> expenseCategories;
    private final List<IncomeCategory> incomeCategories;

    // Private constructor to prevent instantiation
    private CategoryManager() {
        this.expenseCategories = new ArrayList<>();
        this.incomeCategories = new ArrayList<>();
        IncomeCategory salary = new IncomeCategory("Salary");
        ExpenseCategory food = new ExpenseCategory("FOOD", "Food");
        ExpenseCategory entertainment = new ExpenseCategory("ENTERTAINMENT", "Entertainment");
        addExpenseCategory(food);
        addExpenseCategory(entertainment);
        addIncomeCategory(salary);

    }

    // Inner static class responsible for holding the singleton instance
    private static class SingletonHelper {
        private static final CategoryManager INSTANCE = new CategoryManager();
    }

    // Public method to access the singleton instance
    public static CategoryManager getInstance() {
        return SingletonHelper.INSTANCE;
    }

    // Add a new category
    public void addExpenseCategory(ExpenseCategory expenseCategory) {
        expenseCategories.add(expenseCategory);
    }

    public void addIncomeCategory(IncomeCategory incomeCategory) {
        incomeCategories.add(incomeCategory);
    }

    // Remove a category
    public void removeExpenseCategory(ExpenseCategory expenseCategory) {
        expenseCategories.remove(expenseCategory);
    }

    public void removeIncomeCategory(IncomeCategory incomeCategory) {
        incomeCategories.remove(incomeCategory);
    }


    // Get all purchaseCategories
    public List<ExpenseCategory> getExpenseCategories() {
        return new ArrayList<>(expenseCategories); // Return an immutable copy
    }

    public List<IncomeCategory> getIncomeCategories() {
        return new ArrayList<>(incomeCategories); // Return an immutable copy
    }

    public List<Expense> getExpensesOverFiveK() {
        List<Expense> result = new ArrayList<>();
        for (ExpenseCategory expenseCategory : expenseCategories) {
            for (Expense expense : expenseCategory.getReceipts()) {
                if (expense.getCost() >= 5000) {
                    result.add(expense);
                }
            }
        } return result;
    }


    // Calculate total costs per category for a given year
    public Map<String, Double> calculateTotalCostPerCategory(int year) {
        return expenseCategories.stream()
                .collect(Collectors.toMap(
                        ExpenseCategory::getCategoryName,  // Use category name as the key
                        category -> category.calculateTotalCost(year) // Total cost as the value
                ));
    }

    // Calculate total costs per category for a given year
    public Map<String, Double> calculateTotalIncomePerCategory(int year) {
        return incomeCategories.stream()
                .collect(Collectors.toMap(
                        IncomeCategory::getCategoryName,  // Use category name as the key
                        category -> category.calculateTotalIncome(year) // Total cost as the value
                ));
    }

    public double getTotalCostForYear(int year) {
        double totalCost = 0.0;
        for (ExpenseCategory category : expenseCategories) {
            for (Expense expense : category.getReceipts()) {
                if (expense.getDateOfPurchase().getYear() == year) {
                    totalCost += expense.getCost();  // Sum the cost of receipts in the selected year
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
        return expenseCategories.stream()
                .flatMap(category -> category.getReceipts().stream()) // Flatten all receipts
                .filter(receipt -> receipt.getDateOfPurchase().getMonthValue() == month && receipt.getDateOfPurchase().getYear() == year) // Filter by month and year
                .mapToDouble(Expense::getCost) // Extract the cost
                .sum(); // Sum up the costs
    }

    public double getTotalIncomeForMonthYear(int month, int year) {
        return incomeCategories.stream()
                .flatMap(category -> category.getIncomes().stream()) // Flatten all receipts
                .filter(receipt -> receipt.getDate().getMonthValue() == month && receipt.getDate().getYear() == year) // Filter by month and year
                .mapToDouble(Income::getAmount) // Extract the cost
                .sum(); // Sum up the costs
    }

    public List<Expense> giveYearlyCostReport(int year) {
        return expenseCategories.stream()
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
