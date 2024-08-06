package model;

import java.time.LocalDate;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;


// The ExpenseTracker class represents the 
// the list of expense entries and : 
// - add expense entries method
// - delete expense entries method
// - edit their expense entries method
// - get total expense amount method

public class ExpenseTracker implements Writable {

    private ArrayList<ExpenseEntry> listOfExpenseEntries;
    private int largestIdNum;
    private User user;

    // EFFECTS: creates an instance of the ExpenseTrackerApp console ui application
    public ExpenseTracker() {
        // initalize empty list of entries
        user = new User();
        listOfExpenseEntries = new ArrayList<>();
        largestIdNum = 0;
    }

    // EFFECTS: write the implemtation for the public interface method to return one Json object of 
    //          the user info json object, the name of this expense tracker and a json array of list of expense entries
    // Note: we override here because Expense Entry class & User also implements the Writable class!
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("listOfExpenseEntries", listOfExpenseEntriesToJson());
        json.put("user", user.toJson());
        return json;
    }

     // EFFECTS: returns every expense entry as a json object and placed into a new json array
    public JSONArray listOfExpenseEntriesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (ExpenseEntry entry : listOfExpenseEntries) {
            jsonArray.put(entry.toJson());
        }
        return jsonArray;
    }


    // MODIFIES: this
    // EFFECTS: adds a new Expense Entry to the list of expense entries
    public void addExpenseEntry(ExpenseEntry newEntry) {
        int uniqueId;

        listOfExpenseEntries.add(newEntry);
        uniqueId = largestIdNum;
        largestIdNum++;
        newEntry.setId(uniqueId);
        EventLog.getInstance().logEvent(new Event("Added a new expense entry" + "ID: " + uniqueId));
    }

    // REQUIRES: at least one expense entry & id given is valid
    // MODIFIES: ExpenseEntry
    // EFFECTS: edits an Expense Entry given new expense entry information
    public void editExpenseEntry(String name, String category, double amount, String note, LocalDate date, int id) {
        // finds an expense entry with the same given id from the list of entries
        // use a bunch of set methods to edit that entry
        ExpenseEntry toBeEditedExpenseEntry = findExpenseEntry(id);
        toBeEditedExpenseEntry.setName(name);
        toBeEditedExpenseEntry.setCategory(category);
        toBeEditedExpenseEntry.setExpenseAmount(amount);
        toBeEditedExpenseEntry.setDate(date);
        toBeEditedExpenseEntry.setNote(note);
        EventLog.getInstance().logEvent(new Event("Edit Expense Entry"));
    }

    // REQUIRES: id to be valid
    // MODIFIES: this
    // EFFECTS: deletes a new Expense Entry from the list of expense entries
    public void deleteExpenseEntry(int id) {
        // finds an expense entry with the same given id from the list of entries
        // remove from list
        listOfExpenseEntries.remove(findExpenseEntry(id));
        EventLog.getInstance().logEvent(new Event("Deleted Expense Entry" + "ID: " + id));
    }

    // EFFECTS: get the list of expense entries
    public ArrayList<ExpenseEntry> getListOfExpenseEntries() {
        return listOfExpenseEntries;
    }

    // EFFECTS: find and return the expense entry given the entry id, otherwise
    // return null
    public ExpenseEntry findExpenseEntry(int id) {
        for (ExpenseEntry entry : listOfExpenseEntries) {
            if (id == entry.getId()) {
                return entry;
            }
        }
        return null;
    }

    // EFFECTS: get the total expense amount
    public double getTotalExpenseAmount() {
        double total = 0;
        for (ExpenseEntry entry : listOfExpenseEntries) {
            total += entry.getExpenseAmount();
        }
        return total;
    }

    public User getUser() {
        return user;
    }

}