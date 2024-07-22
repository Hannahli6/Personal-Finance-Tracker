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
        assertEquals(0, user1.getExpenseLimit());

        //False expense limit
        assertFalse(user1.getOverExpenseLimit(0));
    }

    @Test
    void testGetExpenseLimitPerMonth() {
        user1.setExpenseLimit(350.50);
        assertEquals(350.50, user1.getExpenseLimit());
    }

    
    @Test
    void testGetOverExpenseLimit() {
        user1.setExpenseLimit(50);
        assertTrue(user1.getOverExpenseLimit(500));
    }
    
    @Test
    void testGetOverExpenseLimitCase2() {
        user1.setExpenseLimit(110);
        assertFalse(user1.getOverExpenseLimit(10));
        assertFalse(user1.getOverExpenseLimit(10));
        
        assertTrue(user1.getOverExpenseLimit(200));
    }

    @Test
    void testSetExpenseLimit() {
        user1.setExpenseLimit(450.50);
        assertEquals(450.50, user1.getExpenseLimit());
    }



}
