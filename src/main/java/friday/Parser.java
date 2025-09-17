package friday;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Provides parsing utilities for user commands and task serialization.
 */
public class Parser {

    /**
     * Parses a user input line into command and arguments.
     * 
     * @param line The full input line
     * @return A ParsedCommand object containing the command and arguments
     * @throws FridayException if the command is invalid
     */
    public static ParsedCommand parseCommand(String line) throws FridayException {
        if (line == null || line.trim().isBlank()) {
            throw new FridayException("Please enter a command.");
        }

        String trimmed = line.trim();
        assert trimmed.length() > 0 : "Trimmed line should not be empty after null/blank check";

        int spaceIndex = trimmed.indexOf(' ');
        String cmd;
        String rest;

        if (spaceIndex == -1) {
            cmd = trimmed;
            rest = "";
        } else {
            cmd = trimmed.substring(0, spaceIndex);
            rest = trimmed.substring(spaceIndex + 1).trim();
        }

        assert cmd != null && !cmd.isEmpty() : "Command should not be null or empty";
        assert rest != null : "Rest should not be null (can be empty)";

        return new ParsedCommand(cmd, rest);
    }

    /**
     * Parses deadline arguments from the rest string.
     * 
     * @param rest The arguments string (after "deadline ")
     * @return A DeadlineArgs object with description and date
     * @throws FridayException if parsing fails
     */
    public static DeadlineArgs parseDeadlineArgs(String rest) throws FridayException {
        if (rest == null || rest.isBlank()) {
            throw new FridayException("A deadline needs a description.");
        }

        assert rest.trim().length() > 0 : "Rest should have content after null/blank check";

        int byIndex = rest.indexOf("/by");
        String desc;
        String byStr = "";

        if (byIndex != -1) {
            desc = rest.substring(0, byIndex).trim();
            byStr = rest.substring(byIndex + 3).trim();
        } else {
            desc = rest;
        }

        assert desc != null : "Description should not be null";
        assert byStr != null : "By string should not be null (can be empty)";

        LocalDate by = null;
        if (!byStr.isBlank()) {
            try {
                by = LocalDate.parse(byStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                assert by != null : "Parsed date should not be null when parsing succeeds";
            } catch (DateTimeParseException e) {
                throw new FridayException(" Invalid date format. Use yyyy-MM-dd (e.g., 2025-10-15).");
            }
        }

        return new DeadlineArgs(desc, by);
    }

    /**
     * Parses event arguments from the rest string.
     * 
     * @param rest The arguments string (after "event ")
     * @return An EventArgs object with description, from, and to
     * @throws FridayException if parsing fails
     */
    public static EventArgs parseEventArgs(String rest) throws FridayException {
        if (rest == null || rest.isBlank()) {
            throw new FridayException("An event needs a description.");
        }

        int fromIndex = rest.indexOf("/from");
        int toIndex = rest.indexOf("/to");
        String desc;
        String from = "";
        String to = "";

        if (fromIndex != -1) {
            desc = rest.substring(0, fromIndex).trim();
            if (toIndex != -1 && toIndex > fromIndex) {
                from = rest.substring(fromIndex + 5, toIndex).trim();
                to = rest.substring(toIndex + 3).trim();
            } else {
                from = rest.substring(fromIndex + 5).trim();
            }
        } else {
            if (toIndex != -1) {
                desc = rest.substring(0, toIndex).trim();
                to = rest.substring(toIndex + 3).trim();
            } else {
                desc = rest;
            }
        }

        return new EventArgs(desc, from, to);
    }

    /**
     * Parses and validates a task index from string.
     * 
     * @param s The string representation of the index
     * @return The validated index (1-based)
     * @throws FridayException if parsing or validation fails
     */
    public static int parseIndex(String s) throws FridayException {
        if (s == null || s.isBlank()) {
            throw new FridayException("Please provide a task number.");
        }

        assert s.trim().length() > 0 : "String should have content after null/blank check";

        try {
            int idx = Integer.parseInt(s.trim());
            if (idx < 1) {
                throw new FridayException(" Task number must be 1 or greater.");
            }

            assert idx >= 1 : "Validated index should be 1 or greater";
            return idx;
        } catch (NumberFormatException e) {
            throw new FridayException(" '" + s + "' is not a valid task number.");
        }
    }

    /**
     * Parses a serialized task string into a Task object.
     * 
     * @param line The serialized task string
     * @return The parsed Task object
     */
    public static Task parseSerializedTask(String line) {
        assert line != null : "Input line should not be null";

        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return null; // malformed; skip
        }

        assert parts.length >= 3 : "Parts array should have at least 3 elements";

        String type = parts[0];
        boolean done = "1".equals(parts[1]);
        String desc = parts[2];

        assert type != null : "Task type should not be null";
        assert desc != null : "Task description should not be null";

        Task t = createTaskByType(type, desc, parts);

        if (t != null && done) {
            t.markDone();
        }

        assert t == null || t.getDesc().equals(desc) : "Created task should have the correct description";
        return t;
    }

