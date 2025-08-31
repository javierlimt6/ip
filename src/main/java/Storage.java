import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.BufferedWriter;

public class Storage {
    private final Path dataFile;
    private final TaskList taskList;

    public Storage(Path dataFile, TaskList taskList) {
        this.dataFile = dataFile;
        this.taskList = taskList;
    }

    /**
     * Saves the current tasks to the data file.
     */
    public void save() {
        if (dataFile == null) return;
        try {
            if (Files.notExists(dataFile.getParent())) {
                Files.createDirectories(dataFile.getParent());
            }
            try (BufferedWriter bw = Files.newBufferedWriter(dataFile)) {
                for (Task t : taskList.getTasks()) {
                    bw.write(serialize(t));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            Ui.printWarning("Could not save tasks: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from the data file into the task list.
     */
    public void load() {
        if (dataFile == null || !Files.exists(dataFile)) return;
        try {
            for (String line : Files.readAllLines(dataFile)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;
                Task t = Parser.parseSerializedTask(trimmed);
                if (t != null) {
                    taskList.add(t);
                }
            }
        } catch (IOException e) {
            Ui.printWarning("Could not load tasks: " + e.getMessage());
        }
    }

    /**
     * Serializes a task into a string format for storage.
     */
    private String serialize(Task t) {
        String type;
        String extra = "";
        if (t instanceof ToDo) {
            type = "T";
        } else if (t instanceof Deadline) {
            type = "D";
            extra = ((Deadline) t).getByFormatted();
        } else if (t instanceof Event) {
            type = "E";
            Event ev = (Event) t;
            // Combine from/to with a delimiter so we can parse later: from || to
            String from = ev.getFrom() == null ? "" : ev.getFrom();
            String to = ev.getTo() == null ? "" : ev.getTo();
            extra = from + " || " + to; // always have the delimiter for parsing
        } else {
            type = "?"; // fallback
        }
        int doneFlag = t.isDone() ? 1 : 0;
        // Format: TYPE | doneFlag | description | extra (extra omitted if blank except for Event delimiter form)
        if (type.equals("E")) {
            return String.join(" | ", type, String.valueOf(doneFlag), t.desc, extra);
        }
        if (!extra.isBlank()) {
            return String.join(" | ", type, String.valueOf(doneFlag), t.desc, extra);
        }
        return String.join(" | ", type, String.valueOf(doneFlag), t.desc);
    }
}
