import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {

    /**
     * Parses a user input line into command and arguments.
     * @param line The full input line
     * @return A ParsedCommand object containing the command and arguments
     * @throws FridayException if the command is invalid
     */
    public static ParsedCommand parseCommand(String line) throws FridayException {
        if (line == null || line.trim().isBlank()) {
            throw new FridayException(" Please enter a command.");
        }

        String trimmed = line.trim();
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

        return new ParsedCommand(cmd, rest);
    }

    /**
     * Parses deadline arguments from the rest string.
     * @param rest The arguments string (after "deadline ")
     * @return A DeadlineArgs object with description and date
     * @throws FridayException if parsing fails
     */
    public static DeadlineArgs parseDeadlineArgs(String rest) throws FridayException {
        if (rest == null || rest.isBlank()) {
            throw new FridayException(" A deadline needs a description.");
        }

        int byIndex = rest.indexOf("/by");
        String desc;
        String byStr = "";

        if (byIndex != -1) {
            desc = rest.substring(0, byIndex).trim();
            byStr = rest.substring(byIndex + 3).trim();
        } else {
            desc = rest;
        }

        LocalDate by = null;
        if (!byStr.isBlank()) {
            try {
                by = LocalDate.parse(byStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                throw new FridayException(" Invalid date format. Use yyyy-MM-dd (e.g., 2025-10-15).");
            }
        }

        return new DeadlineArgs(desc, by);
    }

    /**
     * Parses event arguments from the rest string.
     * @param rest The arguments string (after "event ")
     * @return An EventArgs object with description, from, and to
     * @throws FridayException if parsing fails
     */
    public static EventArgs parseEventArgs(String rest) throws FridayException {
        if (rest == null || rest.isBlank()) {
            throw new FridayException(" An event needs a description.");
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
     * @param s The string representation of the index
     * @return The validated index (1-based)
     * @throws FridayException if parsing or validation fails
     */
    public static int parseIndex(String s) throws FridayException {
        if (s == null || s.isBlank()) {
            throw new FridayException(" Please provide a task number.");
        }

        try {
            int idx = Integer.parseInt(s.trim());
            if (idx < 1) {
                throw new FridayException(" Task number must be 1 or greater.");
            }
            return idx;
        } catch (NumberFormatException e) {
            throw new FridayException(" '" + s + "' is not a valid task number.");
        }
    }

    /**
     * Parses a serialized task string into a Task object.
     * @param line The serialized task string
     * @return The parsed Task object
     */
    public static Task parseSerializedTask(String line) {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return null; // malformed; skip
        }

        String type = parts[0];
        boolean done = "1".equals(parts[1]);
        String desc = parts[2];

        Task t = null;
        switch (type) {
            case "T":
                t = new ToDo(desc);
                break;
            case "D":
                LocalDate by = null;
                if (parts.length >= 4 && !parts[3].isBlank()) {
                    try {
                        by = LocalDate.parse(parts[3], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    } catch (DateTimeParseException e) {
                        // Skip if date invalid
                        return null;
                    }
                }
                t = new Deadline(desc, by);
                break;
            case "E":
                String from = "";
                String to = "";
                if (parts.length >= 4) {
                    String extra = parts[3];
                    String[] ft = extra.split("\\s*\\|\\|\\s*", -1); // keep empty
                    if (ft.length > 0) from = ft[0];
                    if (ft.length > 1) to = ft[1];
                }
                t = new Event(desc, from, to);
                break;
            default:
                return null; // unknown type
        }

        if (t != null && done) {
            t.markDone();
        }
        return t;
    }

    // Inner static classes for parsed results
    public static class ParsedCommand {
        public final String command;
        public final String arguments;

        public ParsedCommand(String command, String arguments) {
            this.command = command;
            this.arguments = arguments;
        }
    }

    public static class DeadlineArgs {
        public final String description;
        public final LocalDate by;

        public DeadlineArgs(String description, LocalDate by) {
            this.description = description;
            this.by = by;
        }
    }

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
}
