package persistence;

import org.json.JSONObject;

// Modeled on the Writable class in the JsonSerializationDemo

// Interface for JSONObject used in Meal and MealCollection
public interface Writable {

    // EFFECTS: returns as JSON object
    JSONObject toJson();
}
