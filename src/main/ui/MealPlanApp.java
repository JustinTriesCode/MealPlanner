package ui;

import model.MealCollection;
import model.Meal;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

// Represents the meal planner app as a consolse interface
public class MealPlanApp {
    private MealCollection myMeals;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECT: Runs the meal planner app and initializes a meal collection
    // and new scanner
    public MealPlanApp() {
        myMeals = new MealCollection("initial");
        input = new Scanner(System.in);
        runPlanner();
    }

    // MODIFIES: this
    // EFFECTS: runs the main application
    private void runPlanner() {
        boolean loop = true;
        String command = null;
        try {
            while (loop) {
                menuOptions();
                command = input.nextLine();

                if (command.equals("q")) {
                    loop = false;
                } else {
                    enterOption(command);
                }
            }
        } catch (Exception e) {
            System.out.println("\nError occured:" + e);
        }
        System.out.println("\nClosing meal planner...");
    }

    // EFFECTS: calls the corresponding method based on user input
    private void enterOption(String command) {
        if (command.equalsIgnoreCase("v")) {
            viewMeals();
        } else if (command.equalsIgnoreCase("a")) {
            addMeal();
        } else if (command.equalsIgnoreCase("l")) {
            loadSelectedCollection();
        } else if (!myMeals.getMeals().isEmpty()) {
            enterOptionForMeal(command);
        } else {
            System.out.println("Sorry, key not recognized, please try again.");
        }
    }

    // EFFECTS: calls the corresponding method based on input if there's >0 meals
    private void enterOptionForMeal(String command) {
        if (command.equalsIgnoreCase("r")) {
            removeMeal();
        } else if (command.equalsIgnoreCase("u")) {
            updateSetMeal();
        } else if (command.equalsIgnoreCase("f")) {
            filterCollection();
        } else if (command.equalsIgnoreCase("g")) {
            generateGroceryList();
        } else if (command.equalsIgnoreCase("s")) {
            saveMealCollectionAs();
        } else {
            System.out.println("Sorry, key not recognized, please try again.");
        }
    }

    // EFFECTS: prints out the list of options a user has
    private void menuOptions() {
        System.out.println("Please select what you would like to do:\n");
        System.out.println("\tPress  a  to add a meal to your collection");
        if (!myMeals.getMeals().isEmpty()) {
            menuOptionsForMeal();
        }
        System.out.println("\tPress  l  to load a meal collection");
        System.out.println("\tPress  q  to quit\n");
    }

    // EFFECTS: prints options that require 1 meal or more in collection
    private void menuOptionsForMeal() {
        System.out.println("\tPress  v  to view meals in collection");
        System.out.println("\tPress  r  to remove a meal from your collection");
        System.out.println("\tPress  u  to update a meal in your collection");
        System.out.println("\tPress  f  to filter you collection");
        System.out.println("\tPress  g  to generate a grocery list for your meals");
        System.out.println("\tPress  s  to save your meal collection");
    }

    // EFFECTS: prints out the list of meals currently in the collection, or
    // indicates there are no meals if empty
    private void viewMeals() {
        List<Meal> meals = myMeals.getMeals();
        if (meals.isEmpty()) {
            System.out.println("No meals in collection, please add a meal first.\n");
        } else {
            String formatting = "\t %-20s | %-9s | %-12s | %-7s | %-6s%n";
            System.out.println("You have the following meals in your collection:\n");
            System.out.printf(formatting, "Meal Name", "Cook Time", "Effort Level",
                    "Protein", "Rating");
            System.out.println("-".repeat(80));
            for (Meal m : meals) {
                System.out.printf(formatting, m.getMealName(), m.getCookTime() + " h",
                        "|".repeat(m.getEffortLevel()), m.getProtein() + " g", m.getRating() + "/5");
            }
        }
        enterToReturn();
    }

