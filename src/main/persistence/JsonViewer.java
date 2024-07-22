package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.json.*;

import model.ExpenseEntry;
import model.ExpenseTracker;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonViewer {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonViewer(String source) {
        this.source = source;
    }

    // EFFECTS: reads expense tracker from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ExpenseTracker read() throws IOException {
        String jsonData = readFile(source); // String
        JSONObject jsonObject = new JSONObject(jsonData); // Construct a JSONObject from a source JSON text string.
        return parseExpenseTracker(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses expense tracker from JSON object and returns it
    private ExpenseTracker parseExpenseTracker(JSONObject jsonObject) {
        ExpenseTracker expenseTracker = new ExpenseTracker();
        parseListOfExpenseEntries(expenseTracker, jsonObject);
        parseUserInfo(expenseTracker, jsonObject);
        return expenseTracker;
    }

    // MODIFIES: ExpenseTracker
    // EFFECTS: parses list of expense entries from JSON object and adds them to
    // expense tracker
    private void parseListOfExpenseEntries(ExpenseTracker expenseTracker, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("listOfExpenseEntries");
        for (Object entryObject : jsonArray) {
            JSONObject nextEntry = (JSONObject) entryObject;
            parseExpenseEntry(expenseTracker, nextEntry);
        }
    }

    // MODIFIES: ExpenseTracker
    // EFFECTS: parses expense entry info from its JSON object and adds it to
    // expense tracker
    private void parseExpenseEntry(ExpenseTracker expenseTracker, JSONObject jsonObject) {

        String name = jsonObject.getString("name");
        String category = jsonObject.getString("category");
        double expenseAmount = jsonObject.getDouble("expenseAmount");
        String note = jsonObject.getString("note");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = jsonObject.getString("date");
        LocalDate date = LocalDate.parse(dateStr, formatter);

        ExpenseEntry entry = new ExpenseEntry(name, category, expenseAmount, note, date);
        expenseTracker.addExpenseEntry(entry);
    }

    private void parseUserInfo(ExpenseTracker expenseTracker, JSONObject jsonObject) {
        JSONObject jsonUserObject = jsonObject.getJSONObject("user");
        boolean overExpenseLimit = jsonUserObject.getBoolean("overExpenseLimit");
        double expenseLimit = jsonUserObject.getDouble("expenseLimit");
        expenseTracker.getUser().setExpenseLimit(expenseLimit);
        expenseTracker.getUser().setOverExpenseLimit(overExpenseLimit);
    }
}
