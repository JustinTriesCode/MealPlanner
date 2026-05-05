package ui.gui.panels;

import java.awt.GridLayout;
import java.util.List;
import javax.swing.*;

import ui.gui.MainFrame;
import model.Meal;

// Panel with menu to apply criteria to return a filtered collection
public class FilterPanel extends ViewMealPanel {
    private static final int GAP = 10;
    protected double maxCookTime = -1;
    protected int maxEffort = 10;
    protected int minProtein = -1;
    protected int minRating = 0;
    protected JSpinner cookTimeSpinner;
    protected JSpinner proteinSpinner;
    protected JSlider effortSlider;
    protected JSlider ratingSlider;
    protected JLabel instructions;
    protected JPanel filters;

    // EFFECTS: Display for filtering the meal collection
    public FilterPanel(MainFrame controller) {
        super(controller);
    }

    // MODIFIES: this
    // EFFECTS: Creates a popup to request parameters for filtering
    private void showFilterPopup() {
        JPanel filters = new JPanel(new GridLayout(0, 2, GAP, GAP));
        cookTimeSpinner = new JSpinner(new SpinnerNumberModel(-1.0, -1.0, 100.0, 0.5));
        effortSlider = new JSlider(0, 10, 10);
        effortSlider.setMajorTickSpacing(1);
        effortSlider.setPaintLabels(true);
        proteinSpinner = new JSpinner(new SpinnerNumberModel(-1, -1, 1000, 5));
        ratingSlider = new JSlider(0, 5, 0);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintLabels(true);
        addFilters(filters);
        int result = JOptionPane.showConfirmDialog(this, filters,
                "Filter Meals", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            applyFilters(
                    (Double) cookTimeSpinner.getValue(),
                    effortSlider.getValue(),
                    (Integer) proteinSpinner.getValue(),
                    ratingSlider.getValue());
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds the labels and filters to the panel
    private void addFilters(JPanel filters) {
        filters.add(new JLabel("Max Cook Time (h):"));
        filters.add(cookTimeSpinner);
        filters.add(new JLabel("Max Effort (0-10):"));
        filters.add(effortSlider);
        filters.add(new JLabel("Min Protein (g):"));
        filters.add(proteinSpinner);
        filters.add(new JLabel("Min Rating (0-5):"));
        filters.add(ratingSlider);
    }

    // MODIFIES: this
    // EFFECTS: Applies the filters the user inputs
    public void applyFilters(double cook, int effort, int protein, int rating) {
        this.maxCookTime = cook;
        this.maxEffort = effort;
        this.minProtein = protein;
        this.minRating = rating;

        updateView();
    }

    // EFFECTS: Call to run filter popup
    public void promptAndFilter() {
        showFilterPopup();
    }

    // MODIFIES: this
    // EFFECTS: Updates the the current meal collection shown
    @Override
    public void updateView() {
        mealContainer.removeAll();
        List<Meal> filtered = getController().getMealCollection()
                .filterMeals(maxCookTime, maxEffort, minProtein, minRating);

        for (Meal m : filtered) {
            mealContainer.add(new MealCard(m, getController()));
        }
        revalidate();
        repaint();
    }
}
