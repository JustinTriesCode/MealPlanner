package persistence;

// Modeled on the JsonReader class in the JsonSerializationDemo

import model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.json.*;

// Represents a reader that reads MealCollections from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs a reader for Meal and MealCollection
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads a MealCollection from JSON file and returns it;
    // throws IOException if an error triggers
    public MealCollection read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMealCollection(jsonObject);
    }

    // EFFECTS: read the file at the source and returns it;
    // throws IOException if an error triggers
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses MealCollection from JSON and returns it
    private MealCollection parseMealCollection(JSONObject jo) {
        String name = jo.getString("collectionName");
        MealCollection mc = new MealCollection(name);
        addMeals(mc, jo);
        EventLog.getInstance().logEvent(new Event("Finished loading collection: " + source));
        return mc;
    }

    // MODIFIES: MealCollection
    // EFFECTS: parses multiple Meals from JSON and adds them to MealCollection
    private void addMeals(MealCollection mc, JSONObject jo) {
        JSONArray jsonArray = jo.getJSONArray("mealList");
        for (Object json : jsonArray) {
            JSONObject nextMeal = (JSONObject) json;
            addMeal(mc, nextMeal);
        }
    }

    // MODIFIES: MealCollection
    // EFFECTS: parses Meal from JSON and adds it to MealCollection
    private void addMeal(MealCollection mc, JSONObject jo) {
        Meal meal = new Meal(jo);

        mc.addMeal(meal);
    }
}
