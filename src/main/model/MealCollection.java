package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

// A collection of meals to pull from when making a meal plan
public class MealCollection implements Writable {
    private List<Meal> mealList;
    private String collectionName;

    // REQUIRES: meals have unique names and no duplicate meals
    public MealCollection(String name) {
        this.mealList = new ArrayList<>();
        collectionName = name;
    }

    // Getters

    public String getCollectionName() {
        return collectionName;
    }

    // EFFECTS: shows the list of meals in the collection
    public List<Meal> getMeals() {
        return new ArrayList<>(this.mealList);
    }

    // Methods for MealCollection class

    // EFFECTS: Returns true if meal name is in collection
    public Boolean hasMealName(String name) {
        for (Meal m : mealList) {
            if (m.getMealName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: Returns true if meal is in collection already
    public Boolean hasMeal(Meal m) {
        return mealList.contains(m);
    }

    // REQUIRES: index < mealList.size()
    // EFFECTS: Returns the meal at the given index
    public Meal getMeal(int index) {
        return mealList.get(index);
    }

    // EFFECTS: Returns the meal with the given name
    public Meal getMeal(String name) {
        for (Meal m : mealList) {
            if (m.getMealName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    // MODIFIES: this
    // EFFECTS: updates the collection's name
    public void updateCollectionName(String newName) {
        EventLog.getInstance().logEvent(new Event(collectionName + "meal collection renamed " + newName));
        this.collectionName = newName;
    }

    // MODIFIES: this
    // EFFECTS: adds one or more meals to the collection
    public void addMeal(Meal meal) {
        EventLog.getInstance().logEvent(new Event("Added meal to collection: " + meal.getMealName()));
        mealList.add(meal);
    }

    // MODIFIES: this
    // EFFECTS: removes the meal with passed name from the meal collection if
    // exists, otherwise it does nothing
    public void removeMeal(String mealName) {
        for (Meal m : mealList) {
            if (m.getMealName().equalsIgnoreCase(mealName)) {
                EventLog.getInstance().logEvent(new Event("Meal removed from collection: " + mealName));
                mealList.remove(m);
                break;
            }
        }
    }

    // EFFECTS: Returns a shopping list of unique ingredients needed
    public List<String> getGroceryList() {
        List<String> groceryList = new ArrayList<>();
        for (Meal m : mealList) {
            if (m.needsIngredients()) {
                for (String ingredient : m.getIngredients()) {
                    if (!groceryList.contains(ingredient)) {
                        groceryList.add(ingredient);
                    }
                }
            }
        }
        return groceryList;
    }

    // EFFECTS: returns a list of meals in the collection that
    // are below the cookTime or effortLevel limits, or have
    // at least the specified protein or rating
    public List<Meal> filterMeals(double cookTime, int effortLevel,
            int protein, int rating) {
        List<Meal> filteredList = new ArrayList<>();
        EventLog.getInstance().logEvent(
                new Event("Filtering for meals with: cookTime <= " + cookTime + ", effortLevel <= " + effortLevel
                        + ", protein >= " + protein + ", rating >= " + rating));

        for (Meal m : mealList) {
            if (matchesCookTime(m, cookTime) && matchesEffort(m, effortLevel)
                    && matchesProtein(m, protein) && matchesRating(m, rating)) {
                filteredList.add(m);
                EventLog.getInstance().logEvent(new Event(m.getMealName() + " met filter criteria."));
            }
        }
        return filteredList;
    }

    // Helper functions for filtering
    // EFFECTS: Returns true if m.cookTime <= cookTime or equal -1
    private boolean matchesCookTime(Meal m, double cookTime) {
        return (m.getCookTime() <= cookTime) || cookTime == -1;
    }

    // EFFECTS: Returns true if m.effortLevel <= effortLevel or equal -1
    private boolean matchesEffort(Meal m, int effortLevel) {
        return (m.getEffortLevel() <= effortLevel) || effortLevel == -1;
    }

    // EFFECTS: Returns true if m.protein >= protein or equal -1
    private boolean matchesProtein(Meal m, int protein) {
        return protein == -1 || (m.getProtein() >= protein);
    }

    // EFFECTS: Returns true if m.rating >= rating or equal -1
    private boolean matchesRating(Meal m, int rating) {
        return rating == -1 || (m.getRating() >= rating);
    }

    // EFFECTS: creates a JSON object of the meal collection
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("collectionName", collectionName);
        json.put("mealList", mealsToJson());
        return json;
    }

    // EFFECTS: returns meals in the collection as a JSON array
    private JSONArray mealsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Meal m : mealList) {
            jsonArray.put(m.toJson());
        }
        return jsonArray;
    }
}
