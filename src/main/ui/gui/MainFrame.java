package ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import model.MealCollection;
import model.Meal;
import model.Event;
import model.EventLog;
import ui.gui.panels.*;

// The panel in which the Meal Planner is rendered. Manages top-level layout
public class MainFrame extends JFrame {
    private static final String INITIAL_NAME = "New Collection";

    private double screenRatio = 0.32;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private NavigationPanel navigationPanel;
    protected MealCollection myMeals;
    private ViewMealPanel viewMealPanel;
    private UpdateMealPanel updateMealPanel;
    private GroceryListPanel groceryListPanel;
    private FilterPanel filterPanel;

    // EFFECTS: Initializes the main frame and card layout
    public MainFrame() {
        initializeFrame();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        navigationPanel = new NavigationPanel(this);
        myMeals = new MealCollection(INITIAL_NAME);
        viewMealPanel = new ViewMealPanel(this);
        updateMealPanel = new UpdateMealPanel(this);
        groceryListPanel = new GroceryListPanel(this);
        filterPanel = new FilterPanel(this);
        addPanels();
        add(navigationPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "HomeView");
        setVisible(true);
        showView("HomePanel");
        addWindowListener(new WindowCloseHandler());
    }

    // MODIFIES: this
    // EFFECTS: Addes panels to the card layout
    private void addPanels() {
        mainPanel.add(new HomePanel(this), "HomePanel");
        mainPanel.add(new AddMealPanel(this), "AddMealPanel");
        mainPanel.add(filterPanel, "FilterPanel");
        mainPanel.add(groceryListPanel, "GroceryListPanel");
        mainPanel.add(viewMealPanel, "ViewMealPanel");
        mainPanel.add(updateMealPanel, "UpdateMealPanel");
        mainPanel.add(new SaveLoadPanel(this), "SaveLoadPanel");
    }

    // MODIFIES: this
    // EFFECTS: Initializes the size of the main frame
    private void initializeFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) (screenSize.width * screenRatio);
        int screenHeight = (int) (screenSize.height * screenRatio);

        setTitle("Peas of Cake");
        setSize(screenWidth, screenHeight);
        setMinimumSize(new Dimension(400, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // MODIFIES: this
    // EFFECTS: Shows the appropriate panel, only showing navigation panel if not on
    // the home screen
    public void showView(String viewName) {
        if (viewName.equals("ViewMealPanel")) {
            viewMealPanel.updateView();
        } else if (viewName.equals("GroceryListPanel")) {
            groceryListPanel.refreshGroceries();
        } else if (viewName.equals("FilterPanel")) {
            filterPanel.promptAndFilter();
        }
        cardLayout.show(mainPanel, viewName);
        if (viewName.equals("HomePanel")) {
            navigationPanel.setVisible(false);
        } else {
            navigationPanel.setVisible(true);
        }
        revalidate();
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: Switches to the update panel passing meal to update
    public void switchToUpdateView(Meal meal) {
        updateMealPanel.setMeal(meal);
        showView("UpdateMealPanel");
    }

    // EFFECTS: returns the MealCollection associated with this panel
    public MealCollection getMealCollection() {
        return myMeals;
    }

    // EFFECTS: sets the MealCollection associated with this panel
    public void setMealCollection(MealCollection loadedCollection) {
        myMeals = loadedCollection;
    }

    // MODIFIES: this
    // EFFECTS: custom refresh based on which panel is calling refresh
    public void refreshActivePanel() {
        for (Component comp : mainPanel.getComponents()) {
            if (comp.isVisible() && comp instanceof ViewMealPanel) {
                ((ViewMealPanel) comp).updateView();
            }
        }
    }

    // EFFECTS: Prints the event log for the session in the console
    public class WindowCloseHandler extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
            for (Event next : EventLog.getInstance()) {
                System.out.println(next.toString() + "\n");
            }
        }
    }
}
