package model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestUser {
    private User user1;
    
    @BeforeEach
    void runBefore() {
        user1 = new User(200);
    }
    
    @Test
    void testUser() {
        //initalize expense limit 
        assertEquals(200, user1.getExpenseLimitPerMonth());
        //initialize total Expense Amount to 0 dollars
        assertEquals(0, user1.getTotalExpenseAmount());
        //False expense limit
        assertFalse(user1.getOverExpenseLimit());
    }

    @Test
    void testGetExpenseLimitPerMonth() {
        user1.setExpenseLimitPerMonth(350.50);
        assertEquals(350.50, user1.getExpenseLimitPerMonth());
    }

    @Test
    void testGetTotalExpenseAmount() {
        user1.setTotalExpenseAmount(500.00);
        assertEquals(500.00, user1.getTotalExpenseAmount());
    }

    @Test
    void testGetOverExpenseLimit() {
        user1.setExpenseLimitPerMonth(350.50);
        user1.setTotalExpenseAmount(500.00);
        assertTrue(user1.getOverExpenseLimit());
    }

    @Test
    void testSetExpenseLimitPerMonth() {
        user1.setExpenseLimitPerMonth(450.50);
        assertEquals(450.50, user1.getExpenseLimitPerMonth());
    }

    @Test
    void testSetTotalExpeneAmount() {
        user1.setTotalExpenseAmount(750.00);
        assertEquals(750.00, user1.getTotalExpenseAmount());
    }
}
