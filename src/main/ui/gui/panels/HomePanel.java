package ui.gui.panels;

import javax.swing.*;
import java.awt.GridLayout;

import ui.gui.MainFrame;

// The home menu, also the inital menu when starting up the application
public class HomePanel extends BasePanel {
    private static final ImageIcon VIEW_MEAL_ICON = new ImageIcon("src/main/resources/icons/viewMeals.png");
    private static final ImageIcon ADD_ICON = new ImageIcon("src/main/resources/icons/add.png");
    private static final ImageIcon LIST_FILTER_ICON = new ImageIcon("src/main/resources/icons/listFilter.png");
    private static final ImageIcon GROCERY_LIST_ICON = new ImageIcon("src/main/resources/icons/groceryList.png");
    private static final ImageIcon SAVE_ICON = new ImageIcon("src/main/resources/icons/save.png");
    protected JButton viewBtn;
    protected JButton groceryListBtn;
    protected JButton filterBtn;
    protected JButton addMealBtn;
    protected JButton saveBtn;

    // EFFECTS: Displays a panel with links to each of the other panels
    public HomePanel(MainFrame controller) {
        super(controller);

        setLayout(new GridLayout(0, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        viewBtn = makeButton("View", "ViewMealPanel", VIEW_MEAL_ICON);
        addMealBtn = makeButton("Add", "AddMealPanel", ADD_ICON);
        filterBtn = makeButton("Filter", "FilterPanel", LIST_FILTER_ICON);
        groceryListBtn = makeButton("Groceries", "GroceryListPanel", GROCERY_LIST_ICON);
        saveBtn = makeButton("Save/Load", "SaveLoadPanel", SAVE_ICON);
    }

}
