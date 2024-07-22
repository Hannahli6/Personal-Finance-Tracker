package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;

import model.ExpenseEntry;

public class JsonTest {
    // it's marked as protected so that its own class or its subclass is able to inherit this method
    protected void checkExpenseEntry(ExpenseEntry entry,String name, String category, double expenseAmount, String note, LocalDate date, int id) {
        assertEquals(name, entry.getName());
        assertEquals(category, entry.getCategory());
        assertEquals(expenseAmount, entry.getExpenseAmount());
        assertEquals(note, entry.getNote());
        assertEquals(date, entry.getDate());
        assertEquals(id, entry.getId());
    }
}
