package ui.gui.panels;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import ui.gui.MainFrame;
import model.*;

// Panel to display the current collection of meals
public class ViewMealPanel extends BasePanel {
    protected JPanel mealContainer;

    // EFFECTS: Initializes display for current meals in collection
    public ViewMealPanel(MainFrame controller) {
        super(controller);
        setLayout(new BorderLayout());

        mealContainer = new JPanel();
        mealContainer.setLayout(new BoxLayout(mealContainer, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(mealContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: Updates the the current meal collection
    public void updateView() {
        mealContainer.removeAll();
        List<Meal> meals = getController().getMealCollection().getMeals();
        for (Meal m : meals) {
            mealContainer.add(new MealCard(m, getController()));
        }
        mealContainer.add(Box.createVerticalGlue());
        revalidate();
        repaint();
    }

}
