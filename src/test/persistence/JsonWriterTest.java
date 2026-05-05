package persistence;

// Modeled on the JsonWriterTest in the JsonSerializationDemo

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import model.MealCollection;
import model.Meal;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterBadFileName() {
        try {
            JsonWriter writer = new JsonWriter("./data/TestMealCol\0ection");
            writer.open();
            fail("Error in file name, IOException expected");
        } catch (IOException e) {
            // pass, exception expected
        }
    }

    @Test
    void testWriterEmptyMealCollection() {
        try {
            MealCollection mc = new MealCollection("empty mc");
            JsonWriter writer = new JsonWriter("./data/writerTestEmptyMC.json");
            writer.open();
            writer.write(mc);
            writer.close();

            JsonReader reader = new JsonReader("./data/writerTestEmptyMC.json");
            mc = reader.read();
            assertEquals("empty mc", mc.getCollectionName());
            assertTrue(mc.getMeals().isEmpty());
        } catch (IOException e) {
            fail("Unexpected exception triggered: " + e);
        }
    }

    @Test
    void testWriterTestMealCollection() {
        try {
            MealCollection mc = new MealCollection("test mc");
            List<String> someIngredients = List.of("apple", "pie");
            mc.addMeal(new Meal("m1", 2.1, 3));
            mc.getMeal("m1").addManyIngredients(someIngredients);
            mc.addMeal(new Meal("m2", 2.2, 2));
            JsonWriter writer = new JsonWriter("./data/writerTestMC.json");
            writer.open();
            writer.write(mc);
            writer.close();

            JsonReader reader = new JsonReader("./data/writerTestMC.json");
            mc = reader.read();
            assertEquals("test mc", mc.getCollectionName());
            assertEquals(2, mc.getMeals().size());
            List<String> emptyList = new ArrayList<>();
            checkMeal("m1", 2.1, 3, 0, 0, true, someIngredients, mc.getMeals().get(0));
            checkMeal("m2", 2.2, 2, 0, 0, true, emptyList, mc.getMeals().get(1));
        } catch (IOException e) {
            fail("Unexpected exception triggered: " + e);
        }
    }

}
