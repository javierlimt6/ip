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

        String type = determineTaskType(t);
        String extra = getTaskExtraData(t);
        int doneFlag = t.checkDone() ? 1 : 0;

        assert type != null && !type.isEmpty() : "Task type should be determined";

        return formatSerializedTask(type, doneFlag, t.getDesc(), extra);
    }

    /**
     * Determines the task type identifier for serialization.
     * 
     * @param t The task to get the type for
     * @return The task type identifier string
     */
    private String determineTaskType(Task t) {
        if (t instanceof ToDo) {
            return TaskType.TODO.shortName();
        } else if (t instanceof Deadline) {
            return TaskType.DEADLINE.shortName();
        } else if (t instanceof Event) {
            return TaskType.EVENT.shortName();
        } else {
            return "?"; // fallback
        }
    }

    /**
     * Extracts task-specific extra data for serialization.
     * 
     * @param t The task to get extra data from
     * @return The extra data string
     */
    private String getTaskExtraData(Task t) {
        if (t instanceof Deadline) {
            return ((Deadline) t).getByFormatted();
        } else if (t instanceof Event) {
            Event ev = (Event) t;
            // Combine from/to with a delimiter so we can parse later: from || to
            String from = ev.getFrom() == null ? "" : ev.getFrom();
            String to = ev.getTo() == null ? "" : ev.getTo();
            return from + " || " + to; // always have the delimiter for parsing
        }
        return "";
    }

    /**
     * Formats the serialized task string with proper delimiters.
     * 
     * @param type     The task type identifier
     * @param doneFlag The completion status flag (0 or 1)
     * @param desc     The task description
     * @param extra    The task-specific extra data
     * @return The formatted serialized string
     */
    private String formatSerializedTask(String type, int doneFlag, String desc, String extra) {
        // Format: TYPE | doneFlag | description | extra (extra omitted if blank except
        // for Event delimiter form)
        if (type.equals(TaskType.EVENT.shortName()) || !extra.isBlank()) {
            String result = String.join(" | ", type, String.valueOf(doneFlag), desc, extra);
            assert result.contains(type) && result.contains(desc)
                    : "Serialized string should contain type and description";
            return result;
        }

        String result = String.join(" | ", type, String.valueOf(doneFlag), desc);
        assert result.contains(type) && result.contains(desc)
                : "Serialized string should contain type and description";
        return result;
    }
}
