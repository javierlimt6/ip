import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    private LocalDate by;
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    public Deadline(String desc, String byStr) throws FridayException {
        super(desc);
        if (byStr == null || byStr.isBlank()) {
            this.by = null;
        } else {
            try {
                this.by = LocalDate.parse(byStr, INPUT_FORMAT);
            } catch (DateTimeParseException e) {
                throw new FridayException(" Invalid date format. Use yyyy-mm-dd (e.g., 2019-10-15).");
            }
        }
    }

    @Override public TaskType getType() { return TaskType.DEADLINE; }

    @Override public String display() {
        String base = super.display();
        if (by != null) {
            return base + " (by: " + by.format(DISPLAY_FORMAT) + ")";
        }
        return base;
    }

    public LocalDate getBy() {
        return by;
    }

    // For serialization compatibility, return formatted string
    public String getByFormatted() {
        return by != null ? by.format(INPUT_FORMAT) : "";
    }
}