    // MODIFIES: this
    // EFFECT: adds a meal to collection with inputted attributes
    private void addMeal() {
        String mealName;
        double cookTime;
        int protein;

        System.out.println("\nPlease enter the name of the meal: ");
        mealName = input.nextLine();
        System.out.println("Please enter the estimated cook time (in hours): ");
        cookTime = stringToDouble();
        System.out.println("Please enter the amount of protein (in grams): ");
        protein = stringToInteger();

        myMeals.addMeal(new Meal(mealName, cookTime, protein));
        System.out.println("\nMeal added successfully");
        enterToReturn();
    }

    // MODIFIES: this
    // EFFECTS: removes the meal with the name user inputted or
    // indicates no such meal was found
    private void removeMeal() {
        System.out.println("\nPlease enter the name of the meal you would like to remove:\n");
        String mealName = input.nextLine();
        if (myMeals.hasMealName(mealName)) {
            myMeals.removeMeal(mealName);
            System.out.println("Meal successfully removed");
            enterToReturn();
        } else {
            String response = mealNotFound();
            if (response.equalsIgnoreCase("t")) {
                removeMeal();
            }
        }
    }

    // EFFECTS: takes user inputs to set the meal they will be updating
    private void updateSetMeal() {
        System.out.println("\nEnter the meal name you would like to update: ");
        String mealName = input.nextLine();
        Meal meal;
        if (!myMeals.hasMealName(mealName)) {
            String response = mealNotFound();
            if (response.equalsIgnoreCase("t")) {
                updateSetMeal();
            } else {
                return;
            }
        } else {
            meal = myMeals.getMeal(mealName);
            updateSetAttribute(meal);
        }
    }

    // EFFECTS: takes the users input of attribute to send them to the
    // corresponding method to update that attribute for the meal
    public void updateSetAttribute(Meal meal) {
        boolean keepTrying = true;
        while (keepTrying) {
            System.out.println("\nEnter the attribute you would like to update: ");
            updateOptions(meal.needsIngredients());
            String attribute = input.nextLine();
            if (attribute.isEmpty()) {
                keepTrying = false;
            } else {
                updateMealInput(meal, attribute);
            }
        }
    }

    // EFFECTS: prints the list of options the user has
    private void updateOptions(boolean needsIngredients) {
        System.out.println("\tPress n to rename the meal");
        System.out.println("\tPress c to update cook time");
        System.out.println("\tPress p to update protein");
        System.out.println("\tPress e to update effort");
        System.out.println("\tPress r to update rating");
        System.out.println("\tPress i to update ingredients");
        if (needsIngredients) {
            System.out.println("\tPress g if ingredients were purchased");
        } else {
            System.out.println("\tPress g if ingredients need to be repurchased");
        }
        System.out.println("\tPress enter to return");
    }

    // EFFECTS: redirects user to the proper method to update inputted attribute
    private void updateMealInput(Meal meal, String attribute) {
        if (attribute.equalsIgnoreCase("n")) {
            updateMealName(meal);
        } else if (attribute.equalsIgnoreCase("c")) {
            updateMealCookTime(meal);
        } else if (attribute.equalsIgnoreCase("p")) {
            updateMealProtein(meal);
        } else if (attribute.equalsIgnoreCase("e")) {
            updateMealEffort(meal);
        } else if (attribute.equalsIgnoreCase("r")) {
            updateMealRating(meal);
        } else if (attribute.equalsIgnoreCase("i")) {
            updateMealIngredients(meal);
        } else if (attribute.equalsIgnoreCase("g")) {
            updateNeedsIngredients(meal);
        } else {
            System.out.println("Sorry, key not recognized, please try again.");
        }
    }

    // MODIFIES: this
    // EFFECTS: updates the meal's name with inputted name
    private void updateMealName(Meal meal) {
        System.out.println("\nCurrent Name: " + meal.getMealName() + "\nPlease enter the new meal name");
        // input.nextLine();
        String newName = input.nextLine();
        meal.renameMeal(newName);
        System.out.println("Meal name was updated\n");
    }

    // MODIFIES: this
    // EFFECTS: updates the meal's cook time with inputted value
    private void updateMealCookTime(Meal meal) {
        System.out.println("\nCurrent Cook Time: " + meal.getCookTime() + "\nPlease enter the new cook time");
        double newTime = stringToDouble();
        meal.updateCookTime(newTime);
        System.out.println("Cook time was updated\n");
    }

