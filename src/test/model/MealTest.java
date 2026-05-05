package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MealTest {
    private Meal testMeal;

    @BeforeEach
    void runBefore() {
        testMeal = new Meal("meal1", 3.5, 8);

    }

    @Test
    void constructorTest() {
        assertEquals("meal1", testMeal.getMealName());
        assertEquals(3.5, testMeal.getCookTime(), 0.01);
        assertEquals(0, testMeal.getEffortLevel());
        assertEquals(8, testMeal.getProtein());
        assertEquals(0, testMeal.getIngredients().size());
        assertEquals(0, testMeal.getRating());
        assertTrue(testMeal.needsIngredients());
    }

    @Test
    void renameMealTest() {
        testMeal.renameMeal("newName");
        assertEquals("newName", testMeal.getMealName());
    }

    @Test
    void updateTimeTest() {
        testMeal.updateCookTime(4.1);
        assertEquals(4.1, testMeal.getCookTime(), 0.01);
    }

    @Test
    void updateTimeTestBound() {
        testMeal.updateCookTime(0.01);
        assertEquals(0.01, testMeal.getCookTime(), 0.01);
    }

    @Test
    void updateProteinTest() {
        testMeal.updateProtein(3);
        assertEquals(3, testMeal.getProtein());
    }

    @Test
    void testSetEffort() {
        testMeal.setEffort(5);
        assertEquals(5, testMeal.getEffortLevel());
    }

    @Test
    void increaseEffortTest() {
        testMeal.increaseEffort();
        assertEquals(1, testMeal.getEffortLevel());
        testMeal.increaseEffort();
        assertEquals(2, testMeal.getEffortLevel());
    }

    @Test
    void increaseEffortTestBound() {
        for (int i = 0; i < 9; i++) {
            testMeal.increaseEffort();
        }
        testMeal.increaseEffort();
        assertEquals(10, testMeal.getEffortLevel());
        testMeal.increaseEffort();
        assertEquals(10, testMeal.getEffortLevel());
    }

    @Test
    void decreaseEffortTestBound() {
        testMeal.increaseEffort();
        assertEquals(1, testMeal.getEffortLevel());
        testMeal.decreaseEffort();
        assertEquals(0, testMeal.getEffortLevel());
        testMeal.decreaseEffort();
        assertEquals(0, testMeal.getEffortLevel());
    }

    @Test
    void decreaseEffortTwiceTest() {
        testMeal.setEffort(2);
        assertEquals(2, testMeal.getEffortLevel());
        testMeal.decreaseEffort();
        testMeal.decreaseEffort();
        assertEquals(0, testMeal.getEffortLevel());
    }

    @Test
    void rateMealTest() {
        testMeal.rateMeal(3);
        assertEquals(3, testMeal.getRating());
        testMeal.rateMeal(0);
        assertEquals(0, testMeal.getRating());
        testMeal.rateMeal(5);
        assertEquals(5, testMeal.getRating());
    }

    @Test
    void purchasedIngredientsTest() {
        testMeal.purchasedIngredients();
        assertFalse(testMeal.needsIngredients());
        testMeal.repurchaseIngredients();
        assertTrue(testMeal.needsIngredients());
    }

    @Test
    void testSetNeedIngredients() {
        testMeal.setNeedIngredients(false);
        assertFalse(testMeal.needsIngredients());
    }

    @Test
    void addIngredientTest() {
        testMeal.addIngredient("apple");
        assertEquals("apple", testMeal.getIngredients().get(0));
        testMeal.addIngredient("apple");
        assertEquals(1, testMeal.getIngredients().size());
    }

    @Test
    void addManyIngredientsTest() {
        List<String> testIngredientList = List.of("apple", "banana", "");
        testMeal.addManyIngredients(testIngredientList);
        assertEquals(2, testMeal.getIngredients().size());
        assertTrue(testMeal.getIngredients().contains("apple"));
        assertTrue(testMeal.getIngredients().contains("banana"));
        testMeal.addManyIngredients(List.of("pear", "banana"));
        assertTrue(testMeal.getIngredients().contains("banana"));
        assertTrue(testMeal.getIngredients().contains("pear"));
        assertEquals(3, testMeal.getIngredients().size());

    }

    @Test
    void removeIngredientTest() {
        testMeal.addIngredient("pineapple");
        testMeal.addIngredient("pear");
        assertTrue(testMeal.getIngredients().contains("pineapple"));
        testMeal.removeIngredient("pineapple");
        assertFalse(testMeal.getIngredients().contains("pineapple"));
        testMeal.removeIngredient("pea");
        assertTrue(testMeal.getIngredients().contains("pear"));
    }

    @Test
    void testReplaceIngredients() {
        testMeal.addIngredient("mango");
        testMeal.replaceIngredients(List.of("peach"));
        assertTrue(testMeal.getIngredients().contains("peach"));
        assertFalse(testMeal.getIngredients().contains("mango"));
    }

    @Test
    void testUpdateMeal() {
        testMeal.updateMeal("new1", 9, 9, 4, List.of("one", "two"), 9, false);
        assertEquals("new1", testMeal.getMealName());
        assertEquals(9, testMeal.getCookTime());
        assertEquals(9, testMeal.getProtein());
        assertEquals(9, testMeal.getEffortLevel());
        assertEquals(4, testMeal.getRating());
        assertFalse(testMeal.needsIngredients());
        assertEquals(List.of("one", "two"), testMeal.getIngredients());
    }
}
