package persistence;

import org.junit.jupiter.api.Test;

import model.ExpenseEntry;
import model.ExpenseTracker;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {

    @Test
    void testJsonWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testJsonWriterEmptyExpenseTracker() {
        try {
            ExpenseTracker expenseTracker = new ExpenseTracker();
            JsonWriter writer = new JsonWriter("./data/testJsonWriterEmptyList.json");
            writer.open();
            writer.write(expenseTracker);
            writer.close();

            JsonViewer reader = new JsonViewer("./data/testJsonWriterEmptyList.json");
            expenseTracker = reader.read();
            assertEquals(0, expenseTracker.getTotalExpenseAmount());
            assertEquals(0, expenseTracker.getUser().getExpenseLimit());
            assertEquals(false, expenseTracker.getUser().getOverExpenseLimit(0));
            assertEquals(0, expenseTracker.getListOfExpenseEntries().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testJsonWriterWithEntries() {
        try {
            ExpenseTracker expenseTracker = new ExpenseTracker();
            // sample entries from test json file
            ExpenseEntry entry0 = new ExpenseEntry(
                    "Sushi",
                    "Food",
                    25,
                    "dinner",
                    LocalDate.of(2024, 10, 10));
            ExpenseEntry entry1 = new ExpenseEntry(
                    "Bus",
                    "Transportation",
                    100,
                    "upass",
                    LocalDate.of(2024, 7, 10));
            JsonWriter writer = new JsonWriter("./data/testJsonWriterWithEntries.json");
            // add expense limit
            expenseTracker.getUser().setExpenseLimit(100);

            // add multiple expense entries
            expenseTracker.addExpenseEntry(entry0);
            entry0.setId(0);
            expenseTracker.addExpenseEntry(entry1);
            entry1.setId(0);

            // write into file
            writer.open();
            writer.write(expenseTracker);
            writer.close();

            // read & check
            JsonViewer reader = new JsonViewer("./data/testJsonWriterWithEntries.json");
            expenseTracker = reader.read();
            checkExpenseEntry(entry0, entry0.getName(), entry0.getCategory(),
                    entry0.getExpenseAmount(), entry0.getNote(), entry0.getDate(), entry0.getId());
            checkExpenseEntry(entry1, entry1.getName(), entry1.getCategory(),
                    entry1.getExpenseAmount(), entry1.getNote(), entry1.getDate(), entry1.getId());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}