package ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import exception.*;
import model.*;
import persistence.JsonViewer;
import persistence.JsonWriter;

public class ExpenseTrackerAppGUI {

    private static final String JSON_STORE = "./data/expensetracker.json";
    private JsonWriter jsonWriter;
    private JsonViewer jsonReader;

    JLabel nameLabel;
    JLabel amountLabel;
    JFrame frame;
    JPanel addEntryField = new JPanel();
    JPanel buttonJPanel = new JPanel();
    JPanel tableJPanel = new JPanel();
    JPanel statusMessageJPanel = new JPanel();

    JTextField nameTextField = new JTextField(10);
    JTextField categoryTextField = new JTextField(10);
    JTextField amountTextField = new JTextField(10);
    JTextField noteTextField = new JTextField(10);
    JTextField dateTextField = new JTextField(10);
    JComboBox<Categories> categoryDropdown;

    JLabel totalExpenseJLabel;
    JLabel expenseLimitJLabel;
    JLabel expenseStatusJLabel;

    DefaultTableModel tableModel;

    ExpenseTracker expenseTracker;

    public ExpenseTrackerAppGUI() throws FileNotFoundException {
        // initalize
        initializeApp();

        // Create the frame (window)
        frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 750);

        tableModel = new DefaultTableModel(
                new Object[] { "Name", "Category", "Amount", "note", "Date", "Id" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable expenseTable = new JTable(tableModel);
        expenseTable.setFillsViewportHeight(true);
        JScrollPane scrollPaneExpenseTable = new JScrollPane(expenseTable);

        // buttons
        JButton addEntryButton = new JButton("add");
        JButton loadButton = new JButton("load");
        JButton saveButton = new JButton("save");
        JButton deleteButton = new JButton("delete");
        JButton editExpenseLimitButton = new JButton("Edit Expense Limit");

        // Create label
        expenseLimitJLabel = new JLabel("Expense Limit: $" + expenseTracker.getUser().getExpenseLimit());
        totalExpenseJLabel = new JLabel("Total Expense Amount: $" + expenseTracker.getTotalExpenseAmount());
        expenseStatusJLabel = new JLabel("Expense Status: ");

        // =========================================
        // EVENT Listerners

        // EFFECTS: on click button to add each of those entry details to an expense
        // entry
        // if date is invalid, throw date exception
        // if expense Amount is invalid, throw expense amount invalid exception
        // if expense amount is negative, throw negative amount exception
        editExpenseLimitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String expenseLimitString = JOptionPane.showInputDialog("Enter a new expense limit: ");
                    double limit = Double.parseDouble(expenseLimitString);
                    if (limit < 0) {
                        throw new NegativeAmountException();
                    }
                    expenseTracker.getUser().setExpenseLimit(limit);
                    update();
                } catch (NumberFormatException msg) {
                    JOptionPane.showMessageDialog(frame, "Invalid Expense Limit", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (NegativeAmountException msg) {
                    JOptionPane.showMessageDialog(frame, "Cannot enter negative expense amount", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //EFFECTS: action listener to handle delete entries
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idString = JOptionPane.showInputDialog("Please input a value");
                try {
                    int id = Integer.parseInt(idString);
                    if (expenseTracker.findExpenseEntry(id) != null) {
                        expenseTracker.deleteExpenseEntry(id);
                        update();
                        JOptionPane.showMessageDialog(frame, "Entry Id : " + id + " has been deleted!",
                                "Sucess Message!",
                                JOptionPane.DEFAULT_OPTION);
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException msg) {
                    JOptionPane.showMessageDialog(frame, "Invalid ID! No entries deleted!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //EFFECTS: action listener to save expense tracker to JSON file
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    jsonWriter.open();
                    jsonWriter.write(expenseTracker);
                    jsonWriter.close();
                    JOptionPane.showMessageDialog(frame, "Expense Tracker Saved Sucessfully!", "Sucess Message!",
                            JOptionPane.DEFAULT_OPTION);
                } catch (FileNotFoundException msg) {
                    JOptionPane.showMessageDialog(frame, "Unable to load to file: " + JSON_STORE, "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //EFFECTS: action listener to load expense tracker from JSON file
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    expenseTracker = jsonReader.read();
                    update();
                } catch (IOException msg) {
                    JOptionPane.showMessageDialog(frame, "Unable to read from file: " + JSON_STORE, "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // EFFECTS: adding each of those details to an expense entry
        addEntryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameTextField.getText();
                    String category = categoryDropdown.getSelectedItem().toString();
                    double expenseAmount = getAmountInDoubleFromTextField();
                    String note = noteTextField.getText();
                    LocalDate date = getDateFromTextField();
                    ExpenseEntry newEntry = new ExpenseEntry(name, category, expenseAmount, note, date);
                    expenseTracker.addExpenseEntry(newEntry);
                    update();
                    clearTextField();
                } catch (DateTimeParseException msg) {
                    JOptionPane.showMessageDialog(frame, "Invalid date format. Please enter date in yyyy-MM-dd format.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException msg) {
                    JOptionPane.showMessageDialog(frame, "Invalid Expense Amount field", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (NegativeAmountException msg) {
                    JOptionPane.showMessageDialog(frame, "Cannot enter negative expense amount", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        // =======================================
        // Panels

        // Create a panel and add the label and button to itz
        addEntryField.setLayout(new GridLayout(5, 2, 10, 10));
        createEntryField();
       
        buttonJPanel.add(addEntryButton);
        buttonJPanel.add(loadButton);
        buttonJPanel.add(saveButton);
        buttonJPanel.add(deleteButton);
        buttonJPanel.add(editExpenseLimitButton);

        statusMessageJPanel.setLayout(new BorderLayout());
        statusMessageJPanel.add(expenseLimitJLabel, BorderLayout.NORTH);
        statusMessageJPanel.add(totalExpenseJLabel, BorderLayout.CENTER);
        statusMessageJPanel.add(expenseStatusJLabel, BorderLayout.SOUTH);

        tableJPanel.setLayout(new BorderLayout());
        tableJPanel.add(scrollPaneExpenseTable, BorderLayout.NORTH);
        tableJPanel.add(statusMessageJPanel, BorderLayout.SOUTH);
        


        // Add the panel to the frame
        frame.setLayout(new BorderLayout());
        frame.add(buttonJPanel, BorderLayout.NORTH);
        frame.add(tableJPanel, BorderLayout.CENTER);
        frame.add(addEntryField, BorderLayout.SOUTH);
        
        // Make the frame visible
        frame.setVisible(true);

    }

    // =====================================================
    // helper methods

    //EFFECTS: update the dynamic data that displays on the GUI
    public void update() {
        updateExpenseLimit();
        updateTotalExpense();
        updateExpenseTable();
        updateExpenseStatus();
    }

    //EFFECTS: update the expense status message 
    public void updateExpenseStatus() {
        double totalExpense = expenseTracker.getTotalExpenseAmount();
        double limit = expenseTracker.getUser().getExpenseLimit();
        if (expenseTracker.getUser().getOverExpenseLimit(totalExpense)) {
            expenseStatusJLabel
                    .setText("Expense Status: You have reached / reached over your expense limit for the month!");
        } else {
            expenseStatusJLabel
                    .setText("Expense Status: You have remaining $" + (limit - totalExpense) + " to expense!");
        }
    }
    //EFFECTS: update the expense limit message 
    public void updateExpenseLimit() {
        expenseLimitJLabel.setText("Expense Limit: $" + expenseTracker.getUser().getExpenseLimit());
    }

    //EFFECTS: update the expense limit message 
    public void updateTotalExpense() {
        totalExpenseJLabel.setText("Total Expense Amount: $" + expenseTracker.getTotalExpenseAmount());
    }

    //EFFECTS: update the expense table to display 
    public void updateExpenseTable() {
        clearTable();
        for (ExpenseEntry entry : expenseTracker.getListOfExpenseEntries()) {
            tableModel.addRow(new Object[] { entry.getName(), entry.getCategory(),
                    entry.getExpenseAmount(), entry.getNote(), entry.getDate(), entry.getId() });
        }
    }

    //EFFECTS: clear the expense table
    public void clearTable() {
        tableModel.setRowCount(0);
    }

    // EFFECTS: initialize the expense tracker app
    public void initializeApp() throws FileNotFoundException {
        this.jsonWriter = new JsonWriter(JSON_STORE);
        this.jsonReader = new JsonViewer(JSON_STORE);
        this.expenseTracker = new ExpenseTracker();
    }

    // EFFECTS: convert textfield of String date to LocalDate, throws DateTimeParseException if date is invalid
    public LocalDate getDateFromTextField() throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = null;
        String dateStr = dateTextField.getText();
        date = LocalDate.parse(dateStr, formatter);
        return date;
    }

    // EFFECTS: convert textfield of String expense amount to double expense amount
    //          throws NegativeAmountException if expense amount is < 0
    //          throws NumberFormatException if textfield value converted to double is still not a valid number
    public double getAmountInDoubleFromTextField() throws NumberFormatException, NegativeAmountException {
        try {
            double expenseAmount;
            expenseAmount = Double.parseDouble(amountTextField.getText());
            if (expenseAmount < 0) {
                throw new NegativeAmountException();
            }
            return expenseAmount;
        } catch (NumberFormatException msg) {
            throw new NumberFormatException();
        }
    }

    //EFFECTS: clear all text field after adding
    public void clearTextField() {
        nameTextField.setText("");
        dateTextField.setText("");
        amountTextField.setText("");
        noteTextField.setText("");
    }

    //EFFECTS: create entry fields
    public void createEntryField() {
        JLabel nameLabel = new JLabel("Name : ");
        addEntryField.add(nameLabel);
        addEntryField.add(nameTextField);

        JLabel categoryLabel = new JLabel("Category : ");
        categoryDropdown = new JComboBox<Categories>(Categories.values());
        addEntryField.add(categoryLabel);
        addEntryField.add(categoryDropdown);

        JLabel amountLabel = new JLabel("Expense Amount : ");
        addEntryField.add(amountLabel);
        addEntryField.add(amountTextField);

        JLabel noteLabel = new JLabel("Note : ");
        addEntryField.add(noteLabel);
        addEntryField.add(noteTextField);

        JLabel dateLabel = new JLabel("Date : ");
        addEntryField.add(dateLabel);
        addEntryField.add(dateTextField);
    }

}