    // MODIFIES: this
    // EFFECTS: updates the meal's protein with inputted value
    private void updateMealProtein(Meal meal) {
        System.out.println("\nCurrent Protein: " + meal.getProtein() + "\nPlease enter the new protein amount");
        int newProtein = stringToInteger();
        meal.updateProtein(newProtein);
        System.out.println("Protein was updated\n");
    }

    // MODIFIES: this
    // EFFECTS: increases meal's effort if + is input,
    // decreases meal's effort if - is input,
    // or exits if e is input
    private void updateMealEffort(Meal meal) {
        System.out.println("\nCurrent Effort: " + meal.getEffortLevel()
                + "\nPlease enter + to increase and - to decrease effort level");
        boolean keepTrying = true;
        while (keepTrying) {
            String effortChange = input.nextLine();
            if (effortChange.equals("+")) {
                meal.increaseEffort();
                keepTrying = false;
            } else if (effortChange.equals("-")) {
                meal.decreaseEffort();
                keepTrying = false;
            } else if (effortChange.equalsIgnoreCase("e")) {
                keepTrying = false;
                return;
            } else {
                System.out.println("Key not recognized, please try again or enter e to escape");
            }
        }
        System.out.println("Effort was updated\n");
    }

    // MODIFIES: this
    // EFFECTS: updates the meal's rating with inputted value
    private void updateMealRating(Meal meal) {
        System.out.println("\nCurrent Rating: " + meal.getRating() + "\nPlease enter the new rating out of 5");
        int newRating = stringToInteger();
        meal.rateMeal(newRating);
        System.out.println("Rating was updated\n");
        enterToReturn();
    }

    // MODIFIES: this
    // EFFECTS: adds list of string to the meal's ingredient list
    private void updateMealIngredients(Meal meal) {
        System.out.println("\nPlease enter the ingredients to add, separated by a comma");
        String newIngredients = input.nextLine();
        String[] ingredients = newIngredients.split(",");
        List<String> ingredientList = new ArrayList<>(Arrays.asList(ingredients));
        meal.addManyIngredients(ingredientList);
        System.out.println("Ingredient(s) were added\n");
        enterToReturn();
    }

    // MODIFIES: this
    // EFFECTS: changes the status of if the meal needs ingredients to the opposite
    private void updateNeedsIngredients(Meal meal) {
        if (meal.needsIngredients()) {
            meal.purchasedIngredients();
            System.out.println("\nIngredients marked as purchased\n");
        } else {
            meal.repurchaseIngredients();
            System.out.println("\nIngredients marked as needing to be purchased\n");
        }
        enterToReturn();
    }

    // EFFECT: prompts user for filters to apply to the meal collection,
    // then prints the meals that match the criteria
    private void filterCollection() {
        System.out.println("\nPlease enter the following criteria or -1 to skip\n");
        System.out.println("Max cook time (h): ");
        double maxCookTime = stringToDouble();
        System.out.println("Max effort (0-10): ");
        int maxEffort = stringToInteger();
        System.out.println("Min protein (g): ");
        int minProtein = stringToInteger();
        System.out.println("Min rating (0-5): ");
        int minRating = stringToInteger();
        List<Meal> filteredCollection = myMeals.filterMeals(maxCookTime, maxEffort, minProtein, minRating);
        if (filteredCollection.isEmpty()) {
            System.out.println("No meals found meeting criteria");
        } else {
            System.out.println("\nThe following meals meet your criteria");
            for (Meal m : filteredCollection) {
                System.out.println("\t" + m.getMealName());
            }
        }
        enterToReturn();
    }

    // EFFECTS: generates a grocery list of the ingredients still needed for meals
    private void generateGroceryList() {
        List<String> groceries = myMeals.getGroceryList();
        if (groceries.isEmpty()) {
            System.out.println("\nNo ingredients found for meals in collection");
        } else {
            System.out.println("\nGrocery List: ");
            for (String g : groceries) {
                System.out.println("\t[] " + g);
            }
        }
        enterToReturn();
    }

