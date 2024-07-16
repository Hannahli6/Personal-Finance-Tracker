package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestExpenseEntry {

    private ExpenseEntry myEntry;

    @BeforeEach
    void runBefore() {
        myEntry = new ExpenseEntry(
                "ramen",
                "Food",
                18.50,
                "my friend's birthday dinner",
                LocalDate.of(2024, 7, 15),
                0);
    }

    @Test
    void testExpenseEntry() {
        assertEquals("ramen", myEntry.getName());
        assertEquals("Food", myEntry.getCategory());
        assertEquals(18.50, myEntry.getExpenseAmount());
        assertEquals("my friend's birthday dinner", myEntry.getNote());
        assertEquals(LocalDate.of(2024, 7, 15), myEntry.getDate());
        assertEquals(0, myEntry.getId());
    }

    @Test
    void testExpenseEntrySetters() {
        LocalDate tempDate = LocalDate.of(2024, 7, 19);
        
        myEntry.setName("Japanese Food");
        assertEquals("Japanese Food", myEntry.getName());

        myEntry.setCategory("Food");
        assertEquals("Food", myEntry.getCategory());

        myEntry.setExpenseAmount(20);
        assertEquals(20.00, myEntry.getExpenseAmount());

        myEntry.setNote("her birthday party!");
        assertEquals("her birthday party!", myEntry.getNote());

        myEntry.setDate(tempDate);
        assertEquals(LocalDate.of(2024, 7, 19), myEntry.getDate());
        
        myEntry.setId(1);
        assertEquals(1,myEntry.getId());
    }

    @Test
    void testExpenseEntryGetters() {
        assertEquals("ramen", myEntry.getName());
        assertEquals("Food", myEntry.getCategory());
        assertEquals(18.50, myEntry.getExpenseAmount());
        assertEquals("my friend's birthday dinner", myEntry.getNote());
        assertEquals(LocalDate.of(2024, 7, 15), myEntry.getDate());

    }

}
