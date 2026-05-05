package persistence;

// Modeled on the JsonReaderTest in the JsonSerializationDemo

import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

import model.MealCollection;

public class JsonReaderTest extends JsonTest {

    @Test
    void testJsonReaderNoFile() {
        JsonReader reader = new JsonReader("./data/DoesNotExist.json");
        try {
            MealCollection mc = reader.read();
            fail("File doesn't exist, IOException expected");
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    void testReaderEmptyFile() {
        JsonReader reader = new JsonReader("./data/EmptyMealCollection.json");
        try {
            MealCollection mc = reader.read();
            assertTrue(mc.getMeals().isEmpty());
            assertEquals("Test Empty MC", mc.getCollectionName());
        } catch (IOException e) {
            fail("Triggered unexpected exception: " + e);
        }
    }

    @Test
    void testJsonReaderTestMealCollection() {
        JsonReader reader = new JsonReader("./data/TestMealCollection.json");
        try {
            MealCollection mc = reader.read();
            assertEquals("Test MC", mc.getCollectionName());
            assertEquals(3, mc.getMeals().size());
            List<String> emptyList = new ArrayList<>();
            List<String> someIngredients = List.of("potato", "beans");
            checkMeal("Potato", 4, 1, 0, 2, true, someIngredients, mc.getMeals().get(0));
            checkMeal("Tomato", 2, 4, 2, 2, true, emptyList, mc.getMeals().get(1));
        } catch (IOException e) {
            fail("Triggered unexpected exception: " + e);
        }
    }
}