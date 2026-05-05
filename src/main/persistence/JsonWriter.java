package persistence;

// Modeled on the JsonWriter class in the JsonSerializationDemo

import model.MealCollection;
import model.EventLog;
import model.Event;

import org.json.JSONObject;
import java.io.*;

// Represents a writer that writes JSON representation of MealCollections to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String saveLocation;

    // EFFECTS constructs writer to write file with given name to save location
    public JsonWriter(String saveLocation) {
        this.saveLocation = saveLocation;
    }

    // MODIFIES: this
    // EFFECTS: opens the writer or throws FileNotFoundException if an error occurs
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(saveLocation));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of the MealCollection to the file
    public void write(MealCollection mc) {
        JSONObject json = mc.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
        EventLog.getInstance().logEvent(new Event("Meal collection saved to: " + saveLocation));
    }

}