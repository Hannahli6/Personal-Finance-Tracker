package persistence;


import org.junit.jupiter.api.Test;

import model.ExpenseEntry;
import model.ExpenseTracker;

import java.io.IOException;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest{
    
    @Test
    void testJsonReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ExpenseTracker expenseTracker = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testJsonReaderEmptyExpenseTracker() {
        JsonReader reader = new JsonReader("./data/testJsonReaderEmptyList.json");
        try {
            ExpenseTracker expenseTracker = reader.read();
            assertEquals(0, expenseTracker.getTotalExpenseAmount());
            assertEquals(0, expenseTracker.getUser().getExpenseLimit());
            assertEquals(false, expenseTracker.getUser().getOverExpenseLimit(0));
            assertEquals(0, expenseTracker.getListOfExpenseEntries().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testJsonReaderWithEntries() {
        JsonReader reader = new JsonReader("./data/testJsonReaderWithEntries.json");
        try {
            ExpenseTracker expenseTracker = reader.read();
            ArrayList<ExpenseEntry> listOfExpenseEntries = expenseTracker.getListOfExpenseEntries();
            ExpenseEntry entry0 = listOfExpenseEntries.get(0);
            ExpenseEntry entry1 = listOfExpenseEntries.get(1);
            double getTotalExpenseAmount = expenseTracker.getTotalExpenseAmount();

            assertEquals(50, expenseTracker.getUser().getExpenseLimit());
            assertEquals(true, expenseTracker.getUser().getOverExpenseLimit(getTotalExpenseAmount));
            assertEquals(2, listOfExpenseEntries.size());

            checkExpenseEntry(entry0, entry0.getName(), entry0.getCategory(),entry0.getExpenseAmount(), entry0.getNote(), entry0.getDate(), entry0.getId());
            checkExpenseEntry(entry1, entry1.getName(), entry1.getCategory(),entry1.getExpenseAmount(), entry1.getNote(), entry1.getDate(), entry1.getId());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
        
    }

}