    // EFFECTS: prompts use to save file with given name
    private void saveMealCollectionAs() {
        String response = "n";
        String filePath = "";
        String collectionName = "initial";
        while (response.equalsIgnoreCase("n")) {
            System.out.println("Please enter the name you'd like to save the collection under: ");
            collectionName = input.nextLine().trim().toLowerCase();
            filePath = "./data/MC_" + collectionName + ".json";
            response = fileCheck(filePath);
        }
        if (response.equalsIgnoreCase("y")) {
            myMeals.updateCollectionName(collectionName);
            saveMealCollection(filePath);
        } else {
            System.out.println("Sorry, key not recognized.");
        }
        enterToReturn();
    }

    // EFFECTS: returns y if file does not exist, otherwise prompts user to confirm
    // if they want to overwrite file with same name
    private String fileCheck(String filePath) {
        File fileToCheck = new File(filePath);
        if (!fileToCheck.exists()) {
            return "y";
        }
        System.out.println("File already exists. Enter 'y' to overwrite, or 'n' to try a new name");
        String response = input.nextLine();
        return response;
    }

    // EFFECTS: prompts user to enter file to load, or view available records
    private void loadSelectedCollection() {
        displaySavedCollections();
        System.out.println(
                "Please enter the name of the collection you'd like to load: ");
        String collectionName = input.nextLine().trim().toLowerCase();
        String fileName = fileNameFormatted(collectionName);
        File fileToLoad = new File(fileName);
        if (fileToLoad.exists()) {
            loadMealCollection(fileName);
        } else {
            System.out.println("Could not find file named: " + collectionName);
        }
        enterToReturn();
    }

    // EFFECTS: displays the json meal collections already saved
    private void displaySavedCollections() {
        File folder = new File("./data/");
        File[] files = folder.listFiles();
        if (files != null) {
            System.out.println("The following meal collections are available:");
            for (File f : files) {
                String fileName = f.getName();
                if (fileName.startsWith("MC_") && fileName.endsWith(".json")) {
                    System.out.println("  -  " + fileName.replace("MC_", "").replace(".json", ""));
                }
            }
        }
    }

    // EFFECTS: reformats file name if not already in the proper format
    private String fileNameFormatted(String collectionName) {
        if (!collectionName.startsWith("MC_")) {
            collectionName = "MC_" + collectionName;
        }
        if (!collectionName.endsWith(".json")) {
            collectionName = collectionName + ".json";
        }
        collectionName = "./data/" + collectionName;
        return collectionName;
    }

    // EFFECTS: prompt to return to previous menu when user is ready
    private void enterToReturn() {
        System.out.println("\nPress Enter to return to previous menu");
        input.nextLine();
    }

    // EFFECTS: prompts user if meal was not found to either try again
    // or to return to the last menu
    private String mealNotFound() {
        System.out.println("\nMeal not found...");
        System.out.println("Press t to try again or enter to return to menu");
        String response = input.nextLine();
        return response;
    }

    // EFFECTS: helper for inputting integers to avoid issues with format
    private int stringToInteger() {
        while (true) {
            try {
                return Integer.parseInt(input.nextLine());
            } catch (NumberFormatException nfe) {
                System.out.println("Number not recognized, please enter a whole number without metric");
            }
        }
    }

    // EFFECTS: helper for inputting doubles to avoid issues with format
    private double stringToDouble() {
        while (true) {
            try {
                return Double.parseDouble(input.nextLine());
            } catch (NumberFormatException nfe) {
                System.out.println("Number not recognized, please enter a number such as 1.2");
            }
        }
    }

    // EFFECTS: saves the MealCollection to file
    private void saveMealCollection(String filePath) {
        jsonWriter = new JsonWriter(filePath);
        try {
            jsonWriter.open();
            jsonWriter.write(myMeals);
            jsonWriter.close();
            System.out.println("Saved " + myMeals.getCollectionName() + " to " + filePath);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + filePath);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads MealCollection from file
    private void loadMealCollection(String fileName) {
        try {
            jsonReader = new JsonReader(fileName);
            myMeals = jsonReader.read();
            System.out.println("Loaded " + fileName);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + fileName);
        }
    }
}
