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
        user1 = new User();
    }
    
    @Test
    void testUser() {
        //initalize expense limit 
        assertEquals(0, user1.getExpenseLimitPerMonth());

        //False expense limit
        assertFalse(user1.getOverExpenseLimit(0));
    }

    @Test
    void testGetExpenseLimitPerMonth() {
        user1.setExpenseLimitPerMonth(350.50);
        assertEquals(350.50, user1.getExpenseLimitPerMonth());
    }

    
    @Test
    void testGetOverExpenseLimit() {
        user1.setExpenseLimitPerMonth(50);
        assertTrue(user1.getOverExpenseLimit(500));
    }
    
    @Test
    void testGetOverExpenseLimitCase2() {
        user1.setExpenseLimitPerMonth(110);
        assertFalse(user1.getOverExpenseLimit(10));
        assertFalse(user1.getOverExpenseLimit(10));
        
        assertTrue(user1.getOverExpenseLimit(200));
    }

    @Test
    void testSetExpenseLimitPerMonth() {
        user1.setExpenseLimitPerMonth(450.50);
        assertEquals(450.50, user1.getExpenseLimitPerMonth());
    }


}
