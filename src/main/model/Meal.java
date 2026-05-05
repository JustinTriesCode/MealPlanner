package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

/*  Represents a meal having a name, cook time, effort level (1-10 scale), 
    protein (in grams), list of ingredients, rating (1-5 rating),
    and an indicator if ingredients are still required for the meal */
public class Meal implements Writable {
    private String mealName;
    private double cookTime;
    private int effortLevel;
    private int protein;
    private List<String> ingredients;
    private int rating;
    private boolean needIngredients;

    /*
     * Represents a meal with given name, cook time, and protein
     * effort and rating are set to 0 by default, meal is set as
     * needing ingredients with an empty list of ingredients
     */
    public Meal(String mealName, double cookTime, int protein) {
        this.mealName = mealName;
        this.cookTime = cookTime;
        effortLevel = 0;
        this.protein = protein;
        ingredients = new ArrayList<>();
        rating = 0;
        needIngredients = true;

    }

    // EFFECTS: constructs a meal from JSON
    public Meal(JSONObject jo) {
        this(jo.getString("mealName"), jo.getDouble("cookTime"), jo.getInt("protein"));
        this.setEffort(jo.optInt("effortLevel", 0));
        this.addManyIngredients(ingredients);
        this.rateMeal(jo.optInt("rating", 0));
        this.setNeedIngredients(jo.optBoolean("needIngredients", true));
        this.addManyIngredients(parseIngredients(jo));
    }

    // Getters

    public String getMealName() {
        return mealName;
    }

    public double getCookTime() {
        return cookTime;
    }

    public int getEffortLevel() {
        return effortLevel;
    }

    public int getProtein() {
        return protein;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public int getRating() {
        return rating;
    }

    // EFFECT: returns true if the meal still needs ingredients
    public boolean needsIngredients() {
        return needIngredients;
    }

    // Methods for Meal class

    // MODIFIES: this
    // EFFECTS: renames the meal
    public void renameMeal(String name) {
        this.mealName = name;
    }

    // REQUIRES: time > 0
    // MODIFIES: this
    // EFFECTS: updates the time required to make the meal
    public void updateCookTime(double time) {
        this.cookTime = time;
    }

    // MODIFIES: this
    // EFFECTS: updates the protein in the meal
    public void updateProtein(int protein) {
        this.protein = protein;
    }

    // MODIFIES: this
    // EFFECTS: increases the meal's effort level by 1 if currently less than 10
    public void increaseEffort() {
        if (this.getEffortLevel() < 10) {
            effortLevel++;
        }
    }

    // MODIFIES: this
    // EFFECTS: decreases the meal's effort level by 1 if currently more than 0
    public void decreaseEffort() {
        if (this.getEffortLevel() > 0) {
            effortLevel--;
        }
    }

    // MODIFIES: this
    // EFFECTS: set the meal's effort to value
    public void setEffort(int value) {
        this.effortLevel = value;
    }

    // REQUIRES: 0 <= stars <= 5
    // MODIFIES: this
    // EFFECTS: sets the rating of the meal
    public void rateMeal(int stars) {
        this.rating = stars;
    }

    // MODIFIES: this
    // EFFECTS: changes the needsIngredients status to false
    public void purchasedIngredients() {
        this.needIngredients = false;
    }

    // MODIFIES: this
    // EFFECTS: changes the needsIngredients status to true
    public void repurchaseIngredients() {
        this.needIngredients = true;
    }

    // MODIFIES: this
    // EFFECTS: changes the needsIngredients status to status
    public void setNeedIngredients(boolean status) {
        this.needIngredients = status;
    }

    // MODIFIES: this
    // EFFECTS: replaces the existing ingredient list
    public void replaceIngredients(List<String> ingredients) {
        this.ingredients.clear();
        addManyIngredients(ingredients);
    }

    // MODIFIES: this
    // EFFECTS: adds ingredient to list of ingredients for meal if not already in
    // list, all in lower case and removes trail/leading white space
    public void addIngredient(String ingredient) {
        ingredient = ingredient.toLowerCase().trim();
        if (!ingredients.contains(ingredient) && !ingredient.isEmpty()) {
            ingredients.add(ingredient);
        }
    }

    // MODIFIES: this
    // EFFECTS: add all ingredients passed to the meal's list of ingredients
    // if ingredient isn't already in list
    public void addManyIngredients(List<String> ingredients) {
        for (String i : ingredients) {
            addIngredient(i);
        }
    }

    // REQUIRES: ingredient list must not be empty
    // MODIFIES: this
    // EFFECTS: remove an ingredient from list of ingredients
    public void removeIngredient(String ingredient) {
        ingredient = ingredient.toLowerCase().trim();
        if (ingredients.contains(ingredient)) {
            ingredients.remove(ingredient);
        }
    }

    // MODIFIES: this
    // EFFECTS: Updates existing meal with passed parameters
    public void updateMeal(String newName, double newCookTime, int newProtein, int newRating,
            List<String> newIngredients, int newEffort, boolean newStatus) {
        this.renameMeal(newName);
        this.updateCookTime(newCookTime);
        this.updateProtein(newProtein);
        this.rateMeal(newRating);
        this.replaceIngredients(newIngredients);
        this.setEffort(newEffort);
        this.setNeedIngredients(newStatus);
        EventLog.getInstance().logEvent(new Event(newName + " has been updated."));
    }

    // EFFECTS: returns parsed ingredient list from ingredients
    private List<String> parseIngredients(JSONObject jo) {
        List<String> ingredientList = new ArrayList<>();
        JSONArray ingredientArray = jo.getJSONArray("ingredients");
        for (int i = 0; i < ingredientArray.length(); i++) {
            String ingredient = ingredientArray.getString(i);
            ingredientList.add(ingredient);
        }

        return ingredientList;
    }

    // EFFECTS: creates a json object of the meal
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("mealName", mealName);
        json.put("protein", protein);
        json.put("cookTime", cookTime);
        json.put("effortLevel", effortLevel);
        json.put("rating", rating);
        json.put("ingredients", ingredientsToJson());
        json.put("needIngredients", needIngredients);
        return json;
    }

    // EFFECTS: returns ingredients in the meal as a JSON array
    private JSONArray ingredientsToJson() {
        JSONArray ingredientJson = new JSONArray();
        for (String s : ingredients) {
            ingredientJson.put(s);
        }
        return ingredientJson;
    }
}
