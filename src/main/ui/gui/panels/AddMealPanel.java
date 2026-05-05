package ui.gui.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ui.gui.MainFrame;
import model.Meal;

// Panel with form to add a new meal to the collection
public class AddMealPanel extends BasePanel {
    protected static final Color BACKGROUND_COLOUR = new Color(230, 233, 233);
    private static final double FIRST_COL_RATIO = 0.2;
    private static final double SECOND_COL_RATIO = 1 - FIRST_COL_RATIO;
    private static final int GAP = 10;
    private static final int CENTER = SwingConstants.CENTER;

    protected JTextField nameField;
    protected JSpinner cookTimeSpinner;
    protected JSpinner proteinSpinner;
    protected JSlider effortSlider;
    protected JSlider ratingSlider;
    protected JTextField ingredientsField;
    protected JCheckBox ingredientsCheck;
    protected JPanel form;
    protected JLabel instructions;
    protected JButton saveBtn;
    protected JButton clearBtn;

    private String mealName;
    private double cookTime;
    private int effortLevel;
    private int protein;
    private List<String> ingredients;
    private int rating;
    private boolean needIngredients;

    // EFFECTS: Initialize the panel to input parameters for adding a meal
    public AddMealPanel(MainFrame controller) {
        super(controller);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(BACKGROUND_COLOUR);
        instructions = new JLabel(setInstruction());
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructions.setBorder(BorderFactory.createEmptyBorder(GAP, 0, 20, GAP));
        instructions.setFont(MAIN_FONT);
        add(instructions);

        form = new JPanel(new GridBagLayout());
        addHeaderRow("Field", "Value / Input");
        addAllFormRows(1);

        JScrollPane scrollPane = new JScrollPane(form);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(BACKGROUND_COLOUR);
        add(scrollPane);
        addButtons();
    }

    // MODIFIES: this
    // EFFECTS: Set the instruction to display, has a defaul text
    public String setInstruction() {
        return "Add a New Meal";
    }

    // MODIFIES: this
    // EFFECTS: Creates the buttons and adds them to the panel
    private void addButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, GAP * 2, GAP));
        saveBtn = new JButton("Save Meal");
        saveBtn.addActionListener(new SaveHandler());
        clearBtn = new JButton("Clear Form");
        clearBtn.addActionListener(new ClearFieldHandler());
        buttonPanel.add(saveBtn);
        buttonPanel.add(clearBtn);
        add(buttonPanel);
    }

    // MODIFIES: this
    // EFFECTS: Runs formatting for all rows/fields in the input form
    private void addAllFormRows(int row) {
        nameField = new JTextField();
        addFormRow("Meal name:", nameField, row++);
        cookTimeSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 500.0, 1.0));
        addFormRow("Cook Time (min):", cookTimeSpinner, row++);
        effortSlider = getRatingSlider(0, 10);
        addFormRow("Effort Level (0-10):", effortSlider, row++);
        proteinSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        addFormRow("Protein (g):", proteinSpinner, row++);
        ratingSlider = getRatingSlider(0, 5);
        addFormRow("Meal Rating (0-5):", ratingSlider, row++);
        ingredientsField = new JTextField();
        addFormRow("Ingredients:", ingredientsField, row++);
        ingredientsCheck = new JCheckBox();
        addFormRow("Needs Ingredients?", ingredientsCheck, row++);
    }

    // MODIFIES: this
    // EFFEECTS: Adds a formatted header row to the form
    public void addHeaderRow(String first, String second) {
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.insets = new Insets(10, 5, 15, 5);
        constraint.fill = GridBagConstraints.HORIZONTAL;

        JLabel label1 = new JLabel(first, CENTER);
        label1.setFont(label1.getFont().deriveFont(Font.BOLD));
        constraint.gridx = 0;
        constraint.weightx = FIRST_COL_RATIO;
        form.add(label1, constraint);

        JLabel label2 = new JLabel(second, CENTER);
        label2.setFont(label2.getFont().deriveFont(Font.BOLD));
        constraint.gridx = 1;
        constraint.weightx = SECOND_COL_RATIO;
        form.add(label2, constraint);
    }

    // EFFECTS: Adds a formatted label and component to the grid
    private void addFormRow(String labelText, JComponent input, int row) {
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.gridy = row;
        constraint.insets = new Insets(5, 5, 5, 5);
        constraint.fill = GridBagConstraints.HORIZONTAL;

        constraint.gridx = 0;
        constraint.weightx = FIRST_COL_RATIO;
        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        form.add(label, constraint);
        constraint.gridx = 1;
        constraint.weightx = SECOND_COL_RATIO;
        form.add(input, constraint);
    }

    // EFFECTS: Returns a slider to set Meal rating
    private JSlider getRatingSlider(int min, int max) {
        JSlider ratingSlider = new JSlider(min, max, 0);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
        ratingSlider.setSnapToTicks(true);
        ratingSlider.addChangeListener(new RatingSliderListener());

        return ratingSlider;
    }

    // MODIFIES: this
    // EFFECTS: Handles the logic for use selection on rating slider
    private class RatingSliderListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();

            if (!source.getValueIsAdjusting()) {
                if (source == ratingSlider) {
                    rating = source.getValue();
                } else if (source == effortSlider) {
                    effortLevel = source.getValue();
                }
            }
        }
    }

    // REQUIRES: Fields must not be empty, except Rating
    // MODIFIES: this, MealCollection
    // EFFECTS: Saves the meal input to the meal collection, then clears inputs.
    // Returns false if meal name is invalid.
    public boolean saveMeal() {
        String name = nameField.getText();
        if (!verifyName(name, false)) {
            return false;
        }
        mealName = name;
        cookTime = (double) cookTimeSpinner.getValue();
        protein = (int) proteinSpinner.getValue();
        Meal meal = new Meal(mealName, cookTime, protein);
        ingredients = Arrays.asList(ingredientsField.getText().split(","));
        needIngredients = ingredientsCheck.isSelected();
        meal.updateMeal(mealName, cookTime, protein, rating, ingredients, effortLevel, needIngredients);
        controller.getMealCollection().addMeal(meal);
        clearFields();
        return true;
    }

    // MODIFIES: this
    // EFFECTS: returns false if name is empty or [already in collection and not
    // current meal], displays message accordingly, returns true otherwise
    protected boolean verifyName(String name, boolean toUpdate) {
        if (name.equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter a meal name", "Missing Name Error",
                    JOptionPane.PLAIN_MESSAGE);
            nameField.requestFocus();
            return false;
        } else if (!toUpdate && controller.getMealCollection().hasMealName(name)) {
            JOptionPane.showMessageDialog(this, "Please enter a unique meal name", "Duplicate Name Error",
                    JOptionPane.PLAIN_MESSAGE);
            nameField.requestFocus();
            return false;
        }
        return true;
    }

    // MODIFIES: this, MealCollection
    // EFFECTS: Registers meal and notifies user of completion
    protected class SaveHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean result = saveMeal();
            if (result) {
                JOptionPane.showMessageDialog(AddMealPanel.this, "Meal added to collection", "Complete",
                        JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Values to use for reset
    public void clearFields() {
        nameField.setText("");
        cookTimeSpinner.setValue(0.0);
        effortSlider.setValue(0);
        ratingSlider.setValue(0);
        ingredientsField.setText("");
        ingredientsCheck.setSelected(false);
        proteinSpinner.setValue(0);
    }

    // MODIFIES: this
    // EFFECTS: Restets the form to default values
    protected class ClearFieldHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clearFields();
        }
    }

}
