package persistence;

// Modeled on the JsonTest in the JsonSerializationDemo

import model.Meal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

public class JsonTest {

    // EFFECTS: helper for the json reader and writer tests
    protected void checkMeal(String name, double cookTime, int protein,
            int effortLvl, int rating, boolean needIngredients, List<String> ingredients,
            Meal m) {
        assertEquals(name, m.getMealName());
        assertEquals(cookTime, m.getCookTime());
        assertEquals(protein, m.getProtein());
        assertEquals(effortLvl, m.getEffortLevel());
        assertEquals(rating, m.getRating());
        assertEquals(needIngredients, m.needsIngredients());
        assertEquals(ingredients, m.getIngredients());
    }

}
