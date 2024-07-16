package model;

import java.util.ArrayList;

// A expense tracker application that allows the user to 
// - add expense entries
// - delete expense entries
// - edit their expense entries
// - display ALL expense entries or display specific category expense entries
public class ExpenseTracker {

    private ArrayList<ExpenseEntry> listOfExpenseEntries;
    //private final String LISTOFCATEGORIES[] = { "Transportation", "Bills", "Education",
    // "Entertainment", "Food", "Sport" };

    // EFFECTS: creates an instance of the ExpenseTrackerApp console ui application
    public ExpenseTracker() {
        // initalize empty list of entries
        listOfExpenseEntries = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds a new Expense Entry to the list of expense entries
    public void addExpenseEntry(ExpenseEntry newEntry) {
        listOfExpenseEntries.add(newEntry);
    }

    //REQUIRES: at least one expense entry & id given is valid
    // MODIFIES: ExpenseEntry
    // EFFECTS: edits an Expense Entry given new expense entry information
    public void editExpenseEntry(String name, String category, double amount, String note, int id) {
        // finds an expense entry with the same given id from the list of entries
        // use a bunch of set methods to edit that entry
        ExpenseEntry toBeEditedExpenseEntry = findExpenseEntry(id);
        toBeEditedExpenseEntry.setName(name);
        toBeEditedExpenseEntry.setCategory(category);
        toBeEditedExpenseEntry.setExpenseAmount(amount);
        toBeEditedExpenseEntry.setNote(note);
    }

    //REQUIRES: id to be valid
    // MODIFIES: this
    // EFFECTS: deletes a new Expense Entry from the list of expense entries
    public void deleteExpenseEntry(int id) {
        // finds an expense entry with the same given id from the list of entries
        // remove from list
        listOfExpenseEntries.remove(findExpenseEntry(id));
    }

    // EFFECTS: get the list of expense entries
    public ArrayList<ExpenseEntry> getListOfExpenseEntries() {
        return listOfExpenseEntries;
    }

    // EFFECTS: find and return the expense entry given the entry id, otherwise return null
    public ExpenseEntry findExpenseEntry(int id) {
        for (ExpenseEntry entry : listOfExpenseEntries) {
            if (id == entry.getId()) {
                return entry;
            }
        }
        return null;
    }

}