package ui;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import exception.*;
import model.*;

public class ExpenseTrackerAppGUI {

    JLabel nameLabel;
    JLabel amountLabel;
    JFrame frame;
    JPanel addEntryField = new JPanel();

    JTextField nameTextField = new JTextField("");
    JTextField categoryTextField = new JTextField("");
    JTextField amountTextField = new JTextField("");
    JTextField noteTextField = new JTextField("");
    JTextField dateTextField = new JTextField("");

    ExpenseTracker expenseTracker = new ExpenseTracker();

    public ExpenseTrackerAppGUI() {
        // Create the frame (window)
        frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 750);

        // Create a label
        nameLabel = new JLabel("name");
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);

        amountLabel = new JLabel("Amount");
        amountLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Create a button
        JButton addEntryButton = new JButton("button");

        
        // =====================================
        //EVENT Listerners

        // EFFECTS: on click button to add each of those entry details to an expense entry
        //          if date is invalid, throw date exception
        //          if expense Amount is invalid, throw expense amount invalid exception
        addEntryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameTextField.getText();
                    String category = categoryTextField.getText();
                    double expenseAmount = getAmountInDoubleFromTextField();
                    String note = noteTextField.getText();
                    LocalDate date = getDateFromTextField();
                    ExpenseEntry newEntry = new ExpenseEntry(name, category, expenseAmount, note, date);
                    expenseTracker.addExpenseEntry(newEntry);
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


        //=======================================
        //Panels

        createEntryField();

        // Create a panel and add the label and button to it
        addEntryField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addEntryField.setLayout(new GridLayout(3, 2));
        addEntryField.setSize(100,1000);    
        addEntryField.add(addEntryButton);

        // Add the panel to the frame
        frame.add(addEntryField);
        // Make the frame visible
        frame.setVisible(true);

    }

    public LocalDate getDateFromTextField() throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = null;
        String dateStr = dateTextField.getText();
        date = LocalDate.parse(dateStr, formatter);
        return date;
    }

    public double getAmountInDoubleFromTextField() throws NumberFormatException,  NegativeAmountException{
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

    public void createEntryField() {
        nameTextField.setPreferredSize(new Dimension(200, 30));
        JLabel nameLabel = new JLabel("name : ");
        JPanel nameWrapper = new JPanel();
        nameWrapper.add(nameLabel);
        nameWrapper.add(nameTextField);
        addEntryField.add(nameWrapper);

        categoryTextField.setPreferredSize(new Dimension(200, 30));
        JLabel categoryLabel = new JLabel("Category : ");
        JPanel categoryWrapper = new JPanel();
        nameWrapper.add(categoryLabel);
        nameWrapper.add(categoryTextField);
        addEntryField.add(categoryWrapper);

        amountTextField.setPreferredSize(new Dimension(200, 30));
        JLabel amountLabel = new JLabel("Expense Amount : ");
        JPanel amountWrapper = new JPanel();
        nameWrapper.add(amountLabel);
        nameWrapper.add(amountTextField);
        addEntryField.add(amountWrapper);

        noteTextField.setPreferredSize(new Dimension(200, 30));
        JLabel noteLabel = new JLabel("Note : ");
        JPanel noteWrapper = new JPanel();
        nameWrapper.add(noteLabel);
        nameWrapper.add(noteTextField);
        addEntryField.add(noteWrapper);

        dateTextField.setPreferredSize(new Dimension(200, 30));
        JLabel dateLabel = new JLabel("Date : ");
        JPanel dateWrapper = new JPanel();
        nameWrapper.add(dateLabel);
        nameWrapper.add(dateTextField);
        addEntryField.add(dateWrapper);
    }

}
