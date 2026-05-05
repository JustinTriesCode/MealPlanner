package ui.gui.panels;

import javax.swing.*;

import java.awt.BorderLayout;
import java.util.List;

import ui.gui.MainFrame;

// Panel to show grocery list for meal plan
public class GroceryListPanel extends BasePanel {
    private List<String> groceries;
    private JScrollPane groceryScroll;
    private JTextArea groceryText;

    // EFFECTS: Displays the list of ingredients needed
    public GroceryListPanel(MainFrame controller) {
        super(controller);
        setLayout(new BorderLayout());
        groceries = controller.getMealCollection().getGroceryList();

        groceryText = new JTextArea(generateList());
        groceryText.setEditable(false);
        groceryText.setVisible(true);

        groceryScroll = new JScrollPane(groceryText);
        groceryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(groceryScroll, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: Generates grocery list for meals in collection of meals that still
    // need ingredients
    public String generateList() {
        StringBuilder builder = new StringBuilder("Grocery List: \n");
        for (String g : groceries) {
            builder.append("- ").append(g).append("\n");
        }
        return builder.toString();
    }

    // EFFECTS: Refreshes the data to ensure added meals show up
    public void refreshGroceries() {
        groceries = getController().getMealCollection().getGroceryList();
        groceryText.setText(generateList());
    }
}
