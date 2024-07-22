package model;

import org.json.JSONObject;

import persistence.Writable;

// A class representing a User with a Expense Limit Per Month,and a over Expense Limit status
public class User implements Writable {

    private double expenseLimit;
    private boolean overExpenseLimit;

    // EFFECTS: constructs a User object, with over expense limit to false and total
    // expense amount to 0 dollars.
    public User() {
        this.expenseLimit = 0;
        this.overExpenseLimit = false;
    }

    // EFFECTS: write the implemtation for the public interface method to make the
    // fields in this class a
    // JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("expenseLimit", expenseLimit);
        json.put("overExpenseLimit", overExpenseLimit);
        return json;
    }

    // EFFECTS: returns the user's expense limit per month
    public double getExpenseLimit() {
        return expenseLimit;
    }

    // MODIFIES: this
    // EFFECTS: returns the user's over expense limit status
    public boolean getOverExpenseLimit(double totalExpense) {
        overExpenseLimit = totalExpense > expenseLimit;
        return overExpenseLimit;
    }

    // MODFIES: this
    // EFFECTS: sets the user's expense limit to the new given limit
    public void setExpenseLimit(double limit) {
        this.expenseLimit = limit;
    }

    // MODFIES: this
    // EFFECTS: sets the user's over expense limit status to the new given status
    public void setOverExpenseLimit(boolean overExpenseLimitStatus) {
        this.overExpenseLimit = overExpenseLimitStatus;
    }

}
