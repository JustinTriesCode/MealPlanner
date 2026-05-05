package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MealCollectionTest {
    private Meal testMeal1;
    private Meal testMeal2;
    private Meal testMeal3;
    private MealCollection emptyMealListTest;
    private MealCollection fullMealListTest;

    @BeforeEach
    void runBefore() {
        testMeal1 = new Meal("meal1", 3.5, 8);
        testMeal2 = new Meal("meal2", 3, 1);
        testMeal3 = new Meal("meal3", 1, 3);
        emptyMealListTest = new MealCollection("empty");
        fullMealListTest = new MealCollection("full");
        fullMealListTest.addMeal(testMeal1);
        fullMealListTest.addMeal(testMeal2);
        fullMealListTest.addMeal(testMeal3);
        testMeal3.increaseEffort();
        testMeal3.increaseEffort();
        testMeal2.rateMeal(4);
    }

    @Test
    void testConstructor() {
        assertTrue(emptyMealListTest.getMeals().isEmpty());
        assertEquals("empty", emptyMealListTest.getCollectionName());
    }

    @Test
    void testUpdateCollectoinName() {
        emptyMealListTest.updateCollectionName("new name");
        assertEquals("new name", emptyMealListTest.getCollectionName());
    }

    @Test
    void testHasMealNameFound() {
        emptyMealListTest.addMeal(testMeal1);
        assertTrue(emptyMealListTest.hasMealName("meal1"));
    }

    @Test
    void testHasMealNameNotFound() {
        assertFalse(emptyMealListTest.hasMealName("meal1"));
        emptyMealListTest.addMeal(testMeal1);
        assertFalse(emptyMealListTest.hasMealName("meal2"));
    }

    @Test
    void testHasMealFound() {
        assertTrue(fullMealListTest.hasMeal(testMeal1));
    }

    @Test
    void testHasMealNotFound() {
        assertFalse(emptyMealListTest.hasMeal(testMeal1));
        emptyMealListTest.addMeal(testMeal1);
        assertFalse(emptyMealListTest.hasMeal(testMeal2));
    }

    @Test
    void testGetMealByName() {
        emptyMealListTest.addMeal(testMeal1);
        assertEquals(testMeal1, emptyMealListTest.getMeal("meal1"));
        assertEquals(null, emptyMealListTest.getMeal("meal3"));
    }

    @Test
    void testGetMealByIndex() {
        assertEquals(testMeal2, fullMealListTest.getMeal(1));
    }

    @Test
    void testAddOneMeal() {
        emptyMealListTest.addMeal(testMeal1);
        assertEquals(1, emptyMealListTest.getMeals().size());
    }

    @Test
    void testAddMultipleMeals() {
        emptyMealListTest.addMeal(testMeal1);
        assertEquals(testMeal1, emptyMealListTest.getMeal(0));
        emptyMealListTest.addMeal(testMeal2);
        assertEquals(testMeal2, emptyMealListTest.getMeal(1));
        emptyMealListTest.addMeal(testMeal3);
        assertTrue(emptyMealListTest.hasMeal(testMeal3));
        assertEquals(3, emptyMealListTest.getMeals().size());
    }

    @Test
    void testRemoveMissingMeal() {
        assertEquals(3, fullMealListTest.getMeals().size());
        fullMealListTest.removeMeal("WrongName");
        assertEquals(3, fullMealListTest.getMeals().size());
    }

    @Test
    void testRemoveOneMeal() {
        assertEquals(3, fullMealListTest.getMeals().size());
        fullMealListTest.removeMeal("meal2");
        assertFalse(fullMealListTest.hasMeal(testMeal2));
        assertEquals(2, fullMealListTest.getMeals().size());
    }

    @Test
    void testRemoveMultipleMeals() {
        assertTrue(fullMealListTest.hasMeal(testMeal3));
        assertEquals(3, fullMealListTest.getMeals().size());
        fullMealListTest.removeMeal("meal2");
        assertFalse(fullMealListTest.hasMeal(testMeal2));
        assertEquals(2, fullMealListTest.getMeals().size());
        fullMealListTest.removeMeal("MEAL1");
        assertFalse(fullMealListTest.hasMeal(testMeal1));
        assertEquals(1, fullMealListTest.getMeals().size());
    }

    @Test
    void testFilteringMealsTime() {
        List<Meal> filteredList = fullMealListTest.filterMeals(3.1, -1, -1, -1);
        assertEquals(2, filteredList.size());
        assertFalse(filteredList.contains(testMeal1));
        assertTrue(filteredList.contains(testMeal2));
        assertTrue(filteredList.contains(testMeal3));
    }

    @Test
    void testFilteringMealsTimeNoResult() {
        List<Meal> filteredList = fullMealListTest.filterMeals(0.3, -1, -1, -1);
        assertTrue(filteredList.isEmpty());
        assertFalse(filteredList.contains(testMeal1));
        assertFalse(filteredList.contains(testMeal2));
        assertFalse(filteredList.contains(testMeal3));
    }

    @Test
    void testFilteringMealsEffort() {
        List<Meal> filteredList = fullMealListTest.filterMeals(-1, 1, -1, -1);
        assertEquals(2, filteredList.size());
        assertFalse(filteredList.contains(testMeal3));
        assertTrue(filteredList.contains(testMeal2));
        assertTrue(filteredList.contains(testMeal1));
    }

    @Test
    void testFilteringMealsEffortNoResult() {
        testMeal1.increaseEffort();
        testMeal2.increaseEffort();
        List<Meal> filteredList = fullMealListTest.filterMeals(-1, 0, -1, -1);
        assertTrue(filteredList.isEmpty());
        assertFalse(filteredList.contains(testMeal1));
        assertFalse(filteredList.contains(testMeal2));
        assertFalse(filteredList.contains(testMeal3));
    }

    @Test
    void testFilteringMealsProtein() {
        List<Meal> filteredList = fullMealListTest.filterMeals(-1, -1, 6, -1);
        assertEquals(1, filteredList.size());
        assertFalse(filteredList.contains(testMeal3));
        assertFalse(filteredList.contains(testMeal2));
        assertTrue(filteredList.contains(testMeal1));
    }

    @Test
    void testFilteringMealsProteinNoResult() {
        List<Meal> filteredList = fullMealListTest.filterMeals(-1, -1, 9, -1);
        assertTrue(filteredList.isEmpty());
        assertFalse(filteredList.contains(testMeal1));
        assertFalse(filteredList.contains(testMeal2));
        assertFalse(filteredList.contains(testMeal3));
    }

    @Test
    void testFilteringMealsRating() {
        List<Meal> filteredList = fullMealListTest.filterMeals(-1, -1, 0, 3);
        assertEquals(1, filteredList.size());
        assertFalse(filteredList.contains(testMeal1));
        assertTrue(filteredList.contains(testMeal2));
        assertFalse(filteredList.contains(testMeal3));
    }

    @Test
    void testFilteringMealsRatingNoResult() {
        List<Meal> filteredList = fullMealListTest.filterMeals(-1, -1, -1, 5);
        assertTrue(filteredList.isEmpty());
        assertFalse(filteredList.contains(testMeal1));
        assertFalse(filteredList.contains(testMeal2));
        assertFalse(filteredList.contains(testMeal3));
    }

    @Test
    void testFilteringMealsManyHit() {
        List<Meal> filteredList = fullMealListTest.filterMeals(4, 5, 0, 0);
        assertEquals(3, filteredList.size());
        assertTrue(filteredList.contains(testMeal1));
        assertTrue(filteredList.contains(testMeal2));
        assertTrue(filteredList.contains(testMeal3));
    }

    @Test
    void testGetGroceryList() {
        assertTrue(fullMealListTest.getGroceryList().isEmpty());
        testMeal1.addIngredient("apple");
        assertTrue(fullMealListTest.getGroceryList().contains("apple"));
        testMeal2.addManyIngredients(List.of("apple", "banana"));
        testMeal3.addIngredient("pear");
        assertEquals(3, fullMealListTest.getGroceryList().size());
        assertTrue(fullMealListTest.getGroceryList().contains("apple"));
        assertTrue(fullMealListTest.getGroceryList().contains("banana"));
        assertTrue(fullMealListTest.getGroceryList().contains("pear"));
    }

    @Test
    void testGetGroceryListNotNeeded() {
        testMeal1.addIngredient("apple");
        testMeal3.addIngredient("pear");
        testMeal1.purchasedIngredients();
        assertFalse(fullMealListTest.getGroceryList().contains("apple"));
        assertTrue(fullMealListTest.getGroceryList().contains("pear"));
    }

}
