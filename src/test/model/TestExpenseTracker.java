package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestExpenseTracker {

    private ExpenseEntry entry0;
    private ExpenseEntry entry1;
    private ExpenseEntry entry2;
    private ExpenseTracker myExpenseTracker;

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
        entry2 = new ExpenseEntry(
                "School",
                "Education",
                500,
                "book fees",
                LocalDate.of(2024, 7, 16));
    }

    @Test
    void testAddExpenseEntry() {
        // empty list
        assertEquals(0, myExpenseTracker.getListOfExpenseEntries().size());

        // add one
        myExpenseTracker.addExpenseEntry(entry0);
        assertEquals(1, myExpenseTracker.getListOfExpenseEntries().size());
        assertEquals("ramen", myExpenseTracker.getListOfExpenseEntries().get(0).getName());

        // add two
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

        myExpenseTracker.editExpenseEntry("sushi", "Food", 300, "expensive sushi", LocalDate.of(2024, 7, 15), 0);
        assertEquals("sushi", myExpenseTracker.getListOfExpenseEntries().get(0).getName());
        assertEquals("Food", myExpenseTracker.getListOfExpenseEntries().get(0).getCategory());
        assertEquals(300, myExpenseTracker.getListOfExpenseEntries().get(0).getExpenseAmount());
        assertEquals("expensive sushi", myExpenseTracker.getListOfExpenseEntries().get(0).getNote());
        assertEquals(LocalDate.of(2024, 7, 15), myExpenseTracker.getListOfExpenseEntries().get(0).getDate());
        assertEquals(0, myExpenseTracker.getListOfExpenseEntries().get(0).getId());

        myExpenseTracker.editExpenseEntry("compass card", "Transportation", 500, "discount for compass card",
                LocalDate.of(2024, 7, 10), 1);
        assertEquals("compass card", myExpenseTracker.getListOfExpenseEntries().get(1).getName());
        assertEquals("Transportation", myExpenseTracker.getListOfExpenseEntries().get(1).getCategory());
        assertEquals(500, myExpenseTracker.getListOfExpenseEntries().get(1).getExpenseAmount());
        assertEquals("discount for compass card", myExpenseTracker.getListOfExpenseEntries().get(1).getNote());
        assertEquals(LocalDate.of(2024, 7, 10), myExpenseTracker.getListOfExpenseEntries().get(1).getDate());
        assertEquals(1, myExpenseTracker.getListOfExpenseEntries().get(1).getId());

        assertEquals(800, myExpenseTracker.getTotalExpenseAmount());
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
    void testDeleteMultipleExpenseEntry() {
        myExpenseTracker.addExpenseEntry(entry0);
        myExpenseTracker.addExpenseEntry(entry1);
        myExpenseTracker.addExpenseEntry(entry2);

        entry0.setId(0);
        entry1.setId(1);
        entry2.setId(2);

        assertEquals(3, myExpenseTracker.getListOfExpenseEntries().size());
        myExpenseTracker.deleteExpenseEntry(1);
        assertEquals(2, myExpenseTracker.getListOfExpenseEntries().size());
        assertEquals(null, myExpenseTracker.findExpenseEntry(1));
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
        assertEquals(entry0, myExpenseTracker.getListOfExpenseEntries().get(0));
        myExpenseTracker.addExpenseEntry(entry1);
        entry1.setId(1);
        assertEquals(entry0, myExpenseTracker.getListOfExpenseEntries().get(0));
        assertEquals(entry1, myExpenseTracker.getListOfExpenseEntries().get(1));
    }

    @Test
    void testGetTotalExpenseAmount() {
        // empty list
        assertEquals(0, myExpenseTracker.getTotalExpenseAmount());

        // one entry in list
        myExpenseTracker.addExpenseEntry(entry0);
        assertEquals(18.50, myExpenseTracker.getTotalExpenseAmount());

        // multiple entries in list
        myExpenseTracker.addExpenseEntry(entry1);
        assertEquals((100 + 18.50), myExpenseTracker.getTotalExpenseAmount());
    }

    @Test
    void testListOfExpenseEntriesToJson() {

        myExpenseTracker.addExpenseEntry(entry0);
        myExpenseTracker.addExpenseEntry(entry1);

        JSONArray jsonArray = myExpenseTracker.listOfExpenseEntriesToJson();
        JSONObject entry0JsonObject = jsonArray.getJSONObject(0);
        JSONObject entry1JsonObject = jsonArray.getJSONObject(1);

        assertEquals("ramen", entry0JsonObject.getString("name"));
        assertEquals("Food", entry0JsonObject.getString("category"));
        assertEquals(18.50, entry0JsonObject.getDouble("expenseAmount"));
        assertEquals("my friend's birthday dinner", entry0JsonObject.getString("note"));
        assertEquals(LocalDate.of(2024, 7, 15), entry0JsonObject.get("date"));
        assertEquals(0, entry0JsonObject.getInt("id"));

        assertEquals("compass card", entry1JsonObject.getString("name"));
        assertEquals("Transportation", entry1JsonObject.getString("category"));
        assertEquals(100, entry1JsonObject.getDouble("expenseAmount"));
        assertEquals("monthly compass card", entry1JsonObject.getString("note"));
        assertEquals(LocalDate.of(2024, 7, 15), entry1JsonObject.get("date"));
        assertEquals(1, entry1JsonObject.getInt("id"));

    }

}
