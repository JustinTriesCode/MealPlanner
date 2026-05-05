package ui.gui.panels;

import java.util.List;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import ui.gui.MainFrame;
import persistence.*;

// Panel with options to save and load meal collection data
public class SaveLoadPanel extends BasePanel {
    private static final String SAVE_PROMPT = "Please enter the name of your collection: ";
    private static final String LOAD_PROMPT = "Please select the file you want to load.";
    private static final String OVERWRITE_PROMPT = "File already exists, would you like to overwrite?";
    private static final String FILE_PATH = "./data/";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JButton saveButton;
    private JButton loadButton;

    // EFFECTS: Initializes panel with save/ load buttons
    public SaveLoadPanel(MainFrame controller) {
        super(controller);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        setBackground(BACKGROUND_COLOUR);

        saveButton = new JButton("Save Meal Collection");
        loadButton = new JButton("Load Meal Collection");
        saveButton.addActionListener(new SaveActionListener());
        loadButton.addActionListener(new LoadActionListener());

        add(saveButton);
        add(loadButton);
    }

    // MODIFIES: this, MealCollection
    // EFFECTS: Prompts use to save file with given name
    private void saveMealCollectionAs() {
        String collectionName = JOptionPane.showInputDialog(this, SAVE_PROMPT, "Save Meals",
                JOptionPane.PLAIN_MESSAGE);
        if (collectionName == null || collectionName.trim().isEmpty()) {
            return;
        }
        String filePath = FILE_PATH + "MC_" + collectionName.trim().toLowerCase() + ".json";
        File fileToCheck = new File(filePath);
        if (fileToCheck.exists()) {
            int overwriteConfirm = JOptionPane.showConfirmDialog(this, OVERWRITE_PROMPT, "Confirm Overwrite",
                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (overwriteConfirm != JOptionPane.YES_NO_OPTION) {
                saveMealCollectionAs();
                return;
            }
        }
        trySave(collectionName, filePath);
    }

    // MODIFIES: this, MealCollection
    // EFFECTS: saves the MealCollection to file, updating MealCollectionName
    private void trySave(String collectionName, String filePath) {
        try {
            controller.getMealCollection().updateCollectionName(collectionName);
            jsonWriter = new JsonWriter(filePath);
            jsonWriter.open();
            jsonWriter.write(controller.getMealCollection());
            jsonWriter.close();
            JOptionPane.showMessageDialog(this, "File saved: " + filePath);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), "Save Error",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: Prompts user to enter file to load, or view available records
    private void loadSelectedCollection() {
        List<String> files = displaySavedCollections();
        if (files == null || files.size() == 0) {
            return;
        }
        for (String file : files) {
            file = file.replace("MC_", "").replace(".json", "");
        }
        String selected = (String) JOptionPane.showInputDialog(this, LOAD_PROMPT, "Load meal collection",
                JOptionPane.PLAIN_MESSAGE, null, files.toArray(), files.get(0));
        if (selected != null) {
            selected = fileNameFormatted(selected);
            loadMealCollection(selected);
            JOptionPane.showMessageDialog(this, selected + " loaded", "Meals Loaded", JOptionPane.PLAIN_MESSAGE);
        }
    }

    // MODIFIES: this, controller
    // EFFECTS: loads MealCollection from file
    private void loadMealCollection(String fileName) {
        try {
            jsonReader = new JsonReader(fileName);
            controller.setMealCollection(jsonReader.read());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading meals: " + e.getMessage(), "Load Error",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    // EFFECTS: displays the json meal collections already saved
    private List<String> displaySavedCollections() {
        File folder = new File(FILE_PATH);
        File[] allFiles = folder.listFiles();
        List<String> validFiles = new ArrayList<>();
        if (allFiles != null) {
            for (File f : allFiles) {
                String fileName = f.getName();
                if (fileName.startsWith("MC_") && fileName.endsWith(".json")) {
                    validFiles.add(fileName);
                }
            }
        }
        return validFiles;
    }

    // EFFECTS: reformats file name if not already in the proper format, then
    // returns the name
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

    // MODIFIES: this
    // EFFECTS: Saves the collection then returns to home panel
    private class SaveActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveMealCollectionAs();
            controller.showView("HomePanel");
        }
    }

    // MODIFIES: this
    // EFFECTS: Loads the collection then returns to home panel
    private class LoadActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadSelectedCollection();
            controller.showView("HomePanel");
        }
    }

}
