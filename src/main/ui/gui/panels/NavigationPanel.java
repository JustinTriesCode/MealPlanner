package ui.gui.panels;

import javax.swing.*;
import java.awt.*;

import ui.gui.MainFrame;

// Sidebar panel to navigate between the various options in the app
public class NavigationPanel extends BasePanel {
    protected JButton viewBtn;
    protected JButton groceryListBtn;
    protected JButton filterBtn;
    protected JButton addMealBtn;
    protected JButton homeBtn;
    protected JButton saveBtn;
    private ImageIcon homeIcon = new ImageIcon("src/main/resources/icons/home.png");
    private ImageIcon viewMealsIcon = new ImageIcon("src/main/resources/icons/viewMeals.png");
    private ImageIcon addIcon = new ImageIcon("src/main/resources/icons/add.png");
    private ImageIcon listFilterIcon = new ImageIcon("src/main/resources/icons/listFilter.png");
    private ImageIcon groceryListIcon = new ImageIcon("src/main/resources/icons/groceryList.png");
    private ImageIcon saveIcon = new ImageIcon("src/main/resources/icons/save.png");

    // EFFECTS: Creates side panel to quickly navigate to other panels
    public NavigationPanel(MainFrame controller) {
        super(controller);

        setLayout(new GridLayout(0, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initializeNavigationButtons();
    }

    // MODIFIES: this
    // EFFECTS: Adds buttons to navigate to other panels
    public void initializeNavigationButtons() {

        homeBtn = makeButton("Home", "HomePanel", homeIcon);
        viewBtn = makeButton("View", "ViewMealPanel", viewMealsIcon);
        addMealBtn = makeButton("Add", "AddMealPanel", addIcon);
        filterBtn = makeButton("Filter", "FilterPanel", listFilterIcon);
        groceryListBtn = makeButton("Groceries", "GroceryListPanel", groceryListIcon);
        saveBtn = makeButton("Save/Load", "SaveLoadPanel", saveIcon);
    }

}