    /**
     * Creates a task object based on the task type and parsed parts.
     * 
     * @param type  The task type identifier
     * @param desc  The task description
     * @param parts The split serialized data parts
     * @return The created Task object or null if type is unknown
     */
    private static Task createTaskByType(String type, String desc, String[] parts) {
        switch (type) {
            case "T":
                assert type.equals(TaskType.TODO.shortName()) : "Type should match TODO constant";
                return new ToDo(desc);
            case "D":
                assert type.equals(TaskType.DEADLINE.shortName()) : "Type should match DEADLINE constant";
                return parseDeadlineTask(desc, parts);
            case "E":
                assert type.equals(TaskType.EVENT.shortName()) : "Type should match EVENT constant";
                return parseEventTask(desc, parts);
            default:
                return null; // unknown type
        }
    }

    /**
     * Parses deadline-specific data from serialized parts.
     * 
     * @param desc  The task description
     * @param parts The split serialized data parts
     * @return A Deadline task object
     */
    private static Deadline parseDeadlineTask(String desc, String[] parts) {
        LocalDate by = null;
        if (parts.length >= 4 && !parts[3].isBlank()) {
            try {
                by = LocalDate.parse(parts[3], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                assert by != null : "Parsed deadline date should not be null";
            } catch (DateTimeParseException e) {
                // Skip if date invalid
                return null;
            }
        }
        return new Deadline(desc, by);
    }

    /**
     * Parses event-specific data from serialized parts.
     * 
     * @param desc  The task description
     * @param parts The split serialized data parts
     * @return An Event task object
     */
    private static Event parseEventTask(String desc, String[] parts) {
        String from = "";
        String to = "";
        if (parts.length >= 4) {
            from += parts[3];
            if (parts.length >= 6) {
                to += parts[5];
            }
        }
        return new Event(desc, from, to);
    }

    // Inner static classes for parsed results
    /**
     * Represents a parsed command with command and arguments.
     */
    public static class ParsedCommand {
        public final String command;
        public final String arguments;

        public ParsedCommand(String command, String arguments) {
            this.command = command;
            this.arguments = arguments;
        }
    }

    /**
     * Represents parsed arguments for a deadline task.
     */
    public static class DeadlineArgs {
        public final String description;
        public final LocalDate by;

        public DeadlineArgs(String description, LocalDate by) {
            this.description = description;
            this.by = by;
        }
    }

    /**
     * Represents parsed arguments for an event task.
     */
    public static class EventArgs {
        public final String description;
        public final String from;
        public final String to;

        public EventArgs(String description, String from, String to) {
            this.description = description;
            this.from = from;
            this.to = to;
        }
    }

    /**
     * Parses tag arguments from the rest string.
     * Expected format: "index tag" (e.g., "1 fun")
     * 
     * @param rest The arguments string (after "tag " or "untag ")
     * @return A TagArgs object with index and tag
     * @throws FridayException if parsing fails
     */
    public static TagArgs parseTagArgs(String rest) throws FridayException {
        if (rest == null || rest.trim().isBlank()) {
            throw new FridayException("Please provide a task number and tag.");
        }

        String trimmed = rest.trim();
        int spaceIndex = trimmed.indexOf(' ');

        if (spaceIndex == -1) {
            throw new FridayException("Please provide both a task number and a tag.");
        }

        String indexStr = trimmed.substring(0, spaceIndex).trim();
        String tagStr = trimmed.substring(spaceIndex + 1).trim();

        if (indexStr.isBlank()) {
            throw new FridayException("Please provide a task number.");
        }
        if (tagStr.isBlank()) {
            throw new FridayException("Please provide a tag.");
        }

        int index = parseIndex(indexStr);
        assert index >= 1 : "Parsed index should be 1 or greater";

        return new TagArgs(index, tagStr);
    }

    /**
     * Represents parsed arguments for tag operations.
     */
    public static class TagArgs {
        public final int index;
        public final String tag;

        public TagArgs(int index, String tag) {
            this.index = index;
            this.tag = tag;
        }
    }
}
