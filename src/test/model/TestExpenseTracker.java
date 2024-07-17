package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TestExpenseTracker {

    private ExpenseEntry entry0;
    private ExpenseEntry entry1;
    private ExpenseTracker myExpenseTracker;
    private

    @BeforeEach
    void runBefore() {
        myExpenseTracker = new ExpenseTracker();
        entry0 = new ExpenseEntry(
                "ramen",
                "Food",
                18.50,
                "my friend's birthday dinner",
                LocalDate.of(2024, 7, 15));
        entry1 = new ExpenseEntry(
                "compass card",
                "Transportation",
                100,
                "monthly compass card",
                LocalDate.of(2024, 7, 15));
    }

    @Test
    void testAddExpenseEntry() {
        //empty list
        assertEquals(0, myExpenseTracker.getListOfExpenseEntries().size());

        //add one
        myExpenseTracker.addExpenseEntry(entry0);
        assertEquals(1, myExpenseTracker.getListOfExpenseEntries().size());
        assertEquals("ramen", myExpenseTracker.getListOfExpenseEntries().get(0).getName());
        
        
        //add two
        myExpenseTracker.addExpenseEntry(entry1);
        entry1.setId(1);
        assertEquals(2, myExpenseTracker.getListOfExpenseEntries().size());
        assertEquals("compass card", myExpenseTracker.getListOfExpenseEntries().get(1).getName());
    }

    @Test
    void testEditExpenseEntry() {
        myExpenseTracker.addExpenseEntry(entry0);
        entry0.setId(0);
        myExpenseTracker.addExpenseEntry(entry1);
        entry1.setId(1);

        myExpenseTracker.editExpenseEntry("sushi", "Food", 30.99, "expensive sushi",0);
        assertEquals("sushi", myExpenseTracker.getListOfExpenseEntries().get(0).getName());
        assertEquals("Food", myExpenseTracker.getListOfExpenseEntries().get(0).getCategory());
        assertEquals(30.99, myExpenseTracker.getListOfExpenseEntries().get(0).getExpenseAmount());
        assertEquals("expensive sushi", myExpenseTracker.getListOfExpenseEntries().get(0).getNote());
        assertEquals(0, myExpenseTracker.getListOfExpenseEntries().get(0).getId());

        myExpenseTracker.editExpenseEntry("compass card", "Transportation", 50, "discount for compass card",1);
        assertEquals("compass card", myExpenseTracker.getListOfExpenseEntries().get(1).getName());
        assertEquals("Transportation", myExpenseTracker.getListOfExpenseEntries().get(1).getCategory());
        assertEquals(50, myExpenseTracker.getListOfExpenseEntries().get(1).getExpenseAmount());
        assertEquals("discount for compass card", myExpenseTracker.getListOfExpenseEntries().get(1).getNote());
        assertEquals(1, myExpenseTracker.getListOfExpenseEntries().get(1).getId());
    }

    @Test
    void testDeleteExpenseEntry() {
        myExpenseTracker.addExpenseEntry(entry0);
        entry0.setId(0);
        assertEquals(1, myExpenseTracker.getListOfExpenseEntries().size());
        myExpenseTracker.deleteExpenseEntry(0);
        assertEquals(0, myExpenseTracker.getListOfExpenseEntries().size());
        
    }

    @Test
    void testFindExpenseEntry() {
        myExpenseTracker.addExpenseEntry(entry0);
        entry1.setId(0);
        myExpenseTracker.addExpenseEntry(entry1);
        entry1.setId(1);
        assertEquals(myExpenseTracker.getListOfExpenseEntries().get(1), myExpenseTracker.findExpenseEntry(1));
        assertEquals(null, myExpenseTracker.findExpenseEntry(99));
    }

    @Test
    void testGetListOfExpenseEntries() {
        myExpenseTracker.addExpenseEntry(entry0);
        entry0.setId(0);
        assertEquals("ramen", myExpenseTracker.getListOfExpenseEntries().get(0).getName());
        myExpenseTracker.addExpenseEntry(entry1);
        entry1.setId(1);
        assertEquals("ramen", myExpenseTracker.getListOfExpenseEntries().get(0).getName());
        assertEquals("compass card", myExpenseTracker.getListOfExpenseEntries().get(1).getName());
    }
}
