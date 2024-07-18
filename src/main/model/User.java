package model;


// A class representing a User with a Expense Limit Per Month, total Expense Amount, and a over Expense Limit status
public class User {

    private double expenseLimitPerMonth;
    private boolean overExpenseLimit;

    // EFFECTS: constructs a User object, with over expense limit to false and total expense amount to 0 dollars.
    public User() {
        this.expenseLimitPerMonth = 0; 
        this.overExpenseLimit = false;
    }

    // EFFECTS: returns the user's expense limit per month
    public double getExpenseLimitPerMonth () {
        return expenseLimitPerMonth;
    };
   
    //MODIFIES: this
    // EFFECTS: returns the user's over expense limit status
    public boolean getOverExpenseLimit (double totalExpense) {
        overExpenseLimit = totalExpense > expenseLimitPerMonth;
        return overExpenseLimit;
    };

    //MODFIES: this
    //EFFECTS: sets the user's expense limit per month to the new given limit
    public void setExpenseLimitPerMonth (double limit) {
        this.expenseLimitPerMonth = limit;
    }


}
