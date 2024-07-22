package persistence;


import org.junit.jupiter.api.Test;

import model.ExpenseTracker;
import model.User;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            
        }
    }

    @Test
    void testWriterEmptyExpenseTracker() {
        try {
            ExpenseTracker expenseTracker = new ExpenseTracker();
            JsonWriter writer = new JsonWriter("./data/testJsonWriterEmptyList.json");
            writer.open();
            writer.write(expenseTracker);
            writer.close();

            JsonReader reader = new JsonReader("./data/testJsonWriterEmptyList.json");
            expenseTracker = reader.read();
            assertEquals(0, expenseTracker.getTotalExpenseAmount());
            assertEquals(0, expenseTracker.getListOfExpenseEntries().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}