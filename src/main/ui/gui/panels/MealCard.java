package ui.gui.panels;

import ui.gui.MainFrame;
import model.Meal;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

// Panel to display meal and meal information
public class MealCard extends JPanel {
    protected static final Color BACKGROUND_COLOUR = new Color(230, 233, 233);
    protected static final ImageIcon REMOVE_ICON = new ImageIcon("src/main/resources/icons/remove.png");
    protected static final ImageIcon UPDATE_ICON = new ImageIcon("src/main/resources/icons/update.png");
    protected static final ImageIcon GROCERY_ICON = new ImageIcon("src/main/resources/icons/groceryList.png");
    private Meal meal;
    private MainFrame controller;

    // EFFECTS: Displays the meal information
    public MealCard(Meal meal, MainFrame controller) {
        this.meal = meal;
        this.controller = controller;
        this.meal = meal;
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOUR);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        JPanel infoPanel = new JPanel();
        creatInfoLabels(meal, infoPanel);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(makeEditButton());
        buttonPanel.add(makeRemoveButton());
        buttonPanel.add(viewIngredientsButton());
        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);
    }

    // MODIFIES: this
    // EFFECTS: Create and adds the info labels to the panel
    private void creatInfoLabels(Meal meal, JPanel infoPanel) {
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel("<html><b>" + meal.getMealName() + "</b></html>"));
        infoPanel.add(new JLabel("Cook Time: " + meal.getCookTime() + " min"));
        infoPanel.add(new JLabel("Effort: " + meal.getEffortLevel()));
        infoPanel.add(new JLabel("Protein: " + meal.getProtein() + "g"));
        infoPanel.add(new JLabel("Rating: " + meal.getRating() + "/5"));
        infoPanel.setBackground(BACKGROUND_COLOUR);
        JCheckBox ingredientsCheck = new JCheckBox("Needs Ingredients?");
        ingredientsCheck.setSelected(meal.needsIngredients());
        ingredientsCheck.setEnabled(false);
        ingredientsCheck.setOpaque(true);
        infoPanel.add(ingredientsCheck);
    }

    // EFFECTS: Returns a button that switches to the update view for this meal
    private JButton makeEditButton() {
        JButton button = new JButton(UPDATE_ICON);
        button.addActionListener(new EditHandler());
        return button;
    }

    // EFFECTS: Returns a button that removes this meal and refreshes the view
    private JButton makeRemoveButton() {
        JButton button = new JButton(REMOVE_ICON);
        button.addActionListener(new RemoveHandler());
        return button;
    }

    // EFFECTS: Returns a button that shows ingredients for the meal and refreshes
    // the view
    private JButton viewIngredientsButton() {
        JButton button = new JButton(GROCERY_ICON);
        button.addActionListener(new ViewIngredientHandler());
        return button;
    }

    // MODIFIES: this
    // EFFECTS: Updates the view to update menu
    private class EditHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            controller.switchToUpdateView(meal);
        }
    }

    // MODIFIES: this, MealCollection
    // EFFECTS: Removes the meal and refreshes the list
    private class RemoveHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            controller.getMealCollection().removeMeal(meal.getMealName());
            controller.refreshActivePanel();
        }
    }

    // EFFECTS: Makes a popup with ingredients for meal
    private class ViewIngredientHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            StringBuilder builder = new StringBuilder();
            for (String i : meal.getIngredients()) {
                builder.append("- " + i + "\n");
            }
            String ingredients;
            if (builder.length() > 0) {
                ingredients = builder.toString();
            } else {
                ingredients = "No ingredients added.";
            }
            JOptionPane.showMessageDialog(null, ingredients, meal.getMealName() + "'s ingredients: ",
                    JOptionPane.PLAIN_MESSAGE, GROCERY_ICON);
        }
    }
}
