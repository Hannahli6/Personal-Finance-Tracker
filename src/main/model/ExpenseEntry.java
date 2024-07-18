package model;

import java.time.LocalDate;

// A class representing an expense entry with a name, category, expense amount and note
public class ExpenseEntry {

    private String name;
    private String category;
    private double expenseAmount;
    private String note;
    private int id;
    private LocalDate date;

    // REQUIRIES: unique id
    // EFFECTS: constructs an expense entry with name, catgeory, expense amount,
    // note, id
    public ExpenseEntry(String name, String category, double expenseAmount, String note, LocalDate date) {
        this.name = name;
        this.category = category;
        this.expenseAmount = expenseAmount;
        this.note = note;
        this.date = date;
        this.id = 0;
    }

    // getters

    // EFFECTS: returns the expense entry's name
    public String getName() {
        return name;
    }

    // EFFECTS: returns the expense entry's category
    public String getCategory() {
        return category;
    }

    // EFFECTS: returns the expense entry's expense amount
    public double getExpenseAmount() {
        return expenseAmount;
    }

    // EFFECTS: returns the expense entry's note
    public String getNote() {
        return note;
    }

    // EFFECTS: returns the expense entry's note
    public LocalDate getDate() {
        return date;
    }

    // EFFECTS: returns the expense entry's id
    public int getId() {
        return id;
    }

    // setters:

    // MODFIES: this
    // EFFECTS: sets the expense entry's name to the new given name
    public void setName(String name) {
        this.name = name;
    }

    // MODFIES: this
    // EFFECTS: sets the expense entry's category to the new given category
    public void setCategory(String category) {
        this.category = category;
    }

    // MODFIES: this
    // EFFECTS: sets the expense entry's expense amount to the new given amount
    public void setExpenseAmount(double amount) {
        this.expenseAmount = amount;
    }

    // MODFIES: this
    // EFFECTS: sets the expense entry's note to the new given note
    public void setNote(String note) {
        this.note = note;
    }

    // MODFIES: this
    // EFFECTS: sets the expense entry's date to the new given date
    public void setDate(LocalDate date) {
        this.date = date;
    }

    // MODFIES: this
    // EFFECTS: sets the expense entry's id to the new given id
    public void setId(int id) {
        this.id = id;
    }
}