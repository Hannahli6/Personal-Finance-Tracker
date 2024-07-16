package model;

// A class representing a User with a Expense Limit Per Month, total Expense Amount, and a over Expense Limit status
public class User {

    private double ExpenseLimitPerMonth;
    private double totalExpenseAmount;
    private boolean overExpenseLimit;

    // EFFECTS: constructs a User object, with over expense limit to false and total expense amount to 0 dollars.
    public User(double limit) {
        this.ExpenseLimitPerMonth = limit; 
        this.totalExpenseAmount = 0;
        this.overExpenseLimit = false;
    }

    // EFFECTS: returns the user's expense limit per month
    public double getExpenseLimitPerMonth () {
        return ExpenseLimitPerMonth;
    };
    // EFFECTS: returns the user's total expense amount
    public double getTotalExpenseAmount () {
        return totalExpenseAmount;
    };

    // EFFECTS: returns the user's over expense limit status
    public boolean getOverExpenseLimit () {
        overExpenseLimit = totalExpenseAmount > ExpenseLimitPerMonth;
        return overExpenseLimit;
    };

    //MODFIES: this
    //EFFECTS: sets the user's expense limit per month to the new given limit
    public void setExpenseLimitPerMonth (double limit) {
        this.ExpenseLimitPerMonth = limit;
    }

    //MODFIES: this
    //EFFECTS: sets the user's total expense amount to the new given amount
    public void setTotalExpenseAmount (double newExpenseAmount) {
        this.totalExpenseAmount = newExpenseAmount;
    }
}
