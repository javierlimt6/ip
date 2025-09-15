package friday;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.BufferedWriter;

/**
 * Handles saving and loading tasks to/from a file.
 */
public class Storage {
    private final Path dataFile;
    private final TaskList taskList;

    /**
     * Constructs a Storage with the given data file and task list.
     *
     * @param dataFile The path to the data file.
     * @param taskList The task list to save/load.
     */
    public Storage(Path dataFile, TaskList taskList) {
        assert dataFile != null : "Data file path should not be null";
        assert taskList != null : "Task list should not be null";
        
        this.dataFile = dataFile;
        this.taskList = taskList;
        
        assert this.dataFile == dataFile : "Data file should be correctly assigned";
        assert this.taskList == taskList : "Task list should be correctly assigned";
    }

    /**
     * Saves the current tasks to the data file.
     */
    public void save() {
        if (dataFile == null)
            return;
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
        if (dataFile == null || !Files.exists(dataFile))
            return;
            
        assert dataFile != null : "Data file should not be null when loading";
        assert Files.exists(dataFile) : "Data file should exist when loading";
        
        int initialSize = taskList.size();
        
        try {
            for (String line : Files.readAllLines(dataFile)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty())
                    continue;
                    
                assert trimmed.length() > 0 : "Trimmed line should not be empty";
                
                Task t = Parser.parseSerializedTask(trimmed);
                if (t != null) {
                    taskList.add(t);
                }
            }
        } catch (IOException e) {
            Ui.printWarning("Could not load tasks: " + e.getMessage());
        }
        
        assert taskList.size() >= initialSize : "Task list size should not decrease after loading";
    }

    /**
     * Serializes a task into a string format for storage.
     *
     * @param t The task to serialize.
     * @return The serialized string representation of the task.
     */
    private String serialize(Task t) {
        assert t != null : "Task to serialize should not be null";
        assert t.getDesc() != null : "Task description should not be null";
        
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
        
        assert type != null && !type.isEmpty() : "Task type should be determined";
        
        int doneFlag = t.isDone() ? 1 : 0;
        // Format: TYPE | doneFlag | description | extra (extra omitted if blank except
        // for Event delimiter form)
        if (type.equals("E")) {
            String result = String.join(" | ", type, String.valueOf(doneFlag), t.getDesc(), extra);
            assert result.contains(type) && result.contains(t.getDesc()) : "Serialized string should contain type and description";
            return result;
        }
        if (!extra.isBlank()) {
            String result = String.join(" | ", type, String.valueOf(doneFlag), t.getDesc(), extra);
            assert result.contains(type) && result.contains(t.getDesc()) : "Serialized string should contain type and description";
            return result;
        }
        String result = String.join(" | ", type, String.valueOf(doneFlag), t.getDesc());
        assert result.contains(type) && result.contains(t.getDesc()) : "Serialized string should contain type and description";
        return result;
    }
}
