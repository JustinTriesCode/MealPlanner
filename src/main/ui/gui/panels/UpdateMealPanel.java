package ui.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import model.Meal;
import ui.gui.MainFrame;

// Panel with options to update a meals attributes
public class UpdateMealPanel extends AddMealPanel {
    private Meal mealToUpdate;

    // EFFECTS: Initializes display to update a meal from collection
    public UpdateMealPanel(MainFrame controller) {
        super(controller);
        saveBtn.setText("Update Meal");
        clearBtn.setText("Reset");
    }

    // MODIFIES: this
    // EFFECTS: Brings up the meal to edit by assigning values
    public void setMeal(Meal meal) {
        mealToUpdate = meal;
        nameField.setText(mealToUpdate.getMealName());
        cookTimeSpinner.setValue(mealToUpdate.getCookTime());
        proteinSpinner.setValue(mealToUpdate.getProtein());
        effortSlider.setValue(mealToUpdate.getEffortLevel());
        ratingSlider.setValue(mealToUpdate.getRating());
        ingredientsField.setText(String.join(", ", mealToUpdate.getIngredients()));
        ingredientsCheck.setSelected(mealToUpdate.needsIngredients());

        for (ActionListener al : saveBtn.getActionListeners()) {
            saveBtn.removeActionListener(al);
        }
        saveBtn.addActionListener(new SaveHandler());
    }

    // MODIFIES: this
    // EFFECTS: Updates the instruction to be shown
    @Override
    public String setInstruction() {
        return "Updating meal: ";
    }

    // MODIFIES: this, mealCollection
    // EFFECTS: Updates the meal input to the meal collection, then clears inputs
    @Override
    public boolean saveMeal() {
        String newName = nameField.getText();
        if (!verifyName(newName, newName.equals(mealToUpdate.getMealName()))) {
            return false;
        }
        List<String> ingredientsList = new ArrayList<>(Arrays.asList(ingredientsField.getText().split(",")));
        mealToUpdate.updateMeal(newName, (Double) cookTimeSpinner.getValue(), (Integer) proteinSpinner.getValue(),
                (Integer) ratingSlider.getValue(), ingredientsList, (Integer) effortSlider.getValue(),
                ingredientsCheck.isSelected());
        clearFields();
        return true;
    }

    // MODIFIES: this
    // EFFECTS: Sets all input fields to current meal's values
    @Override
    public void clearFields() {
        setMeal(mealToUpdate);
    }

    // MODIFIES: this, MealCollection
    // EFFECTS: Updates the meal in collection
    protected class SaveHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean result = saveMeal();
            if (result) {
                JOptionPane.showMessageDialog(UpdateMealPanel.this, "Meal was updated", "Complete",
                        JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    // TODO: fix where you go after updating a meal

}
